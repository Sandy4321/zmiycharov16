package main;
import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import features.*;

public class FeaturesGenerator {
	
	public static List<DocumentsSimilarity> getActualSimilarities(String folderName) throws Exception {
		File truthFile = new File(Config.truthFolderPath + "/" + folderName, "ranking.json");
		
		Type listType = new TypeToken<ArrayList<DocumentsSimilarity>>() {}.getType();
		List<DocumentsSimilarity> result = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);

		return result;
	}

	public static void generateFeaturesSimilarities(String folderName) throws Exception {
		Globals.DocFiles = getDocFiles(new File(Config.inputFolderPath, folderName));
		
		setFeaturesSimilarities(Globals.DocFiles);
		
		normalizeFeaturesSimilarities();
	}

	private static List<File> getDocFiles(File docsDir) {
		
		List<File> result = new ArrayList<File>();
		
		for(File file : docsDir.listFiles()) {
			result.add(file);
		}
		
		return result;
	}
	
	private static void setFeaturesSimilarities(List<File> docFiles) throws Exception {
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

	    	feature.setSimilarities(similarities);
		}
	}

	private static void normalizeFeaturesSimilarities() {
		for (Feature feature : Globals.Features) {
			feature.normalizeSimilarities();
		}
	}

}
