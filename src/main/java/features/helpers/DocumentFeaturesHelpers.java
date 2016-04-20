package features.helpers;

public class DocumentFeaturesHelpers {
	/**
	 * Returns the mean length of all sentences, provided as input parameter.
	 * 
	 * @param sentences
	 *            array of sentences
	 * @return
	 */
	public static double getMeanSentenceLength(String[] sentences) {
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
