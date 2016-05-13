package features.list;

import features.core.Feature;
import features.core.IdentificationDocument;

public class MeanSentenceLength extends Feature {

	public MeanSentenceLength() {
		super("MeanSentenceLengthFeature");
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		double mslDoc1 = doc1.getMeanSentenceLength();
		double mslDoc2 = doc2.getMeanSentenceLength();

		double result = Math.abs(mslDoc1 - mslDoc2);
		return result;
	}
}
