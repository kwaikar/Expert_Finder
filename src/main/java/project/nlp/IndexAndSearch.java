package project.nlp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.FieldInvertState;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import project.nlp.beans.ExpertUser;
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
		Query query = new QueryParser(queryStringColumn, analyzer).parse(queryStringValue);
		writer.deleteDocuments(query);
	}

	/**
	 * This method writes the document to index.
	 * 
	 * @param title
	 * @param isbn
	 * @throws IOException
	 */
	public void indexDoc(ExpertUser experts) throws IOException {
		Document doc = new Document();
		FieldType myStringType = new FieldType(TextField.TYPE_NOT_STORED);
		myStringType.setOmitNorms(false);
		Set<String> skills = new HashSet<String>();
		Set<String> topSkills = new HashSet<String>();

		for (UserExpertise expert : experts.getUserExpertise().values()) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < expert.getWeight() * 100; i++) {
				sb.append(expert.getSkill() + " ");

			}
			skills.add(expert.getSkill() + "[" + ((expert.getWeight() * 100 / 100)) + "]");
			Field field = new Field("skill", sb.toString(), myStringType);
			// field.setBoost( ((Double)expert.getWeight()).floatValue());
			doc.add(field);
		}

		for (UserExpertise expert : experts.getTopSkills().values()) {

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < expert.getWeight() * 100; i++) {
				sb.append(expert.getSkill() + " ");

			}
			topSkills.add(expert.getSkill() + "[" + ((expert.getWeight() * 100 / 100)) + "]");
			Field field = new Field("topSkill", sb.toString(), myStringType);
			doc.add(field);
		}

		doc.add(new StringField("skills", skills.toString(), Field.Store.YES));
		doc.add(new StringField("topSkills", topSkills.toString(), Field.Store.YES));
		doc.add(new StringField("link", experts.getOwner().getLink(), Field.Store.YES));
		doc.add(new StringField("userId", experts.getOwner().getUserId(), Field.Store.YES));
		writer.addDocument(doc);
	}

	/**
	 * This method searches index and returns documents found.
	 * 
	 * @param queryString
	 * @return
	 * @throws IOException
	 */
	public List<String> searchIndex(List<OntologyNode> skills) throws IOException {
		int hitsPerPage = 10;
		Query query = null;

		IndexReader reader = DirectoryReader.open(FSDirectory.open(indexPath));
		IndexSearcher searcher = new IndexSearcher(reader);

		try {
			StringBuilder sb = new StringBuilder();
			for (OntologyNode ontologyNode : skills) {
				
				if(ontologyNode.getEntity().trim().length()>1){
				sb.append((sb.toString().length() == 0 ? " " : " OR ") + ontologyNode.getEntity().toLowerCase().replaceAll("[^\\dA-Za-z ]", ""));
				}
			}
			System.out.println("Primary Skill Set identified: " + sb.toString());
			String queryStr =  "skill:" + sb.toString() + " OR (topSkill:" + sb.toString()+ ")^50" ;
			query = new QueryParser("skill", analyzer).parse(queryStr /* "wildfly and rest" */);
			System.out.println("==>" + query);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Similarity sim = new DefaultSimilarity() {
			@Override
			public float idf(long docFreq, long numDocs) {
				//common terms are less important than uncommon ones
				return 1;
			}

			@Override
			public float tf(float freq) {
				// TODO Auto-generated method stub
				return freq;
			}

			@Override
			public float queryNorm(float sumOfSquaredWeights) {
				// a term matched in fields with less terms have a higher score
				return 1;
			}

			@Override
			public float coord(int overlap, int maxOverlap) {
				// of the terms in the query, a document that contains more terms will have a higher score
				return super.coord(overlap, maxOverlap) * 100;
			}
		};
		searcher.setSimilarity(sim);
		System.out.println(searcher.getDefaultSimilarity());
		TopDocs results = searcher.search(query, 5 * hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;
		System.out.println("Found " + hits.length + " hits.");
		List<String> docs = new LinkedList<String>();
		for (int i = 0; i < hits.length; ++i) {
			int docId = hits[i].doc;
			Document d = searcher.doc(docId);
			docs.add(d.get("userId"));
			System.out.println((i + 1) + ". " + d.get("userId") + "\t" + d.get("topSkills") + "\t" + d.get("skills"));
		}
		reader.close();
		return docs;
	}

}
