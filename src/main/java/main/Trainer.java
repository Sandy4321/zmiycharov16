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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import entities.FolderInfo;
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

	private static String getTrainDataPath(String language, String genre) {
		return TRAIN_FOLDER_PATH + language + "/" + genre + "/" + TRAIN_DATA_FILE;
	}
	
	private static String getClassifierPath(String language, String genre) {
		return TRAIN_FOLDER_PATH + language + "/" + genre + "/" + TRAIN_CLASSIFIER_FILE;
	}
	
	private static Map<String, List<String>> getFoldersTree() {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		
		for(String key : Globals.IdentificationDocs.keySet()) {
			FolderInfo folderInfo = Globals.IdentificationDocs.get(key);
			
			if(!result.containsKey(folderInfo.getLanguage())) {
				result.put(folderInfo.getLanguage(), new ArrayList<String>());
			}
			if(!result.get(folderInfo.getLanguage()).contains(folderInfo.getGenre())) {
				List<String> newList = result.get(folderInfo.getLanguage());
				newList.add(folderInfo.getGenre());
				result.put(folderInfo.getLanguage(), newList);
			}
		}
		
		return result;
	}

	private static void generateTrainFolder(String language, String genre) throws Exception {
		File trainFolder = new File(TRAIN_FOLDER_PATH);
		trainFolder.mkdir();
		
		File langFolder = new File(trainFolder, language);
		langFolder.mkdir();
		
		File genreFolder = new File(langFolder, genre);
		genreFolder.mkdir();

		File trainFile = new File(genreFolder, TRAIN_DATA_FILE);
		trainFile.createNewFile();

		File classifierFile = new File(genreFolder, TRAIN_CLASSIFIER_FILE);
		classifierFile.createNewFile();
	}
	
	private static List<String> getFoldersForCriteria(String language, String genre) {
		List<String> result = new ArrayList<String>();
		
		for (String folder : Globals.IdentificationDocs.keySet()) {
			if(language.equals(Globals.IdentificationDocs.get(folder).getLanguage())
					&& genre.equals(Globals.IdentificationDocs.get(folder).getGenre())) {
				result.add(folder);
			}
		}
		
		return result;
	}
	
	private static FastVector wekaAttributes;
	private static Map<String, Map<String, Classifier>> trainClassifiersMap;
	
	public static Instances getTrainInstances(String language, String genre) throws Exception {
		BufferedReader reader = new BufferedReader(new FileReader(getTrainDataPath(language, genre)));
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

	public static void generateDataSet(String language, String genre) throws Exception {
		Feature lastFeature = Globals.Features.get(Globals.Features.size() - 1);
		int totalCount = 0;
		for (String folder : Globals.IdentificationDocs.keySet()) {
			totalCount += lastFeature.getDifferencesForFolder(folder).size();
		}

		// Create an empty training set
		Instances trainingSet = new Instances("Train", wekaAttributes, totalCount);
		// Set class index
		trainingSet.setClassIndex(Globals.Features.size() - 1);
		
		List<String> folders = getFoldersForCriteria(language, genre);

		for (String folder : folders) {
			for (int siimilarityIndex = 0; siimilarityIndex < lastFeature.getDifferencesForFolder(folder)
					.size(); siimilarityIndex++) {

				// Create the instance
				Instance instance = new Instance(Globals.Features.size());

				for (int featureIndex = 0; featureIndex < Globals.Features.size()-1; featureIndex++) {
					double difference = Globals.Features.get(featureIndex).getDifferencesForFolder(folder)
							.get(siimilarityIndex).getScore();

					instance.setValue((Attribute) wekaAttributes.elementAt(featureIndex), difference);

				}
				
				double lastDifference = lastFeature.getDifferencesForFolder(folder)
						.get(siimilarityIndex).getScore();

				instance.setValue((Attribute) wekaAttributes.elementAt(Globals.Features.size()-1), lastDifference);
				
				// Multiply positive to match negative
				if(lastDifference == 1) {
					int multiplyTimes = Globals.FolderEvaluations.get(folder).multiplyNumberForDocument;
					for (int i = 0; i < multiplyTimes; i++) {
						trainingSet.add(instance);
					}
				}

				// add the instance
				trainingSet.add(instance);
			}
		}

		FileUtils.write(new File(getTrainDataPath(language, genre)), trainingSet.toString());
	}

	private static void generateClassifier(String language, String genre) throws Exception {
		Instances trainingSet = getTrainInstances(language, genre);
		LibSVM classifier = new LibSVM();
		classifier.setSVMType(new SelectedTag(LibSVM.SVMTYPE_EPSILON_SVR, LibSVM.TAGS_SVMTYPE));
		classifier.buildClassifier(trainingSet);
		
		weka.core.SerializationHelper.write(getClassifierPath(language, genre), classifier);
	}

	public static double classify(String folderName, int differenceIndex, Instances instances) throws Exception {

		FolderInfo folderInfo = Globals.IdentificationDocs.get(folderName);
		

		// Create the instance
		Instance instance = new Instance(Globals.Features.size());
		
		for (int featureIndex = 0; featureIndex < Globals.Features.size()-1; featureIndex++) {
			double difference = Globals.Features.get(featureIndex).getDifferencesForFolder(folderName)
					.get(differenceIndex).getScore();

			instance.setValue(featureIndex, difference);
		}
		
		instance.setValue(Globals.Features.size()-1, Instance.missingValue());

		instance.setDataset(instances);
		instance.setClassMissing();
		
		return trainClassifiersMap.get(folderInfo.getLanguage()).get(folderInfo.getGenre()).classifyInstance(instance);
	}

	// LEARN
	public static void trainResults() throws Exception {
		Map<String, List<String>> foldersTree = getFoldersTree();
		
		trainClassifiersMap = new HashMap<String, Map<String, Classifier>>();

		for(String language : foldersTree.keySet()) {
			trainClassifiersMap.put(language, new HashMap<String, Classifier>());
			
			for(String genre : foldersTree.get(language)) {
				Date start = new Date();
				
				if (Config.isTrainMode) {
					System.out.println("Generate train folder");
					generateTrainFolder(language, genre);
					generateDataSet(language, genre);
					System.out.println("Generate classifier");
					generateClassifier(language, genre);
				}
				
				// Always deserialize model
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(getClassifierPath(language, genre)));
				
				Map<String, Classifier> newMap = trainClassifiersMap.get(language);
				newMap.put(genre, (Classifier) ois.readObject());
				
				trainClassifiersMap.put(language, newMap);
				ois.close();

				Date now = new Date();
				
				long total = now.getTime() - start.getTime();
				
				long seconds = total/1000;
				long millis = total%1000;
				
				System.out.println("Train: " + language + " / " + genre + " (" + seconds + "." + String.format("%03d", millis) + " sec)");
			}
		}
		
	}
}