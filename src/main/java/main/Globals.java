package main;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import featureHelpers.DocumentsSimilarity;
import featureHelpers.Feature;
import features.Test1_Feature;
import features.Test2_Feature;

public class Globals {
	public static List<Feature> Features;
	
	public static Map<String, List<File>> DocFiles;
	public static Map<String, List<DocumentsSimilarity>> TrainSimilarities; 

	public static Map<String, Double> FeaturesWeights;
	
	public static void init() {
		DocFiles = new HashMap<String, List<File>>();
		TrainSimilarities = new HashMap<String, List<DocumentsSimilarity>>(); 
		FeaturesWeights = new HashMap<String, Double>();
		
		Features = Arrays.asList(
				new Test1_Feature()
				, new Test2_Feature()
			);
	}
}
