package main;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;

public class Config {
	public static final double MIN_SCORE_TO_RANK = 0.5;

	public static final String TRAIN_FILE_PATH = "./train.json";
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
			isTrainMode = true;
		}
		
		truthFolderPath = inputFolderPath + "/truth";
	}
}
