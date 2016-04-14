package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import featureHelpers.DocumentsSimilarity;
import featureHelpers.Feature;
import features.MeanSentenceLength;
import features.Test1_Feature;
import features.Test2_Feature;
import features.Train_Feature;

public class Globals {
	public static List<Feature> Features;
	
	public static Map<String, List<File>> DocFiles;
	public static Map<String, List<DocumentsSimilarity>> TrainSimilarities; 

	public static Map<String, Double> FeaturesWeights;

	public static Map<String, FolderEvaluationData> FolderEvaluations;

	public static List<JsonProblem> JsonProblems;
	
	public static void init() {
		DocFiles = new HashMap<String, List<File>>();
		TrainSimilarities = new HashMap<String, List<DocumentsSimilarity>>(); 
		FeaturesWeights = new HashMap<String, Double>();
		FolderEvaluations = new HashMap<String, FolderEvaluationData>();
		
		Features = new ArrayList<Feature>();
//		Features.add(new Test1_Feature());
//		Features.add(new Test2_Feature());
		Features.add(new MeanSentenceLength());
		if(Config.isTrainMode) {
			Features.add(new Train_Feature());
		}
	}
	
	public static int getCustomFeaturesCount() {
		int result = Globals.Features.size();
		
		// In train mode last feature is TrainFeature
		if(Config.isTrainMode) {
			result = result -1;
		}

		return result;
	}
	
	public static boolean existsTrainSimilarity(String folderName, String doc1, String doc2) {
		for (DocumentsSimilarity similarity : Globals.TrainSimilarities.get(folderName)) {
			if ((similarity.getDocument1().equals(doc1)
					&& similarity.getDocument2().equals(doc2))
					|| (similarity.getDocument1().equals(doc2)
							&& similarity.getDocument2().equals(doc1))) {
				return true;
			}
		}
		
		return false;
	}
}
