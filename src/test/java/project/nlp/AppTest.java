package project.nlp;

import java.io.File;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import junit.framework.TestCase;
import project.nlp.beans.Owner;
import project.nlp.beans.SimpleQuestion;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public void testWriteAndRead() throws Exception {

		InputReader ir = new InputReader();
		// ir.readJson();
		String expertiseJsonFile = ir.getClass().getResource("/resources/UserExpertiseTest.json").getFile();
		 ir.loadIndex(expertiseJsonFile);
		ObjectMapper mapper = new ObjectMapper();
		File questions = new File(ir.getClass().getResource("/resources/inputQuestion.json").getFile());
		SimpleQuestion question = mapper.readValue(questions, SimpleQuestion.class);
		List<String> userIds =ir.searchIndex(question);
		Assert.assertEquals(userIds.get(0), "16549");
		
		Owner owner =ir.getBaseLineUser("/resources/questions_smaller_subset.json","/resources/inputQuestion.json");
		System.out.println("Owner found" + (owner!=null?owner:"Sorry nobody knows anything about it :( "));
		

		  questions = new File(ir.getClass().getResource("/resources/testQuestion1.json").getFile());
		  question = mapper.readValue(questions, SimpleQuestion.class);
		 userIds =ir.searchIndex(question);
		 System.out.println(userIds);
		Assert.assertEquals(userIds.get(0), "16549");
		

	}

}
