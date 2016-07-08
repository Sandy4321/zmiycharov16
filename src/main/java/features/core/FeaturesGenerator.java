package features.core;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.DocumentsDifference;
import entities.FolderEvaluationData;
import entities.FolderInfo;
import entities.JsonProblem;
import features.helpers.*;
import main.Config;
import main.Globals;
import main.Utils;

public class FeaturesGenerator {

	// GENERATE DOC FILES
	public static void generateIdentificationDocs(File parentFolder, JsonProblem problem) throws Exception {
		String folderName = problem.getFolder();
		
		FolderInfo folderInfo = new FolderInfo();
		folderInfo.setLanguage(problem.getLanguage());
		folderInfo.setGenre(problem.getGenre());
		folderInfo.setDocuments(getIdentificationDocs(new File(parentFolder, folderName), problem.getLanguage(), problem.getGenre()));
		
		Globals.IdentificationDocs.put(folderName, folderInfo);
	}

	private static List<IdentificationDocument> getIdentificationDocs(File docsDir, String language, String genre) throws Exception {
		
		List<IdentificationDocument> result = new ArrayList<IdentificationDocument>();
		
		for(File file : docsDir.listFiles()) {
			if (file.isFile()) {
				result.add(new IdentificationDocument(file, language, genre));
			}
		}
		
		return result;
	}
	
	// GENERATE FEATURES DIFFERENCE
	public static void generateFeaturesDifferences(File parentFolder, String folderName) throws Exception {
		setFeaturesDifferences(folderName);
		
		if(Config.ARE_RESULTS_NORMALIZED) {
			normalizeFeaturesDifferences();
		}
	}

	private static void setFeaturesDifferences(String folderName) throws Exception {
		FolderInfo folderInfo = Globals.IdentificationDocs.get(folderName);
		List<IdentificationDocument> docs = folderInfo.getDocuments();
		for (Feature feature : Globals.Features) {
		    List<DocumentsDifference> differences = new ArrayList<DocumentsDifference>();
		    for(int i = 0; i < docs.size()-1;i++) {
		    	IdentificationDocument doc1 = docs.get(i);
		    	for(int j = i+1; j < docs.size();j++) {
		    		IdentificationDocument doc2 = docs.get(j);
			    	
			    	DocumentsDifference difference = new DocumentsDifference();
			    	difference.setDocument1(doc1.getFileName());
			    	difference.setDocument2(doc2.getFileName());
			    	difference.setScore(feature.getDifference(
		    				doc1, doc2
		    			));
			    	
			    	differences.add(difference);
			    }
		    }

	    	feature.setDifferences(folderName, differences);
		}
	}

	private static void normalizeFeaturesDifferences() {
		for (Feature feature : Globals.Features) {
			feature.normalizeDifferences();
		}
	}
	
	// ACTUAL DIFFERENCES
	public static void setActualDifferences(String folderName) throws Exception {
		File truthFile = new File(Config.truthFolderPath + "/" + folderName, "ranking.json");
		
		Type listType = new TypeToken<ArrayList<DocumentsDifference>>() {}.getType();
		List<DocumentsDifference> result = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);

		Globals.TrainSimilarities.put(folderName, result);
		
		FolderEvaluationData evaluations = new FolderEvaluationData();
		evaluations.trainEvaluatedCouplesCount = result.size();
		evaluations.totalDocCouplesCount = Utils.calculateCouplesCountFromTotal(Globals.IdentificationDocs.get(folderName).getDocuments().size());
		
		evaluations.multiplyNumberForDocument = evaluations.totalDocCouplesCount / evaluations.trainEvaluatedCouplesCount; 

		Globals.FolderEvaluations.put(folderName, evaluations);
	}
}
