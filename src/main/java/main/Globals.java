package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.DocumentsSimilarity;
import entities.FolderEvaluationData;
import entities.FolderInfo;
import entities.JsonProblem;
import features.core.Feature;
import features.list.*;

public class Globals {
	public static List<Feature> Features;
	
	public static Map<String, FolderInfo> IdentificationDocs;
	public static Map<String, List<DocumentsSimilarity>> TrainSimilarities; 

	public static Map<String, FolderEvaluationData> FolderEvaluations;

	public static List<JsonProblem> JsonProblems;
	
	public static void init() {
		IdentificationDocs = new HashMap<String, FolderInfo>();
		TrainSimilarities = new HashMap<String, List<DocumentsSimilarity>>(); 
		FolderEvaluations = new HashMap<String, FolderEvaluationData>();
		
		Features = new ArrayList<Feature>();
		Features.add(new MeanSentenceLength());
		// Features.add(new StopWordsPercentages());
		// Features.add(new PunctuationMarksPercentages());
		Features.add(new UniqueWordsPercentages());
		Features.add(new NounsPercentages());
		Features.add(new AdjectivesPercentages());
		Features.add(new VerbsPercentages());
		
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
