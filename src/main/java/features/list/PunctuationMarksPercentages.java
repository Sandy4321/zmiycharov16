package features.list;

import features.core.Feature;
import features.core.IdentificationDocument;

public class PunctuationMarksPercentages extends Feature {

	public PunctuationMarksPercentages() {
		super("PunctuationMarksPercentagesFeature");
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		double[] doc1Percentages = doc1.getPunctuationMarksPercentages();
		double[] doc2Percentages = doc2.getPunctuationMarksPercentages();

		double result = 0.0;

		for(int i =0;i<doc1Percentages.length;i++) {
			result += Math.abs(doc1Percentages[i] - doc2Percentages[i]);
		}

		return result;
	}
}
