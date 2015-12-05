package project.nlp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import project.nlp.beans.OntologyNode;
import project.nlp.beans.UserExpertise;

/**
 * This class builds Lucene index for the given schema and searches the same.
 */
public class IndexAndSearch {

	Path indexPath = null;
	StandardAnalyzer analyzer = null;
	IndexWriter writer = null;

	public IndexAndSearch(String path) throws Exception {
		indexPath = Paths.get(path);
		analyzer = new StandardAnalyzer();

		writer = openIndexWriter();
	}

	/**
	 * This method closes index writer provided.
	 * 
	 * @param writer
	 * @throws IOException
	 */
	public void closeIndexWriter() throws IOException {
		writer.commit();
		writer.close();
	}

	/**
	 * This method opens the index writer
	 * 
	 * @return
	 * @throws IOException
	 */
	private IndexWriter openIndexWriter() throws IOException {
		Directory index = FSDirectory.open(indexPath);
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		IndexWriter writer = new IndexWriter(index, config);
		return writer;
	}

	/**
	 * This method deletes documents from the index based on query provided.
	 * 
	 * @param queryString
	 * @throws IOException
	 * @throws ParseException
	 */
	public void deleteDocuments(String queryStringColumn, String queryStringValue) throws IOException, ParseException {
		IndexWriter w = openIndexWriter();
		Query query = new QueryParser(queryStringColumn, analyzer).parse(queryStringValue);
		w.deleteDocuments(query);
		closeIndexWriter();
	}

	/**
	 * This method writes the document to index.
	 * 
	 * @param title
	 * @param isbn
	 * @throws IOException
	 */
	public void indexDoc(String userId, Collection<UserExpertise> expertise) throws IOException {
		Document doc = new Document();
		FieldType myStringType = new FieldType(TextField.TYPE_STORED);
		myStringType.setOmitNorms(false);
		for (UserExpertise expert : expertise) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < expert.getWeight(); i++) {
				sb.append(expert.getSkill() + " ");
			}
			Field field = new Field("skill", sb.toString(), myStringType);
			// field.setBoost( ((Double)expert.getWeight()).floatValue());
			doc.add(field);
		}
		doc.add(new StringField("userId", userId, Field.Store.YES));
		writer.addDocument(doc);
	}

	/**
	 * This method searches index and returns documents found.
	 * 
	 * @param queryString
	 * @return
	 * @throws IOException
	 */
	public List<Document> searchIndex(List<OntologyNode> skills) throws IOException {
		int hitsPerPage = 10;
		Query query = null;

		IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));
		IndexSearcher searcher = new IndexSearcher(reader);

		try {
			StringBuilder sb = new StringBuilder();
			for (OntologyNode ontologyNode : skills) {
				sb.append(" " + ontologyNode.getEntity().toLowerCase());
			}
			query = new QueryParser("skill", analyzer).parse(sb.toString() /* "wildfly and rest" */);
		} catch (Exception e) {
			e.printStackTrace();
		}
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		System.out.println("Found " + hits.length + " hits.");
		List<Document> docs = new LinkedList<Document>();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			docs.add(d);
			System.out.println((i + 1) + ". " + d.get("userId") + "\t");
		}
		reader.close();
		return docs;
	}

}
