package project.nlp;

import java.io.File;
import java.util.List;

import org.apache.lucene.document.Document;

import com.fasterxml.jackson.databind.ObjectMapper;

import junit.framework.Assert;
import junit.framework.TestCase;
import project.nlp.beans.Items;

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
		File unigramsFile = new File(ir.getClass().getResource("/resources/questions_smaller_subset.json").getFile());
		Items items = mapper.readValue(unigramsFile, Items.class);
		List<String> userIds =ir.searchIndex(items.getItems().get(0));
		Assert.assertEquals(userIds.get(0), "2597143");

	}

}
