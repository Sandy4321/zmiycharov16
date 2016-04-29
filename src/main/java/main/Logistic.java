package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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

/**
 * Performs simple logistic regression. User: tpeng Date: 6/22/12 Time: 11:01 PM
 * 
 * @author tpeng
 * @author Matthieu Labas
 */
public class Logistic {
	
	private static FastVector wekaAttributes;
	private static Classifier trainClassifier;
	
	private static Instances getTrainInstances() throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(Config.TRAIN_FILE_PATH));
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

	public static Instances readDataSet() {
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

		return trainingSet;
	}

	private static void generateClassifier() throws Exception {
		Instances trainingSet = getTrainInstances();
		LibSVM classifier = new LibSVM();
		classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_EPSILON_SVR, LibSVM.TAGS_SVMTYPE));
		classifier.buildClassifier(trainingSet);

		trainClassifier = classifier;
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
		File trainFile = new File(Config.TRAIN_FILE_PATH);

		Instances trainingSet = null;
		if (Config.isTrainMode) {
			trainingSet = readDataSet();

			trainFile.createNewFile();
			FileUtils.write(trainFile, trainingSet.toString());
		} else {
			
		}
		
		generateClassifier();
	}

}