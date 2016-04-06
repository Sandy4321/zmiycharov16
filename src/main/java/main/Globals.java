package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import features.DocumentsSimilarity;
import features.Feature;
import features.Test1_Feature;
import features.Test2_Feature;

public class Globals {
	public static List<Feature> Features = Arrays.asList(
		new Test1_Feature()
		, new Test2_Feature()
	);
	
	public static Map<String, List<File>> DocFiles = new HashMap<String, List<File>>();
	public static Map<String, List<DocumentsSimilarity>> TrainSimilarities = new HashMap<String, List<DocumentsSimilarity>>(); 
}
