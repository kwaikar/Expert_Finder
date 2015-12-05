package project.nlp;

import java.io.File;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.AnswerUserList;
import project.nlp.beans.ExpertUser;
import project.nlp.beans.Items;
import project.nlp.beans.OntologyNode;
import project.nlp.beans.QuestionDetails;
import project.nlp.beans.UserExpertise;

/**
 * Hello world!
 *
 */
public class InputReader {
	private static String url = "https://api.stackexchange.com/2.2/search?page=8&pagesize=100&order=desc&sort=activity&tagged=java&nottagged=android&site=stackoverflow";
	private static String answers = "https://api.stackexchange.com/2.2/answers/33452399/?order=desc&sort=activity&site=stackoverflow&filter=withbody";

	private static TaggerUtil tagger = null;

	static {
		tagger = new TaggerUtil();
	}

	public void readJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		File unigramsFile = new File(this.getClass().getResource("/resources/questions_smaller_subset.json").getFile());
		Items items = mapper.readValue(unigramsFile, Items.class);
		if (!new File("S:/nlp/index").exists()) {
		IndexAndSearch indexer = new IndexAndSearch("S:/nlp/index");
		
			Map<String, ExpertUser> expertUsers = new HashMap<String, ExpertUser>();
			for (QuestionDetails question : items.getItems()) {

				// questions.put(question.getQuestionId(), question);
				// sentences.add(cleanedString(question.getTitle()));
				System.out.println(question.getQuestionId());
				// owners.put(question.getOwner().getUserId(),
				// question.getOwner());
				Set<String> questionSentence = new HashSet<String>();
				questionSentence.add(cleanedString(question.getBody()));
				questionSentence.add(cleanedString(question.getTitle()));
				List<OntologyNode> questionOntology = extractOntology(questionSentence);
				for (Answer answer : question.getAnswers()) {
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
			System.out.println("Map Received-->" + expertUsers);
			int BEST_ANSWER_WEIGHT = 10;
			int BEST_ANSWERTAG_WEIGHT = 100;
			int NORMALIZED_UPVOTE_WEIGHT = 8;
			int NORMALIZED_DOWNVOTE_WEIGHT = -8;
			for (ExpertUser expertUserEntry : expertUsers.values()) {
				for (AnswerUserList entry : expertUserEntry.getAnswerUserList()) {
					// Feature 1 : If Best Answer, import entire
					if (entry.isBestAnswer()) {
						for (OntologyNode answeredOntology : entry.getOntologyNode()) {
							expertUserEntry = incrementWeightForIdentifiedSkill(
									BEST_ANSWER_WEIGHT * answeredOntology.getFrequency(), expertUserEntry,
									answeredOntology.getEntity());
						}
						for (String tag : entry.getTags()) {
							expertUserEntry = incrementWeightForIdentifiedSkill(BEST_ANSWERTAG_WEIGHT, expertUserEntry,
									tag);
						}
					}

					// NORMALIZED_UPVOTE_WEIGHT
					for (OntologyNode answeredOntology : entry.getOntologyNode()) {
						expertUserEntry = incrementWeightForIdentifiedSkill(
								NORMALIZED_UPVOTE_WEIGHT
										* ((double) entry.getNoOfUpvotes() / entry.getThreadUpvotesMaxCount())
										* answeredOntology.getFrequency(),
								expertUserEntry, answeredOntology.getEntity());
					}

					// NORMALIZED_DOWNVOTE_WEIGHT
					for (OntologyNode answeredOntology : entry.getOntologyNode()) {
						expertUserEntry = incrementWeightForIdentifiedSkill(
								NORMALIZED_DOWNVOTE_WEIGHT
										* ((double) entry.getNoOfDownvotes() / entry.getThreadmaxDownvoteCount())
										* answeredOntology.getFrequency(),
								expertUserEntry, answeredOntology.getEntity());
					}
					System.out.println("-->" + expertUserEntry.getUserExpertise());
				}

				// Since all the answers have been iterated and ontology has
				// been
				// extracted, reset the List.
				expertUserEntry.setAnswerUserList(null);
			}

			for (ExpertUser entry : expertUsers.values()) {
				indexer.indexDoc(entry.getOwner().getUserId(), entry.getUserExpertise().values());
			}
			System.out.println(expertUsers);
			indexer.closeIndexWriter();
			ObjectMapper obj = new ObjectMapper();
			FileUtils.writeStringToFile(new File("s:/UserExpertise.json"),
					obj.writeValueAsString(expertUsers.values()));

		}

	}

	/**
	 * @param BEST_ANSWER_WEIGHT
	 * @param expertUserEntry
	 * @param answeredOntology
	 */
	public ExpertUser incrementWeightForIdentifiedSkill(double BEST_ANSWER_WEIGHT, ExpertUser expertUserEntry,
			String skill) {
		UserExpertise expertise = expertUserEntry.getUserExpertise().get(skill);
		if (expertise == null) {
			expertise = new UserExpertise(skill);
		}
		expertise.setWeight(expertise.getWeight() + BEST_ANSWER_WEIGHT);
		expertUserEntry.getUserExpertise().put(skill, expertise);
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
				.replaceAll("(?s)<a.*>.*?</a>", "").replaceAll("&#39;", "'")/**/;
	}

	public static void main(String[] args) throws Exception {
		// Create an instance of HttpClient.

		InputReader ir = new InputReader();
		/*
		 * String str =
		 * "<p>For this code, numbers are printed from 1-10 for Thread <code>t1</code> and after that for thread <code>t2</code>.</p>\n\n<pre><code>class Synchtest {\n    public static void main(String args[]) {\n        synchtest2 a = new synchtest2();\n        Thread t1 = new Thread(a);\n        Thread t2 = new Thread(a);\n        t1.start();\n        t2.start();\n    }\n}\n\nclass synchtest2 extends Thread {\n\n    public synchronized void run() {\n        for (int i = 0; i &lt;= 10; i++) {\n            System.out.println(i);\n            try {\n                sleep(1000);\n            } catch (Exception e) {\n            }\n        }\n    }\n}\n</code></pre>\n\n<p>but for the below code they printed as 00 11 22 33 44 55 and so on. </p>\n\n<pre><code>class Synchtest {\n    public static void main(String args[]) {\n\n        synchtest2 t = new synchtest2();\n        synchtest2 t2 = new synchtest2();\n        t.start();\n        t2.start();\n    }\n}\n\nclass synchtest2 extends Thread {\n\n    public synchronized void run() {\n        for (int i = 0; i &lt;= 10; i++) {\n            System.out.println(i);\n            try {\n                sleep(1000);\n            } catch (Exception e) {\n\n            }\n\n        }\n    }\n}\n</code></pre>\n"
		 * ; System.out.println(ir.cleanedString(str));
		 */
		String file = FileUtils.readFileToString(new File("s:/UserExpertise.json"));
		ObjectMapper mapper = new ObjectMapper();
		List<ExpertUser> expertUsers = mapper.readValue(file, new TypeReference<List<ExpertUser>>() {
		});
		IndexAndSearch indexer = new IndexAndSearch("S:/nlp/idx1");
		
		for (ExpertUser entry : expertUsers) {
			indexer.indexDoc(entry.getOwner().getUserId(), entry.getUserExpertise().values());
		}
		indexer.searchIndex(new ArrayList<OntologyNode>());
		System.out.println(expertUsers);
		indexer.closeIndexWriter();
		
		//ir.readJson();
	}
}
