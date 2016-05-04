package features.helpers;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import main.Config;

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

	public static double[] getPunctuationMarksPercentages(String content, String language) throws Exception {
		String line = FileUtils.readFileToString(new File(Config.PUNCTUATION_PATH));

		String[] punctuationMarks = line.split(" ");
		double[] result = new double[punctuationMarks.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = StringUtils.countMatches(content, punctuationMarks[i]);
		}

		int spacesCount = StringUtils.countMatches(content, " ");

		for (int i = 0; i < result.length; i++) {
			result[i] /= spacesCount;
		}

		return result;
	}

	public static double getUniqueWordsPercentage(String content, String language, String[] tokens) throws Exception {

		String[] unique = new HashSet<String>(Arrays.asList(tokens)).toArray(new String[0]);

		double result = (1.0 * unique.length) / tokens.length;

		return result;
	}

	/**
	 * Returns the Euclidean distance between the specified vectors. Note that
	 * both vectors must have the same number of elements.
	 * 
	 * @param first
	 * @param second
	 * @return
	 */
	public static Double getEuclideanDistance(Vector<Float> first, Vector<Float> second) {
		if (first == null || second == null) {
			throw new IllegalArgumentException("Please specify two vectors.");
		}
		if (first.size() != second.size()) {
			throw new RuntimeException("Both vectors must be in the same dimensional space.");
		}
		float sum = 0;
		for (int i = 0; i < first.size(); i++) {
			sum += Math.pow(first.get(i) - second.get(i), 2);
		}

		return Math.sqrt(sum);
	}
}
