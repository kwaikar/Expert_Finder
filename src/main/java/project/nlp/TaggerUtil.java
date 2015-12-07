package project.nlp;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

/**
 * This class uses stanford tagger in order to tag the input sentence and
 * extract part of speech tags out of the same.
 * 
 * @author kanchan
 *
 */
public class TaggerUtil {

	MaxentTagger tagger = null;
	Set<String> stopWords = null;

	/**
	 * Load the Trained model in the memory.
	 */
	public TaggerUtil() {
		super();
		try {
			InputStream ioStream = getClass().getClassLoader().getResourceAsStream("resources/stopWords.txt");
			String stopWordString = IOUtils.toString(ioStream);
			stopWords = new HashSet<String>(); 
			for (String word : stopWordString.split("[\r|\n]")) {
				if (word.trim().length() > 1) {
					stopWords.add(word.trim().toLowerCase());
				}
			}
			tagger = new MaxentTagger(this.getClass()
					.getResource("/resources/POS_Tagged_Model/english-bidirectional-distsim.tagger").getFile());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method extracts all noun entities from given String. Input string is
	 * expected to be a valid english sentence
	 * 
	 * @param sentence
	 * @return
	 */
	public List<String> extractNounEntities(String sentences) {
		List<String> entities = new ArrayList<String>();
		String[] sentencesArr = sentences.split("[.?!]");
		for (String sentence : sentencesArr) {
			List<HasWord> sent = Sentence.toWordList(sentence.split(" "));
			List<TaggedWord> taggedSent = tagger.tagSentence(sent);
			String previousNoun = null;
			for (TaggedWord tw : taggedSent) {
				String word = tw.word().replaceAll("[^\\dA-Za-z ]", "");
				if (StringUtils.isNotBlank(word)) {
					if (tw.tag().startsWith("NN")) {
						if (previousNoun != null) {
							entities.remove(previousNoun);
							previousNoun = previousNoun + " " + word;
							if (!stopWords.contains(previousNoun.trim().toLowerCase().replaceAll("[:,//?]", ""))
									&& (!stopWords.contains(word.trim().toLowerCase()))) {
								entities.add(previousNoun);
							}
						} else {
							if (!stopWords.contains(word.toLowerCase()) && word.trim().length() > 1) {
								entities.add(word);
								previousNoun = word;
							} else {
								previousNoun = null;
							}
						}
					} else {
						previousNoun = null;
					}
				}
			}
		}
		return entities;
	}

}
