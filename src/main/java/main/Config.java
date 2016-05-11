package main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class Config {
	public static final boolean ARE_RESULTS_NORMALIZED = false;
	
	public static final double MIN_SCORE_TO_RANK = 0.75;

	public static final String LANG_EN = "en";
	public static final String LANG_NL = "nl";
	public static final String LANG_GR = "gr";

	public static final String STOP_WORDS_PATH_EN = "./dependencies/dictionaries/stopWords/english.csv";
	public static final String STOP_WORDS_PATH_NL = "./dependencies/dictionaries/stopWords/dutch.csv";
	public static final String STOP_WORDS_PATH_GR = "./dependencies/dictionaries/stopWords/greek.csv";

	public static final String PUNCTUATION_PATH = "./dependencies/dictionaries/punctuationMarks.txt";
	
	public static final String TOKENIZR_PATH_EN = "./dependencies/opennlp/en-token.bin";
	public static final String TOKENIZR_PATH_NL = "./dependencies/opennlp/nl-token.bin";
	
	public static final String POSTAGGER_PATH_EN = "./dependencies/opennlp/en-pos-maxent.bin";
	public static final String POSTAGGER_PATH_NL = "./dependencies/opennlp/nl-pos-maxent.bin";
	
	public static boolean isTrainMode;
	
	public static String inputFolderPath;
	public static String outputFolderPath;
	public static String truthFolderPath;
	
	public static void setFolders(String[] args) throws Exception {
		// SET FOLDERS
		Options options = new Options();
		options.addOption("i", true, "path to input directory");
		options.addOption("o", true, "path to output directory");
		CommandLineParser parser = new BasicParser();
		CommandLine cmd = parser.parse(options, args);
		
		if(cmd.getOptionValue("i") != null && cmd.getOptionValue("o") != null) {
			inputFolderPath = cmd.getOptionValue("i");
			outputFolderPath = cmd.getOptionValue("o");
		}
		else {
			inputFolderPath = "./_DATA/dataset";
			outputFolderPath = "./_DATA/output";
		}
		
		truthFolderPath = inputFolderPath + "/truth";
	}
}
