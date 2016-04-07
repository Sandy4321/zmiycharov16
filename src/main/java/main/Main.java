package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	// To run from console: mvn exec:java -Dexec.args="-i inputDir -o outputDir"
	public static void main(String[] args) throws Exception {
		try {
			// READ OPTIONS
			Options options = new Options();
			options.addOption("i", true, "path to input directory");
			options.addOption("o", true, "path to output directory");
			CommandLineParser parser = new BasicParser();
			CommandLine cmd = parser.parse(options, args);
			
			if(cmd.getOptionValue("i") == null) {
				throw new Exception("Missing parameters");
			}
	
			Config.inputFolderPath = cmd.getOptionValue("i");
			Config.outputFolderPath = cmd.getOptionValue("o");

			// TODO: Add real paths for tira
			Config.trainFolderPath = "F:/";
			Config.truthFolderPath = "F:/";
		}
		catch(Exception ex) {
			System.out.println("Folders NOT READ from command line!");
		}
		
		// TRAINING
		File trainFolder = new File(Config.trainFolderPath);
		File trainInfoJson = new File(trainFolder, "info.json");
		
		Type jsonProblemListType = new TypeToken<ArrayList<JsonProblem>>() {}.getType();
		List<JsonProblem> trainJsonProblems = new Gson().fromJson(FileUtils.readFileToString(trainInfoJson), jsonProblemListType);
		
		for(JsonProblem problem : trainJsonProblems) {
			String folderName = problem.getFolder();
			
			FeaturesGenerator.generateFeaturesSimilarities(trainFolder, folderName);
			
			FeaturesGenerator.setActualSimilarities(folderName);
		}
		FeaturesGenerator.trainResults();
		
		
		// CLEAR TRAINING SIMILARITIES
		FeaturesGenerator.clearTrainSimilarities();

		// CLEAR OUTPUT FOLDER
		File outputFolder = new File(Config.outputFolderPath);
		FileUtils.cleanDirectory(outputFolder);
		
		// SETUP INPUT
		File inputFolder = new File(Config.inputFolderPath);
		File inputInfoJson = new File(inputFolder, "info.json");
		
		List<JsonProblem> jsonProblems = new Gson().fromJson(FileUtils.readFileToString(inputInfoJson), jsonProblemListType);
		
		// GENERATE RESULTS
		for(JsonProblem problem : jsonProblems) {
			String folderName = problem.getFolder();
			
			FeaturesGenerator.generateFeaturesSimilarities(inputFolder, folderName);
			
			Results.generateResults(folderName);
			
			Results.generateOutput(new File(outputFolder, folderName));
		}
		
		// CALCULATE ERROR ONLY IF TRAIN AND INPUT ARE THE SAME
		if(Config.trainFolderPath.equals(Config.inputFolderPath)) {
			Results.calculateError();
		}
	}

}
