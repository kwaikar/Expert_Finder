package project.nlp;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.AnswerUserList;
import project.nlp.beans.ExpertUser;
import project.nlp.beans.Items;
import project.nlp.beans.OntologyNode;
import project.nlp.beans.Owner;
import project.nlp.beans.Question;
import project.nlp.beans.QuestionDetails;
import project.nlp.beans.SimpleQuestion;
import project.nlp.beans.UserExpertise;

/**
 * This class Reads Stack overflow data files and exposes methods that return
 * the Most skillful users who can answer the question based on their answering
 * history on stackoverflow!
 *
 */
public class InputReader {
	private static String url = "https://api.stackexchange.com/2.2/search?page=8&pagesize=100&order=desc&sort=activity&tagged=java&nottagged=android&site=stackoverflow";
	private static String answers = "https://api.stackexchange.com/2.2/answers/33452399/?order=desc&sort=activity&site=stackoverflow&filter=withbody";

	double BEST_ANSWER_WEIGHT = 10;
	double BEST_ANSWERTAG_WEIGHT = 20;
	double NORMALIZED_UPVOTE_WEIGHT = 1.5;
	double NORMALIZED_DOWNVOTE_WEIGHT = -1.5;
	
	private static TaggerUtil tagger = null;

	Set<String> topSkills = new HashSet<String>();

	static {
		tagger = new TaggerUtil();
	}

	/**
	 * This method returns the Baseline user. It first parses and extracts
	 * ontology from the question and returns the user who has knowledge about
	 * the topic
	 * 
	 * @param fileName
	 * @param inputQuestion
	 * @return
	 * @throws Exception
	 */
	public Owner getBaseLineUser(String fileName, String inputQuestion) throws Exception {

		ObjectMapper mapper = new ObjectMapper();

		File inputQuestionAnswersData = new File(this.getClass().getResource(fileName).getFile());
		Items items = mapper.readValue(inputQuestionAnswersData, Items.class);

		File questions = new File(this.getClass().getResource(inputQuestion).getFile());
		SimpleQuestion question = mapper.readValue(questions, SimpleQuestion.class);
		List<String> nodes = tagger.extractNounEntities(question.getTitle());

		for (QuestionDetails singleQuestion : items.getItems()) {

			for (Answer answer : singleQuestion.getAnswers()) {
				int count = 0;
				for (String string : nodes) {
					if (answer.getBody().contains(string)) {
						count++;
					}
					if (count == (nodes.size() - 1)) {
						System.out.println("Found User who answered question on the topic "
								+ answer.getOwner().getUserId() + "->" + singleQuestion.getLink());
						return answer.getOwner();
					}
				}
			}
		}
		return null;
	}

