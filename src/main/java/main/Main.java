package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.JsonProblem;
import features.core.Feature;
import features.core.FeaturesGenerator;

public class Main {

	// To run from console: mvn exec:java -Dexec.args="-i $inputDataset -o $outputDir"
	public static void main(String[] args) throws Exception {
		// SET IsTrainMode
		Config.isTrainMode = !(new File(Trainer.TRAIN_FOLDER_PATH).exists());
		
		// INIT
		Globals.init();
		
		// SET FOLDERS
		Config.setFolders(args);

		// SETUP INPUT
		File inputFolder = new File(Config.inputFolderPath);
		File inputInfoJson = new File(inputFolder, "info.json");
		
		Type jsonProblemListType = new TypeToken<ArrayList<JsonProblem>>() {}.getType();
		Globals.JsonProblems = new Gson().fromJson(FileUtils.readFileToString(inputInfoJson), jsonProblemListType);
		
		for(JsonProblem problem : Globals.JsonProblems) {
			String folderName = problem.getFolder();

			Date start = new Date();
			
			FeaturesGenerator.generateIdentificationDocs(inputFolder, problem);
			
			if(Config.isTrainMode) {
				FeaturesGenerator.setActualSimilarities(folderName);
			}

			FeaturesGenerator.generateFeaturesSimilarities(inputFolder, folderName);
			
			Date now = new Date();
			
			long total = now.getTime() - start.getTime();
			
			long seconds = total/1000;
			long millis = total%1000;
			
			System.out.println("Generated features: " + folderName + " (" + seconds + "." + String.format("%03d", millis) + " sec)");
		}
		
		// TRAIN
		Trainer.setWekaAttributes();
		Trainer.trainResults();

		// CLEAR OUTPUT FOLDER
		System.out.println("Generate output");
		File outputFolder = new File(Config.outputFolderPath);
		
		if(!outputFolder.exists()) {
			outputFolder.mkdir();
		}
		
		FileUtils.cleanDirectory(outputFolder);
		
		// GENERATE RESULTS
		for(JsonProblem problem : Globals.JsonProblems) {

			Date start = new Date();
			
			String folderName = problem.getFolder();
			
			Results.generateResults(folderName);
			
			Results.generateOutput(new File(outputFolder, folderName));

			Date now = new Date();
			
			long total = now.getTime() - start.getTime();
			
			long seconds = total/1000;
			long millis = total%1000;
			
			System.out.println("Generated output: " + folderName + " (" + seconds + "." + String.format("%03d", millis) + " sec)");
		}
		
		// CALCULATE ERROR ONLY IF TRAIN MODE
		if(Config.isTrainMode) {
			Errors.calculateError();
			
			System.out.println("Rankings MAP: " + Errors.RankingsMAP);
			System.out.println("Clusters F-score: " + Errors.ClustersFScore);
		}
		
		for(Feature feature : Globals.Features) {
			System.out.println(feature.getName() + ": Min - " + feature.getMinScore() + " Max - " + feature.getMaxScore());
		}

		System.out.println("Finished!");
	}

}
