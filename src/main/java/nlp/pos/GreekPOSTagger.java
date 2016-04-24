package nlp.pos;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import gr.aueb.cs.nlp.postagger.SmallSetFunctions;
import gr.aueb.cs.nlp.postagger.WordWithCategory;

public class GreekPOSTagger extends AbstractPOSTagger {

	@Override
	public LinkedHashMap<String, String> tag(String input) {
		List<WordWithCategory> taggedWords = SmallSetFunctions.smallSetClassifyString(input);
		LinkedHashMap<String, String> result = new LinkedHashMap<String, String>();
		for(WordWithCategory word: taggedWords) {
			result.put(word.getWord(), word.getCategory());
		}
		return result;
	}
}
