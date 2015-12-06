package project.nlp;

import java.io.File;
import java.util.List;

import org.apache.lucene.document.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import junit.framework.TestCase;
import project.nlp.beans.Items;
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
		Assert.assertEquals(userIds.get(0), "2597143");

	}

}
