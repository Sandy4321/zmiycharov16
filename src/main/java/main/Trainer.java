package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import features.core.Feature;
import features.core.IdentificationDocument;
import features.list.Train_Feature;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;

public class Trainer {
	public static final String TRAIN_FOLDER_PATH = "./train/";
	public static final String TRAIN_DATA_FILE = "train.arff";
	public static final String TRAIN_CLASSIFIER_FILE = "classifier.model";

	private static String getTrainDataPath() {
		return TRAIN_FOLDER_PATH + TRAIN_DATA_FILE;
	}
	
	private static String getClassifierPath() {
		return TRAIN_FOLDER_PATH + TRAIN_CLASSIFIER_FILE;
	}
	
	private static FastVector wekaAttributes;
	private static Classifier trainClassifier;
	
	private static Instances getTrainInstances() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(getTrainDataPath()));
		Instances result = new Instances(reader);
		result.setClassIndex(Globals.Features.size() - 1);
		
		return result;
	}
	
	public static void setWekaAttributes() {
		wekaAttributes = new FastVector(Globals.Features.size());

		for (int i = 0; i < Globals.Features.size(); i++) {
			Feature feature = Globals.Features.get(i);
			wekaAttributes.addElement(new Attribute(feature.getName()));
		}
		
		if(!Config.isTrainMode) {
			wekaAttributes.addElement(new Attribute("Result"));
		}
	}

	public static void generateDataSet() throws Exception {
		Feature lastFeature = Globals.Features.get(Globals.Features.size() - 1);
		int totalCount = 0;
		for (String folder : Globals.IdentificationDocs.keySet()) {
			totalCount += lastFeature.getSimilaritiesForFolder(folder).size();
		}

		// Create an empty training set
		Instances trainingSet = new Instances("Train", wekaAttributes, totalCount);
		// Set class index
		trainingSet.setClassIndex(Globals.Features.size() - 1);

		for (String folder : Globals.IdentificationDocs.keySet()) {
			for (int siimilarityIndex = 0; siimilarityIndex < lastFeature.getSimilaritiesForFolder(folder)
					.size(); siimilarityIndex++) {

				// Create the instance
				Instance instance = new Instance(Globals.Features.size());

				for (int featureIndex = 0; featureIndex < Globals.Features.size()-1; featureIndex++) {
					double similarity = Globals.Features.get(featureIndex).getSimilaritiesForFolder(folder)
							.get(siimilarityIndex).getScore();

					instance.setValue((Attribute) wekaAttributes.elementAt(featureIndex), similarity);

				}
				
				double lastSimilarity = lastFeature.getSimilaritiesForFolder(folder)
						.get(siimilarityIndex).getScore();

				instance.setValue((Attribute) wekaAttributes.elementAt(Globals.Features.size()-1), lastSimilarity);
				
				// Multiply positive to match negative
				if(lastSimilarity == 1) {
					int multiplyTimes = Globals.FolderEvaluations.get(folder).multiplyNumberForDocument;
					for (int i = 0; i < multiplyTimes; i++) {
						trainingSet.add(instance);
					}
				}

				// add the instance
				trainingSet.add(instance);
			}
		}

		FileUtils.write(new File(getTrainDataPath()), trainingSet.toString());
	}

	private static void generateClassifier() throws Exception {
		Instances trainingSet = getTrainInstances();
		LibSVM classifier = new LibSVM();
		classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_EPSILON_SVR, LibSVM.TAGS_SVMTYPE));
		classifier.buildClassifier(trainingSet);
		
		weka.core.SerializationHelper.write(getClassifierPath(), classifier);
	}

	public static double classify(String folderName, int similarityIndex) throws Exception {

		Instances instances = getTrainInstances();

		// Create the instance
		Instance instance = new Instance(Globals.Features.size());
		
		for (int featureIndex = 0; featureIndex < Globals.Features.size()-1; featureIndex++) {
			double similarity = Globals.Features.get(featureIndex).getSimilaritiesForFolder(folderName)
					.get(similarityIndex).getScore();

			instance.setValue(featureIndex, similarity);
		}
		
		instance.setValue(Globals.Features.size()-1, Instance.missingValue());

		instance.setDataset(instances);
		instance.setClassMissing();

		return trainClassifier.classifyInstance(instance);
	}

	// LEARN
	public static void trainResults() throws Exception {
		if (Config.isTrainMode) {
			generateTrainFolder();
			generateDataSet();
			generateClassifier();
		} else {
			
		}
		
		// deserialize model
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getClassifierPath()));
		trainClassifier = (Classifier) ois.readObject();
		ois.close();
	}
	
	private static void generateTrainFolder() throws Exception {
		File trainFolder = new File(TRAIN_FOLDER_PATH);
		trainFolder.mkdir();
		
		File trainFile = new File(trainFolder, TRAIN_DATA_FILE);
		trainFile.createNewFile();

		File classifierFile = new File(trainFolder, TRAIN_CLASSIFIER_FILE);
		classifierFile.createNewFile();
	}

}