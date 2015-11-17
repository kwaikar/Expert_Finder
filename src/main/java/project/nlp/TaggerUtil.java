package project.nlp;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.unitils.reflectionassert.ReflectionAssert;
import org.unitils.reflectionassert.ReflectionComparatorMode;

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

	/**
	 * Load the Trained model in the memory.
	 */
	public TaggerUtil() {
		super();
		try {
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
				if (StringUtils.isNotBlank(tw.word())) {
					if (tw.tag().startsWith("NN")) {
						if (previousNoun != null) {
							entities.remove(previousNoun);
							previousNoun = previousNoun + " " + tw.word();
							entities.add(previousNoun);
						} else {
							entities.add(tw.word());
							System.out.println(tw.word() + " : " + tw.tag());
							previousNoun = tw.word();
						}
					} else {
						previousNoun = null;
					}
				}
			}
		}
		System.out.println(entities);
		return entities;
	}
 
}
