package features.list;

import features.core.Feature;
import features.core.IdentificationDocument;

public class UniqueWordsPercentages extends Feature {

	public UniqueWordsPercentages() {
		super("UniqueWordsPercentages");
	}

	@Override
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
		double doc1Percentage = doc1.getUniqueWordsPercentage();
		double doc2Percentage = doc2.getUniqueWordsPercentage();

		double result = 1- Math.abs(doc1Percentage - doc2Percentage);

		return result;
	}
}
