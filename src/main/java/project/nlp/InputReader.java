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
import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;

import project.nlp.beans.Answer;
import project.nlp.beans.AnswerUserList;
import project.nlp.beans.ExpertUser;
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

	private static TaggerUtil tagger = null;

	static {
		tagger = new TaggerUtil();
	}

	public void readJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		File unigramsFile = new File(this.getClass().getResource("/resources/questions_subset.json").getFile());
		Items items = mapper.readValue(unigramsFile, Items.class);

		Map<String, QuestionDetails> questions = new LinkedHashMap<String, QuestionDetails>();
		Map<String, Answer> answers = new LinkedHashMap<String, Answer>();
		Map<String, Owner> owners = new LinkedHashMap<String, Owner>();
		Map<String, ExpertUser> expertUser = new HashMap<String, ExpertUser>();
		List<OntologyNode> completeOntoList = new ArrayList<OntologyNode>();

		for (QuestionDetails question : items.getItems()) {

			// questions.put(question.getQuestionId(), question);
			// sentences.add(cleanedString(question.getTitle()));
			System.out.println(question.getQuestionId());
			// owners.put(question.getOwner().getUserId(), question.getOwner());
			for (Answer answer : question.getAnswers()) {

				if (!(answer.getDownVotes() == 0 && answer.getUpVotes() == 0)) {
					Set<String> sentences = new HashSet<String>();
					sentences.add(cleanedString(question.getBody()));
					sentences.add(cleanedString(question.getTitle()));

					sentences.add(cleanedString(answer.getBody()));
					sentences.add(cleanedString(answer.getTitle()));

					List<OntologyNode> ontoList = extractOntology(sentences);

					ExpertUser expertUsr = expertUser.get(answer.getOwner().getUserId());
					if (expertUsr == null) {
						expertUsr = new ExpertUser();
						expertUsr.setOwner(answer.getOwner());
						expertUser.put(answer.getOwner().getUserId(), expertUsr);
						List<AnswerUserList> answerUserList = (expertUsr.getAnswerUserList());
						if (answerUserList == null || answerUserList.size() == 0) {
							answerUserList = new ArrayList<AnswerUserList>();
						}
						answerUserList.add(new AnswerUserList(answer, ontoList));
						expertUsr.setAnswerUserList(answerUserList);

						System.out.println(expertUsr);
					}

				}

			}
			System.out.println(expertUser);
		}
		reducer(completeOntoList);
		System.out.println(completeOntoList);
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
	private void reducer(List<OntologyNode> ontoList) {
		Collections.sort(ontoList, new Comparator<OntologyNode>() {
			public int compare(OntologyNode o1, OntologyNode o2) {
				// TODO Auto-generated method stub
				return o2.getFrequency().compareTo(o1.getFrequency());
			}
		});
	}

	private String cleanedString(String str) {
		return str.replaceAll("(?s)<code>.*?</code>", "").replaceAll("(?s)<pre>.*?</pre>", "")
				.replaceAll("<strong>", "").replaceAll("</strong>", "").replaceAll("</br>", "").replaceAll("<em>", "")
				.replaceAll("</em>", "").replaceAll("\n", " ").replaceAll("  ", " ").replaceAll("<p>", "")
				.replaceAll("</p>", "").replaceAll("&amp;", "&").replaceAll("(?s)<a.*>.*?</a>", "")
				.replaceAll("&#39;", "'")/**/;
	}

	public static void main(String[] args) throws Exception {
		// Create an instance of HttpClient.

		InputReader ir = new InputReader();
		/*
		 * String str =
		 * "<p>For this code, numbers are printed from 1-10 for Thread <code>t1</code> and after that for thread <code>t2</code>.</p>\n\n<pre><code>class Synchtest {\n    public static void main(String args[]) {\n        synchtest2 a = new synchtest2();\n        Thread t1 = new Thread(a);\n        Thread t2 = new Thread(a);\n        t1.start();\n        t2.start();\n    }\n}\n\nclass synchtest2 extends Thread {\n\n    public synchronized void run() {\n        for (int i = 0; i &lt;= 10; i++) {\n            System.out.println(i);\n            try {\n                sleep(1000);\n            } catch (Exception e) {\n            }\n        }\n    }\n}\n</code></pre>\n\n<p>but for the below code they printed as 00 11 22 33 44 55 and so on. </p>\n\n<pre><code>class Synchtest {\n    public static void main(String args[]) {\n\n        synchtest2 t = new synchtest2();\n        synchtest2 t2 = new synchtest2();\n        t.start();\n        t2.start();\n    }\n}\n\nclass synchtest2 extends Thread {\n\n    public synchronized void run() {\n        for (int i = 0; i &lt;= 10; i++) {\n            System.out.println(i);\n            try {\n                sleep(1000);\n            } catch (Exception e) {\n\n            }\n\n        }\n    }\n}\n</code></pre>\n"
		 * ; System.out.println(ir.cleanedString(str));
		 */
		ir.readJson();
	}
}