	/**
	 * This method reads the Json input file and loads the index.
	 * 
	 * @param fileName
	 * @throws Exception
	 */
	public void expertiseExtractor(String fileName) throws Exception {

		ObjectMapper mapper = new ObjectMapper();
		// if (!new File("S:/nlp/index").exists()) {
		IndexAndSearch indexer = getIndexerAndSearcher();

		Map<String, ExpertUser> expertUsers = new HashMap<String, ExpertUser>();
		List<String> fileNames = new ArrayList<String>();
		fileNames.add(fileName);
		/*
		 * for (int i = 1; i < 11; i++) {
		 * fileNames.add("/resources/Production_Data/question_" + i + ".json");
		 * }
		 */
		for (int i = 0; i < fileNames.size(); i++) {

			File inputQuestionAnswersData = new File(this.getClass().getResource(fileNames.get(i)).getFile());
			Items items = mapper.readValue(inputQuestionAnswersData, Items.class);
			for (QuestionDetails question : items.getItems()) {

				System.out.println(question.getQuestionId());
				List<OntologyNode> questionOntology = extractQuestionOntology(question);
				for (Answer answer : question.getAnswers()) {
					for (String tag : answer.getTags()) {
						topSkills.add(tag.trim().toLowerCase());
					}
					for (String tag : question.getTags()) {
						topSkills.add(tag.trim().toLowerCase());
					}
					if (answer.getUpVotes() > question.getThreadmaxCount()) {
						question.setThreadmaxCount(answer.getUpVotes());
					}
					if (answer.getDownVotes() < question.getThreadmaxDownvoteCount()) {
						question.setThreadmaxDownvoteCount(answer.getThreadmaxDownvoteCount());
					}
				}

				for (Answer answer : question.getAnswers()) {

					answer.setThreadUpvoteCount(question.getThreadmaxCount());
					if ((answer.getScore() != 0)) {
						Set<String> sentences = new HashSet<String>();

						sentences.add(cleanedString(answer.getBody()));
						sentences.add(cleanedString(answer.getTitle()));

						List<OntologyNode> ontoList = extractOntology(sentences);
						ontoList.addAll(questionOntology);
						ontoList = reducer(ontoList);
						ExpertUser expertUsr = expertUsers.get(answer.getOwner().getUserId());
						if (expertUsr == null) {
							expertUsr = new ExpertUser();
						}
						expertUsr.setOwner(answer.getOwner());
						expertUsers.put(answer.getOwner().getUserId(), expertUsr);
						List<AnswerUserList> answerUserList = (expertUsr.getAnswerUserList());
						if (answerUserList == null || answerUserList.size() == 0) {
							answerUserList = new ArrayList<AnswerUserList>();
						}
						answerUserList.add(new AnswerUserList(answer, ontoList));
						expertUsr.setAnswerUserList(answerUserList);

						sentences = null;
					}
					answer = null;
				}
			}
		}
		System.out.println("Map Received-->" + expertUsers);

		for (ExpertUser expertUserEntry : expertUsers.values()) {
			for (AnswerUserList entry : expertUserEntry.getAnswerUserList()) {
				// Feature 1 : If Best Answer, import entire
				if (entry.isBestAnswer()) {
					for (String tag : entry.getTags()) {
						expertUserEntry = incrementWeightForIdentifiedSkill(BEST_ANSWERTAG_WEIGHT, expertUserEntry,
								tag);
					}

					for (OntologyNode answeredOntology : entry.getOntologyNode()) {
						expertUserEntry = incrementWeightForIdentifiedSkill(
								BEST_ANSWER_WEIGHT * answeredOntology.getFrequency(), expertUserEntry,
								answeredOntology.getEntity());
					}

				}

				// NORMALIZED_UPVOTE_WEIGHT
				for (OntologyNode answeredOntology : entry.getOntologyNode()) {
					expertUserEntry = incrementWeightForIdentifiedSkill(NORMALIZED_UPVOTE_WEIGHT
							* ((double) entry.getNoOfUpvotes() / entry.getThreadUpvotesMaxCount())
							* answeredOntology.getFrequency(), expertUserEntry, answeredOntology.getEntity());
				}

				// NORMALIZED_DOWNVOTE_WEIGHT
				for (OntologyNode answeredOntology : entry.getOntologyNode()) {
					expertUserEntry = incrementWeightForIdentifiedSkill(NORMALIZED_DOWNVOTE_WEIGHT
							* ((double) entry.getNoOfDownvotes() / entry.getThreadmaxDownvoteCount())
							* answeredOntology.getFrequency(), expertUserEntry, answeredOntology.getEntity());
				}
				System.out.println("-->" + expertUserEntry.getUserExpertise());
			}

			// Since all the answers have been iterated and ontology has
			// been
			// extracted, reset the List.
			expertUserEntry.setAnswerUserList(null);
		}

		for (ExpertUser entry : expertUsers.values()) {
			indexer.indexDoc(entry);
		}
		System.out.println(expertUsers);
		indexer.closeIndexWriter();
		ObjectMapper obj = new ObjectMapper();
		File outputJsonFileName = new File(("/resources/UserExpertiseTest.json"));
		System.out.println("Filename->" + outputJsonFileName.getAbsolutePath());
		FileUtils.writeStringToFile((outputJsonFileName), obj.writeValueAsString(expertUsers.values()));

		// }

	}

	/**
	 * @param question
	 * @return
	 */
	public List<OntologyNode> extractQuestionOntology(Question question) {
		Set<String> questionSentence = new HashSet<String>();
		questionSentence.add(cleanedString(question.getBody()));
		questionSentence.add(cleanedString(question.getTitle()));
		List<OntologyNode> questionOntology = extractOntology(questionSentence);
		return questionOntology;
	}

	/**
	 * @param BEST_ANSWER_WEIGHT
	 * @param expertUserEntry
	 * @param answeredOntology
	 */
	private ExpertUser incrementWeightForIdentifiedSkill(double BEST_ANSWER_WEIGHT, ExpertUser expertUserEntry,
			String skill) {

		UserExpertise expertise = (!topSkills.contains(skill.toLowerCase().trim()))
				? expertUserEntry.getUserExpertise().get(skill) : expertUserEntry.getTopSkills().get(skill);
		if (expertise == null) {
			expertise = new UserExpertise(skill);
		}
		expertise.setWeight(expertise.getWeight() + BEST_ANSWER_WEIGHT);
		if (!topSkills.contains(skill.toLowerCase().trim())) {
			if (!expertUserEntry.getTopSkills().containsKey(skill)) {
				expertUserEntry.getUserExpertise().put(skill, expertise);
			}
		} else {
			expertUserEntry.getTopSkills().put(skill, expertise);
		}
		return expertUserEntry;

	}

