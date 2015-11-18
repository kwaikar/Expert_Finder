package project.nlp;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.Items;
import project.nlp.beans.OntologyNode;
import project.nlp.beans.Owner;
import project.nlp.beans.QuestionDetails;

/**
 * Hello world!
 *
 */
public class InputReader {
	private static String url = "https://api.stackexchange.com/2.2/search?page=8&pagesize=100&order=desc&sort=activity&tagged=java&nottagged=android&site=stackoverflow";
	private static String answers = "https://api.stackexchange.com/2.2/answers/33452399/?order=desc&sort=activity&site=stackoverflow&filter=withbody";

	public void readJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		File unigramsFile = new File(this.getClass().getResource("/resources/questions_subset.json").getFile());
		Items items = mapper.readValue(unigramsFile, Items.class);

		Map<String, QuestionDetails> questions = new LinkedHashMap<String, QuestionDetails>();
		Map<String, Answer> answers = new LinkedHashMap<String, Answer>();
		Map<String, Owner> owners = new LinkedHashMap<String, Owner>();
		Set<String> sentences = new HashSet<String>();
		for (QuestionDetails question : items.getItems()) {
			questions.put(question.getQuestionId(), question);
			sentences.add(cleanedString(question.getTitle()));
			System.out.println(question.getQuestionId());
			owners.put(question.getOwner().getUserId(), question.getOwner());
			for (Answer answer : question.getAnswers()) {
				answers.put(answer.getAnswer_id(), answer);
				System.out.println(answer.getAnswer_id());
			}
		}
		TaggerUtil tagger = new TaggerUtil();
		Map<String, OntologyNode> ontologyMap = new HashMap<String, OntologyNode>();

		for (String sentence : sentences) {
			List<String> entries = tagger.extractNounEntities(sentence);
			for (String node : entries) {
				OntologyNode nodeFromMap = ontologyMap.get(node.toLowerCase().trim());
				if (nodeFromMap != null) {
					nodeFromMap.addFrequency();
				}
				else
				{
					ontologyMap.put(node, new OntologyNode(node.toLowerCase().trim()));
				}
			}
		}
		List<OntologyNode> ontoList= new ArrayList<OntologyNode>(ontologyMap.values());
		Collections.sort(ontoList, new Comparator<OntologyNode>() {
			public int compare(OntologyNode o1, OntologyNode o2) {
				// TODO Auto-generated method stub
				return o2.getFrequency().compareTo(o1.getFrequency());
			}
		});
		System.out.println(ontoList);
	}

	private String cleanedString(String str) {
		return str.replaceAll("<p>", "").replaceAll("</p>", "").replaceAll("&amp;", "&").replaceAll("&#39;", "'");
	}

	public static void main(String[] args) throws Exception {
		// Create an instance of HttpClient.

		InputReader ir = new InputReader();
		ir.readJson();
	}
}
