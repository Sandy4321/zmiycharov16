package features.helpers;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import main.Config;

import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.TokenizerFactory;

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

	public static double getUniqueWordsPercentage(String content, String language) throws Exception {
		AbstractTokenizer tokenizer = TokenizerFactory.get(language);
		String[] tokens = tokenizer.tokenize(content);

		String[] unique = new HashSet<String>(Arrays.asList(tokens)).toArray(new String[0]);
		
		double result = (1.0 * unique.length) / tokens.length;

		return result;
	}
}
