package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.DocumentsDifference;
import entities.FolderEvaluationData;
import entities.FolderInfo;
import entities.JsonProblem;
import features.core.Feature;
import features.list.*;

public class Globals {
	public static List<Feature> Features;
	
	public static Map<String, FolderInfo> IdentificationDocs;
	public static Map<String, List<DocumentsDifference>> TrainDifferences; 

	public static Map<String, FolderEvaluationData> FolderEvaluations;

	public static List<JsonProblem> JsonProblems;
	
	public static void init() {
		IdentificationDocs = new HashMap<String, FolderInfo>();
		TrainDifferences = new HashMap<String, List<DocumentsDifference>>(); 
		FolderEvaluations = new HashMap<String, FolderEvaluationData>();
		
		Features = new ArrayList<Feature>();
		Features.add(new MeanSentenceLength());
		Features.add(new StopWordsPercentages());
		// Features.add(new PunctuationMarksPercentages());
		Features.add(new UniqueWordsPercentages());
		Features.add(new NounsPercentages());
		Features.add(new AdjectivesPercentages());
		Features.add(new VerbsPercentages());
		Features.add(new ConjunctionPercentages());
		
		if(Config.isTrainMode) {
			Features.add(new Train_Feature());
		}
	}
	
	public static boolean existsTrainDifference(String folderName, String doc1, String doc2) {
		for (DocumentsDifference difference : Globals.TrainDifferences.get(folderName)) {
			if ((difference.getDocument1().equals(doc1)
					&& difference.getDocument2().equals(doc2))
					|| (difference.getDocument1().equals(doc2)
							&& difference.getDocument2().equals(doc1))) {
				return true;
			}
		}
		
		return false;
	}
}
