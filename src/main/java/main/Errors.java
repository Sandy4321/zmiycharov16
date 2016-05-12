package main;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.ClusterDocument;
import entities.DocumentsSimilarity;
import entities.JsonProblem;

public class Errors {
	public static double RankingsMAP = 0;
	public static double ClustersFScore = 0;

	public static void calculateError() throws Exception {
		calculateRankingsMAP();
		calculateClustersFScore();
	}

	// Calculate Mean Average Precision for rankings
	private static void calculateRankingsMAP() {
		double result = 0;

		for (JsonProblem problem : Globals.JsonProblems) {
			String folderName = problem.getFolder();

			double resultForFolder = 0;

			List<DocumentsSimilarity> folderRankings = Results.JsonRankings.get(folderName);

			double relevantDocsUntilNow = 0;

			for (int i = 0; i < folderRankings.size(); i++) {
				if (Globals.existsTrainSimilarity(folderName, folderRankings.get(i).getDocument1(),
						folderRankings.get(i).getDocument2())) {
					relevantDocsUntilNow++;

					resultForFolder += relevantDocsUntilNow / (i + 1);
				}
			}

			result += resultForFolder / Globals.TrainSimilarities.get(folderName).size();
		}

		RankingsMAP = result / Globals.JsonProblems.size();
	}

	// Calculate BCubed F Score for clusters
	private static void calculateClustersFScore() throws Exception {
		double totalRelevantDocPairs = 0;
		double totalRetrievedDocPairs = 0;
		double totalJoinedDocPairs = 0;

		Map<String, List<Set<ClusterDocument>>> relevantClusters = getRelevantClusters();
		
		List<String> relevantDocPairs = getDocumentPairs(relevantClusters);
		List<String> retrievedDocPairs = getDocumentPairs(Results.JsonClusters);
		
		totalRelevantDocPairs = relevantDocPairs.size();
		totalRetrievedDocPairs = retrievedDocPairs.size();
		totalJoinedDocPairs = getJoinedDocPairs(relevantDocPairs, retrievedDocPairs);

		double precision = 0;
		double recall = 0;

		if (totalRetrievedDocPairs > 0) {
			precision = totalJoinedDocPairs / totalRetrievedDocPairs;
		}

		if (totalRelevantDocPairs > 0) {
			recall = totalJoinedDocPairs / totalRelevantDocPairs;
		}

		ClustersFScore = 0;

		if (precision + recall > 0) {
			ClustersFScore = (2.0 * precision * recall) / (precision + recall);
		}
	}
	
	private static int getJoinedDocPairs(List<String> relevantDocPairs, List<String> retrievedDocPairs) {
		int result = 0;
		
		for(String pair : relevantDocPairs) {
			if(retrievedDocPairs.contains(pair)) {
				result++;
			}
		}
		
		return result;
	}
	
	private static Map<String, List<Set<ClusterDocument>>> getRelevantClusters() throws Exception {
		Map<String, List<Set<ClusterDocument>>> result = new HashMap<String, List<Set<ClusterDocument>>>();
		
		for(JsonProblem jsonProblem : Globals.JsonProblems) {
			String folderName = jsonProblem.getFolder();

			File truthFile = new File(Config.truthFolderPath + "/" + folderName, "clustering.json");
			
			Type listType = new TypeToken<ArrayList<Set<ClusterDocument>>>() {}.getType();
			ArrayList<Set<ClusterDocument>> folderClusters = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);
			
			result.put(folderName, folderClusters);
		}
		
		return result;
	}

	private static List<String> getDocumentPairs(Map<String, List<Set<ClusterDocument>>> clusters) {
		List<String> result = new ArrayList<String>();

		for (String folderName : clusters.keySet()) {
			for (Set<ClusterDocument> cluster : clusters.get(folderName)) {
				ClusterDocument[] array = cluster.toArray(new ClusterDocument[cluster.size()]);
				
				for (int i = 0; i < array.length; i++) {
					for (int j = i + 1; j < array.length; j++) {
						String doc1 = array[i].getDocument();
						String doc2 = array[j].getDocument();
						
						if(doc1.compareTo(doc2) > 0) {
							result.add(folderName + doc2 + doc1);
						}
						else if (doc1.compareTo(doc2) < 0) {
							result.add(folderName + doc1 + doc2);
						}
					}
				}
			}
		}

		return result;
	}
}
