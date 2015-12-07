package project.nlp;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import junit.framework.TestCase;
import project.nlp.beans.Answer;
import project.nlp.beans.Items;
import project.nlp.beans.Owner;
import project.nlp.beans.QuestionDetails;
import project.nlp.beans.SimpleQuestion;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public void testWriteAndRead() throws Exception {

		InputReader ir = new InputReader();
		ir.expertiseExtractor("/resources/questions_smaller_subset.json");
		ObjectMapper mapper = new ObjectMapper();
		File inputQuestionAnswersData = new File(
				this.getClass().getResource("/resources/questions_smaller_subset.json").getFile());
		Items items = mapper.readValue(inputQuestionAnswersData, Items.class);
		int numQuestionsAnswered = 0;
		int numQuestionsAnsweredBySameUser = 0;
		for (QuestionDetails question : items.getItems()) {

			if (question.getIsAnswered()) {
				List<String> userIdsFound = ir.searchIndex(question, 5);
				for (Answer singleAnswer : question.getAnswers()) {
					if (singleAnswer.isIs_accepted()) {
						numQuestionsAnswered++;
						if (userIdsFound.contains(singleAnswer.getOwner().getUserId())) {
							numQuestionsAnsweredBySameUser++;
						}
						else
						{
							System.out.println("Question ID: "+question.getLink()+ " Could have been answered by "+userIdsFound.get(0));
						}
					}
				}
			}
		}
		System.out.println("Total Accuracy of the system (numQuestionsAnsweredBySameUser/numQuestionsAnswered)="+ ( 100* (double) numQuestionsAnsweredBySameUser/numQuestionsAnswered));

		// ir.readJson();
		/*
		 * String expertiseJsonFile =
		 * ir.getClass().getResource("/resources/UserExpertiseTest.json").
		 * getFile(); ir.loadIndex(expertiseJsonFile); ObjectMapper mapper = new
		 * ObjectMapper(); File questions = new
		 * File(ir.getClass().getResource("/resources/inputQuestion.json").
		 * getFile()); SimpleQuestion question = mapper.readValue(questions,
		 * SimpleQuestion.class); List<String> userIds
		 * =ir.searchIndex(question); Assert.assertEquals(userIds.get(0),
		 * "1400755");
		 * 
		 * Owner owner
		 * =ir.getBaseLineUser("/resources/questions_smaller_subset.json",
		 * "/resources/inputQuestion.json"); System.out.println("Owner found" +
		 * (owner!=null?owner:"Sorry nobody knows anything about it :( "));
		 * 
		 * 
		 * questions = new
		 * File(ir.getClass().getResource("/resources/dosQuestion.json").getFile
		 * ()); question = mapper.readValue(questions, SimpleQuestion.class);
		 * userIds =ir.searchIndex(question); System.out.println(userIds);
		 * Assert.assertEquals(userIds.get(0), "16549");
		 */

		/*
		 * 1. Set 1: 1 Java question answer, with non zero upvote,downvote count
		 * and has skill set of Java+Json+MVC Select abother onem, in which
		 * question has not been answered, but it has positive Upvotes on REST
		 * One question - android based one question - scala non java question
		 * 
		 * / When send a question to searchIndex method that has REST JAVA in
		 * it, it should return the second user, not first although second;s
		 * answer is not best answer
		 * 
		 * InputReader ir = new InputReader();
		 * ir.expertiseExtractor("/resources/questions_smaller_subset.json");
		 * questions = new
		 * File(ir.getClass().getResource("/resources/testQuestion1.json").
		 * getFile()); question = mapper.readValue(questions,
		 * SimpleQuestion.class); userIds =ir.searchIndex(question);
		 * System.out.println(userIds); Assert.assertEquals(userIds.get(0),
		 * "16549");
		 * 
		 * 
		 */

	}

}
