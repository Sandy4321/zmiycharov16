package features.helpers;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;

import main.Config;

import opennlp.MyTokenizer;

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

	public static double[] getStopWordsPercentages(String content, String language) throws Exception {
		String line = null;

		if (language.equals(Config.LANG_EN)) {
			line = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_EN));
		}
		else if (language.equals(Config.LANG_GR)) {
			line = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_GR));
		}
		else if (language.equals(Config.LANG_NL)) {
			line = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_NL));
		}

		if (line != null) {
			List<String> stopWords = Arrays.asList(line.split(","));
			double[] result = new double[stopWords.size()];

			// TODO: Get tokens for language
			MyTokenizer tokenizer = new MyTokenizer("./opennlp/en-token.bin");
			String[] tokens = tokenizer.tokenize(content);

			for (int i = 0; i < tokens.length; i++) {
				if (stopWords.contains(tokens[i])) {
					result[stopWords.indexOf(tokens[i])]++;
				}
			}

			for (int i = 0; i < result.length; i++) {
				result[i] /= result.length;
			}

			return result;
		}

		return new double[0];
	}
}
