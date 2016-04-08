package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import features.*;

public class FeaturesGenerator {
	
	// GENERATE FEATURES SIMILARITY
	public static void generateFeaturesSimilarities(File parentFolder, String folderName) throws Exception {
		Globals.DocFiles.put(folderName, getDocFiles(new File(parentFolder, folderName)));
		
		setFeaturesSimilarities(folderName);
		
		normalizeFeaturesSimilarities();
	}

	private static List<File> getDocFiles(File docsDir) {
		
		List<File> result = new ArrayList<File>();
		
		for(File file : docsDir.listFiles()) {
			result.add(file);
		}
		
		return result;
	}
	
	private static void setFeaturesSimilarities(String folderName) throws Exception {
		List<File> docFiles = Globals.DocFiles.get(folderName);
		for (Feature feature : Globals.Features) {
		    List<DocumentsSimilarity> similarities = new ArrayList<DocumentsSimilarity>();
		    for(int i = 0; i < docFiles.size()-1;i++) {
		    	File file1 = docFiles.get(i);
		    	for(int j = i+1; j < docFiles.size();j++) {
			    	File file2 = docFiles.get(j);
			    	
			    	Document doc1 = new Document(file1);
			    	Document doc2 = new Document(file2);
			    	
			    	DocumentsSimilarity similarity = new DocumentsSimilarity();
			    	similarity.setDocument1(doc1.getFileName());
			    	similarity.setDocument2(doc2.getFileName());
			    	similarity.setScore(feature.getSimilarity(
		    				doc1, doc2
		    			));
			    	
			    	similarities.add(similarity);
			    }
		    }

	    	feature.setSimilarities(folderName, similarities);
		}
	}

	private static void normalizeFeaturesSimilarities() {
		for (Feature feature : Globals.Features) {
			feature.normalizeSimilarities();
		}
	}
	
	public static double getCalculatedSimilarity(String folderName, String document1, String document2, Feature feature) {
		for(DocumentsSimilarity similarity : feature.getSimilaritiesForFolder(folderName)) {
			if(
					(similarity.getDocument1().equals(document1) && similarity.getDocument2().equals(document2))
					|| (similarity.getDocument1().equals(document2) && similarity.getDocument2().equals(document1))
				) {
				return similarity.getScore();
			}
		}
		
		return 0;
	}

	// ACTUAL SIMILARITIES
	public static void setActualSimilarities(String folderName) throws Exception {
		File truthFile = new File(Config.truthFolderPath + "/" + folderName, "ranking.json");
		
		Type listType = new TypeToken<ArrayList<DocumentsSimilarity>>() {}.getType();
		List<DocumentsSimilarity> result = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);

		Globals.TrainSimilarities.put(folderName, result);
	}
	
	public static double getActualSimilarity(String folderName, String document1, String document2) {
		for(DocumentsSimilarity similarity : Globals.TrainSimilarities.get(folderName)) {
			if(
					(similarity.getDocument1().equals(document1) && similarity.getDocument2().equals(document2))
					|| (similarity.getDocument1().equals(document2) && similarity.getDocument2().equals(document1))
				) {
				return similarity.getScore();
			}
		}
		
		return 0;
	}
}
