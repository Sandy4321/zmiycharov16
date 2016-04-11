package main;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import featureHelpers.Document;
import featureHelpers.DocumentsSimilarity;
import featureHelpers.Feature;

/**
 * Performs simple logistic regression. User: tpeng Date: 6/22/12 Time: 11:01 PM
 * 
 * @author tpeng
 * @author Matthieu Labas
 */
public class Logistic {

	/** the learning rate */
	private double rate;

	/** the number of iterations */
	private int ITERATIONS = 3000;

	public Logistic() {
		this.rate = 0.0001;
	}

	private static double sigmoid(double z) {
		return 1.0 / (1.0 + Math.exp(-z));
	}

	public void train(List<Instance> instances) {
		for (int n = 0; n < ITERATIONS; n++) {

			System.out.println("Train iteration: " + (n + 1));

			for (Instance instance : instances) {
				double predicted = classify(instance);

				for (int i = 0; i < Globals.getCustomFeaturesCount(); i++) {
					Feature feature = Globals.Features.get(i);
					double currentWeight = feature.getWeight();
					double featureScore = instance.scores.get(feature.getName());

					feature.setWeight(
							currentWeight + rate * (instance.actualScore - predicted) * featureScore);
				}
			}
		}
	}

	public static double classify(Instance instance) {
		double logit = .0;

		for (int i = 0; i < Globals.getCustomFeaturesCount(); i++) {
			Feature feature = Globals.Features.get(i);
			logit += feature.getWeight() * instance.scores.get(feature.getName());
		}

		return sigmoid(logit);
	}

	public static double classify(String folderName, int similarityIndex) {
		double logit = .0;

		for (int i = 0; i < Globals.getCustomFeaturesCount(); i++) {
			Feature feature = Globals.Features.get(i);
			logit += feature.getWeight() * feature.getSimilaritiesForFolder(folderName).get(similarityIndex).getScore();
		}

		return sigmoid(logit);
	}

	public static class Instance {
		public String folderName;
		public String document1;
		public String document2;
		public Map<String, Double> scores;
		public double actualScore;

		public Instance() {
		}

		public Instance(String folderName, String document1, String document2, int similarityIndex) {
			this.folderName = folderName;
			this.document1 = document1;
			this.document1 = document2;
			this.scores = new HashMap<String, Double>();

			int i = 0;
			for (; i < Globals.getCustomFeaturesCount(); i++) {
				Feature feature = Globals.Features.get(i);

				this.scores.put(feature.getName(),
						feature.getSimilaritiesForFolder(folderName).get(similarityIndex).getScore());
			}

			Feature trainFeature = Globals.Features.get(i);

			this.actualScore = trainFeature.getSimilaritiesForFolder(folderName).get(similarityIndex).getScore();
		}
	}

	public static List<Instance> readDataSet() throws FileNotFoundException {
		List<Instance> dataset = new ArrayList<Instance>();

		for (String folderName : Globals.DocFiles.keySet()) {
			System.out.println("Add to train folder: " + folderName);

			int similarityIndex = 0;

			List<File> docFiles = Globals.DocFiles.get(folderName);
			for (int i = 0; i < docFiles.size() - 1; i++) {
				File file1 = docFiles.get(i);
				for (int j = i + 1; j < docFiles.size(); j++, similarityIndex++) {
					File file2 = docFiles.get(j);

					dataset.add(new Instance(folderName, file1.getName(), file2.getName(), similarityIndex));
				}
			}
		}

		return dataset;
	}

	// LEARN
	public static void trainResults() throws Exception {
		File trainFile = new File(Config.TRAIN_FILE_PATH);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		if (Config.isTrainMode) {

			List<Instance> instances = readDataSet();
			Logistic logistic = new Logistic();
			logistic.train(instances);

			trainFile.createNewFile();
			FileUtils.write(trainFile, gson.toJson(Globals.FeaturesWeights));
		} else {
			Type mapType = new TypeToken<HashMap<String, Double>>() {}.getType();
			Globals.FeaturesWeights = gson.fromJson(FileUtils.readFileToString(trainFile), mapType);
		}
	}

}