	/**
	 * This method extracts ontology from given sentences
	 * 
	 * @param sentences
	 * @return
	 */
	private List<OntologyNode> extractOntology(Set<String> sentences) {
		List<OntologyNode> ontoList;
		Map<String, OntologyNode> ontologyMap = new HashMap<String, OntologyNode>();

		for (String sentence : sentences) {
			List<String> entries = tagger.extractNounEntities(sentence);
			sentence = null;

			for (String node : entries) {
				if (StringUtils.isNotBlank(node)) {
					String replacedString = node.replaceAll("[-+.^:,()]", "").trim();
					OntologyNode nodeFromMap = ontologyMap.get(replacedString);
					if (nodeFromMap != null) {
						nodeFromMap.addFrequency();
					} else {
						ontologyMap.put(replacedString, new OntologyNode(replacedString));
					}
				}
			}
			entries = null;
		}
		ontoList = new ArrayList<OntologyNode>(ontologyMap.values());
		ontologyMap = null;
		Collections.sort(ontoList, new Comparator<OntologyNode>() {
			public int compare(OntologyNode o1, OntologyNode o2) {
				// TODO Auto-generated method stub
				return o2.getFrequency().compareTo(o1.getFrequency());
			}
		});
		return ontoList;
	}

	/**
	 * @param ontoList
	 */
	private List<OntologyNode> reducer(List<OntologyNode> ontoList) {

		List<OntologyNode> tempList = new ArrayList<OntologyNode>();
		Map<String, OntologyNode> map = new HashMap<String, OntologyNode>();
		for (OntologyNode ontologyNode : ontoList) {
			OntologyNode entry = map.get(ontologyNode.getEntity());
			if (entry == null) {
				entry = ontologyNode;
			} else {
				entry.addFrequency(ontologyNode.getFrequency());
			}
			map.put(ontologyNode.getEntity(), entry);
		}
		tempList = new ArrayList<OntologyNode>(map.values());
		Collections.sort(tempList, new Comparator<OntologyNode>() {
			public int compare(OntologyNode o1, OntologyNode o2) {
				// TODO Auto-generated method stub
				return o2.getFrequency().compareTo(o1.getFrequency());
			}
		});
		return tempList;
	}

	private String cleanedString(String str) {
		return str.replaceAll("(?s)<code>.*?</code>", "").replaceAll("(?s)<pre>.*?</pre>", "")
				.replaceAll("<strong>", "").replaceAll("</strong>", "").replaceAll("</br>", "").replaceAll("<em>", "")
				.replaceAll("</em>", "").replaceAll("\n", " ").replaceAll("  ", " ").replaceAll("<blockquote>", "")
				.replaceAll("</blockquote>", "").replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("&amp;", "&")
				.replaceAll("\\\\", "").replaceAll("'", "").replaceAll("\"", "").replaceAll("<ul>", "")
				.replaceAll("<li>", "").replaceAll("</ul>", "").replaceAll("</li>", "")
				.replaceAll("(?s)<a.*>.*?</a>", "").replaceAll("&#39;", "'")/**/;
	}

	public static void main(String[] args) throws Exception {

		InputReader ir = new InputReader();
		ir.expertiseExtractor("/resources/questions_smaller_subset.json");
	}

	/**
	 * @param expertiseJsonFile
	 * @throws IOException
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws Exception
	 */
	public void loadIndex(String expertiseJsonFile)
			throws IOException, JsonParseException, JsonMappingException, Exception {
		String file = FileUtils.readFileToString(new File(expertiseJsonFile));
		ObjectMapper mapper = new ObjectMapper();
		List<ExpertUser> expertUsers = mapper.readValue(file, new TypeReference<List<ExpertUser>>() {
		});
		IndexAndSearch indexer = getIndexerAndSearcher();
		indexer.deleteDocuments("*", "*");
		for (ExpertUser entry : expertUsers) {
			indexer.indexDoc(entry);
		}
		System.out.println(expertUsers);
		indexer.closeIndexWriter();

	}
	
	/**
	 * This method accepts a Question object and returns set of user Ids it finds relevant and matching.
	 * @param question
	 * @return
	 * @throws Exception
	 */
	public List<String> searchIndex(Question question) throws Exception {
		IndexAndSearch indexer = getIndexerAndSearcher();
		List<OntologyNode> questionOntology = extractQuestionOntology(question);
		System.out.println("Ontology found in the question {"+questionOntology+"}");
		List<String> userIds = indexer.searchIndex(questionOntology);
		indexer.closeIndexWriter();
		return userIds;
	}

	/**
	 * @return
	 * @throws Exception
	 */
	public IndexAndSearch getIndexerAndSearcher() throws Exception {
		File f = new File("Expertise_Index");
		System.out.println("Creating index on " + f.getAbsolutePath());
		IndexAndSearch indexer = new IndexAndSearch(f.getAbsolutePath());
		return indexer;
	}
}
