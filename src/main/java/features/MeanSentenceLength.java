package features;

import featureHelpers.Document;
import featureHelpers.Feature;
import org.apache.lucene.wordnet.AnalyzerUtil;

public class MeanSentenceLength extends Feature {

	public MeanSentenceLength() {
		super("MeanSentenceLengthFeature");
	}

	@Override
	public double getSimilarity(Document doc1, Document doc2) {
		String[] sentencesDoc1 = AnalyzerUtil.getSentences(doc1.getContent(), 0);
		String[] sentencesDoc2 = AnalyzerUtil.getSentences(doc2.getContent(), 0);
		double mslDoc1 = getMeanSentenceLength(sentencesDoc1);
		double mslDoc2 = getMeanSentenceLength(sentencesDoc2);

		double result = mslDoc1 > mslDoc2 ? (mslDoc2 / mslDoc1) : (mslDoc1 / mslDoc2);
		return result;
	}

	/**
	 * Returns the mean length of all sentences, provided as input parameter.
	 * 
	 * @param sentences
	 *            array of sentences
	 * @return
	 */
	private double getMeanSentenceLength(String[] sentences) {
		if (sentences == null || sentences.length == 0) {
			return 0.0;
		}
		double result = 0.0;
		for (String sentence : sentences) {
			result += sentence.length();
		}
		return result / sentences.length;
	}
}
