package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static void main(String[] args) throws Exception {
		
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
