package nlp.pos;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import features.helpers.POSTagEntry;
import gr.aueb.cs.nlp.postagger.SmallSetFunctions;
import gr.aueb.cs.nlp.postagger.WordWithCategory;

public class GreekPOSTagger extends AbstractPOSTagger {

	@Override
	public LinkedList<POSTagEntry> tag(String input) {
		
		List<WordWithCategory> taggedWords = SmallSetFunctions.smallSetClassifyString(input);
		LinkedList<POSTagEntry> result = new LinkedList<POSTagEntry>();
		for (WordWithCategory word : taggedWords) {
			if (!result.contains(word.getWord())) {
				POSTagEntry newEntry = new POSTagEntry(word.getWord(), word.getCategory(), 1);
				result.add(newEntry);
			} else {
				POSTagEntry existingEntry = result.get(result.indexOf(word.getWord()));
				Integer.sum(existingEntry.getNumberOfOccurrences(), 1);
				result.set(result.indexOf(word.getWord()), existingEntry);
			}
		}

		return result;
	}
}
