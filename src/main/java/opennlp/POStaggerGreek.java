package opennlp;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cs.nlp.postagger.BigSetFunctions;
import gr.aueb.cs.nlp.postagger.SmallSetFunctions;
import gr.aueb.cs.nlp.postagger.WordWithCategory;

public class POStaggerGreek {
	public static final short SMALL_SET_CLASSIFY = 0;
	public static final short BIG_SET_CLASSIFY = 1;

	/**
	 * Returns all part of speech tags of all words from the specified text.
	 * 
	 * @param text
	 *            the text to be tagged.
	 * @param option
	 *            <li>{@value #SMALL_SET_CLASSIFY} if only the part of speech is
	 *            necessary
	 *            <li>{@value #BIG_SET_CLASSIFY} provides more information, e.g.
	 *            "noun/nominative/feminine/singular/"
	 * 
	 * @return Map with all words from text and the corresponding category.
	 */
	public static Map<String, String> getPOStags(String text, short option) {
		if (text == null || text.trim().isEmpty()) {
			return Collections.emptyMap();
		}
		Map<String, String> resultMap = new HashMap<String, String>();
		List<WordWithCategory> list = null;
		if (option == SMALL_SET_CLASSIFY) {
			list = SmallSetFunctions.smallSetClassifyString(text);
		} else if (option == BIG_SET_CLASSIFY) {
			list = BigSetFunctions.bigSetClassifyString(text);
		} else {
			throw new RuntimeException("Invalid option for POS tag classification !");
		}
		for (WordWithCategory word : list) {
			resultMap.put(word.getWord(), word.getCategory());
		}
		return resultMap;
	}
}
