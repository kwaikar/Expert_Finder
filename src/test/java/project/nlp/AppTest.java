package project.nlp;

import java.util.List;

import org.apache.lucene.document.Document;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
	public void testWriteAndRead() throws Exception {
		IndexAndSearch indexAndSearch = new IndexAndSearch("/index");
	/*	indexAndSearch.indexDocuments();
		List<Document> docs = indexAndSearch.searchIndex("*", "*");
		Assert.assertEquals("Test document indexing and Search",docs.size(), 1);
		indexAndSearch.deleteDocuments("*", "*");
		docs = indexAndSearch.searchIndex("*", "*");
		Assert.assertEquals("Test document deletion",docs.size(), 0);
*/	}

}
