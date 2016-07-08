	package main;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.DocumentsDifference;
import entities.JsonProblem;
import features.core.Feature;
import features.core.FeaturesGenerator;
import features.helpers.DocumentPOSDistribution;

public class Main {

	// To run from console: mvn exec:java -Dexec.args="-i $inputDataset -o
	// $outputDir"
	public static void main(String[] args) throws Exception {
		// SET IsTrainMode
		Config.isTrainMode = !(new File(Trainer.TRAIN_FOLDER_PATH).exists());
		
		// SET isLocalRun
		Config.isLocalRun = (new File(Config.LOCAL_FILE_PATH).exists());
		
		// INIT POSTAG DIRS
		DocumentPOSDistribution.initDirs();

		// INIT
		Globals.init();

		// SET FOLDERS
		Config.setFolders(args);

		// SETUP INPUT
		File inputFolder = new File(Config.inputFolderPath);
		File inputInfoJson = new File(inputFolder, "info.json");

		Type jsonProblemListType = new TypeToken<ArrayList<JsonProblem>>() {
		}.getType();
		Globals.JsonProblems = new Gson().fromJson(FileUtils.readFileToString(inputInfoJson), jsonProblemListType);

		for (JsonProblem problem : Globals.JsonProblems) {
			String folderName = problem.getFolder();

			Date start = new Date();

			FeaturesGenerator.generateIdentificationDocs(inputFolder, problem);

			if (Config.isTrainMode) {
				FeaturesGenerator.setActualDifferences(folderName);
			}

			FeaturesGenerator.generateFeaturesDifferences(inputFolder, folderName);

			Date now = new Date();

			long total = now.getTime() - start.getTime();

			long seconds = total / 1000;
			long millis = total % 1000;

			System.out.println("Generated features: " + folderName + " (" + seconds + "."
					+ String.format("%03d", millis) + " sec)");
		}

		// TRAIN
		Trainer.setWekaAttributes();
		Trainer.trainResults();

		// CLEAR OUTPUT FOLDER
		System.out.println("Generate output");
		File outputFolder = new File(Config.outputFolderPath);

		if (!outputFolder.exists()) {
			outputFolder.mkdir();
		}

		FileUtils.cleanDirectory(outputFolder);

		// GENERATE RESULTS
		for (JsonProblem problem : Globals.JsonProblems) {
			
			Date start = new Date();

			String folderName = problem.getFolder();

			Results.generateResults(folderName);

			Results.generateOutput(new File(outputFolder, folderName));

			Date now = new Date();

			long total = now.getTime() - start.getTime();

			long seconds = total / 1000;
			long millis = total % 1000;

			System.out.println(
					"Generated output: " + folderName + " (" + seconds + "." + String.format("%03d", millis) + " sec)");
		}

		// Print Min and max score foreach feature
		for (Feature feature : Globals.Features) {
			System.out.println(
					feature.getName() + ": Min - " + feature.getMinScore() + " Max - " + feature.getMaxScore());
		}
		
		// Print mid scores for true and false documents foreach feature
		if (Config.isTrainMode) {
			Feature trainFeature = Globals.Features.get(Globals.Features.size() - 1);
			for (int i = 0; i < Globals.Features.size() - 1; i++) {
				Feature currentFeature = Globals.Features.get(i);

				int negativeCount = 0;
				double negativeTotal = 0.0;

				int positiveCount = 0;
				double positiveTotal = 0.0;

				for (String folder : Globals.IdentificationDocs.keySet()) {
					List<DocumentsDifference> currentDifferences = currentFeature.getDifferencesForFolder(folder);
					List<DocumentsDifference> trainDifferences = trainFeature.getDifferencesForFolder(folder);

					for (int j = 0; j < currentDifferences.size(); j++) {
						if (trainDifferences.get(j).getScore() == 1) {
							negativeCount++;
							negativeTotal += currentDifferences.get(j).getScore();
						} else {
							positiveCount++;
							positiveTotal += currentDifferences.get(j).getScore();
						}
					}
				}

				double negativeMid = negativeTotal / negativeCount;
				double positiveMid = positiveTotal / positiveCount;

				System.out.println(
						currentFeature.getName() + ": Positive - " + positiveMid + "; Negative - " + negativeMid);
			}
		}

		// Print mid scores for true and false for classified results
		if (Config.isTrainMode) {
			Feature trainFeature = Globals.Features.get(Globals.Features.size() - 1);
			
			int negativeCount = 0;
			double negativeTotal = 0.0;
			double negativeMax = Double.MIN_VALUE;
			double negativeMin = Double.MAX_VALUE;

			int positiveCount = 0;
			double positiveTotal = 0.0;
			double positiveMax = Double.MIN_VALUE;
			double positiveMin = Double.MAX_VALUE;

			for (String folder : Globals.IdentificationDocs.keySet()) {
				List<DocumentsDifference> trainDifferences = trainFeature.getDifferencesForFolder(folder);
				List<DocumentsDifference> calculatedRankings = Results.CalculatedRankings.get(folder);
				
				for (int i = 0; i < calculatedRankings.size(); i++) {
					double score = calculatedRankings.get(i).getScore();
					
					if(trainDifferences.get(i).getScore() == 1) {
						negativeCount++;
						negativeTotal += score;
						
						if(score < negativeMin) {
							negativeMin = score;
						}
						if(score > negativeMax) {
							negativeMax = score;
						}
					}
					else {
						positiveCount++;
						positiveTotal += score;

						if(score < positiveMin) {
							positiveMin = score;
						}
						if(score > positiveMax) {
							positiveMax = score;
						}
					}
				}
			}

			double negativeMid = negativeTotal / negativeCount;
			double positiveMid = positiveTotal / positiveCount;

			System.out.println("===== Total mid scores =====");
			System.out.println("Positive: Min: " + positiveMin + "; Middle: " + positiveMid + "; Max: " + positiveMax + ";");
			System.out.println("Negative: Min: " + negativeMin + "; Middle: " + negativeMid + "; Max: " + negativeMax + ";");
		}

		// CALCULATE ERROR ONLY IF TRAIN MODE
		if (Config.isTrainMode) {
			Errors.calculateError();

			System.out.println("Rankings MAP: " + Errors.RankingsMAP);
			System.out.println("Clusters F-score: " + Errors.ClustersFScore);
		}
		
		System.out.println("Finished!");
	}
}
