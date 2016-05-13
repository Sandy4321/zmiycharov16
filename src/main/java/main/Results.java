package main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import entities.ClusterDocument;
import entities.DocumentsDifference;
import entities.FolderInfo;
import features.core.IdentificationDocument;
import weka.core.Instances;

public class Results {
	public static Map<String, List<DocumentsDifference>> CalculatedRankings = new HashMap<String, List<DocumentsDifference>>();
	public static Map<String, List<DocumentsDifference>> JsonRankings = new HashMap<String, List<DocumentsDifference>>();
	public static Map<String, List<Set<ClusterDocument>>> JsonClusters = new HashMap<String, List<Set<ClusterDocument>>>();

	public static void generateResults(String folderName) throws Exception {
		calculateRankings(folderName);
		generateRankings(folderName);
		generateClusters(folderName);
	}

	private static void calculateRankings(String folderName) throws Exception {
		List<DocumentsDifference> rankings = new ArrayList<DocumentsDifference>();

		FolderInfo folderInfo = Globals.IdentificationDocs.get(folderName);
		Instances instances = Trainer.getTrainInstances(folderInfo.getLanguage(), folderInfo.getGenre());

		for (int i = 0; i < Globals.Features.get(0).getDifferencesForFolder(folderName).size(); i++) {
			DocumentsDifference configDifference = Globals.Features.get(0).getDifferencesForFolder(folderName).get(i);

			DocumentsDifference difference = new DocumentsDifference();
			difference.setDocument1(configDifference.getDocument1());
			difference.setDocument2(configDifference.getDocument2());

			difference.setScore(Trainer.classify(folderName, i, instances));

			rankings.add(difference);
		}

		CalculatedRankings.put(folderName, rankings);
	}

	private static void generateRankings(String folderName) {
		List<DocumentsDifference> rankings = new ArrayList<DocumentsDifference>();

		for (DocumentsDifference difference : CalculatedRankings.get(folderName)) {
			if (difference.getScore() >= Config.MIN_SCORE_TO_RANK) {
				difference.setScore(1.0);
				rankings.add(difference);
			}
		}

		JsonRankings.put(folderName, rankings);
	}

	private static void generateClusters(String folderName) {
		List<Set<String>> clusters = new ArrayList<Set<String>>();

		// Init clusters with all matching couples
		for (int i = 0; i < JsonRankings.get(folderName).size(); i++) {
			DocumentsDifference ranking = JsonRankings.get(folderName).get(i);

			// Create new cluster
			Set<String> cluster = new LinkedHashSet<String>();
			cluster.add(ranking.getDocument1());
			cluster.add(ranking.getDocument2());

			clusters.add(cluster);
		}

		// Generate clusters:
		// Add document X to existing cluster if
		// X is similar to more than the half of the docs in the cluster
		List<IdentificationDocument> folderDocs = Globals.IdentificationDocs.get(folderName).getDocuments();
		for (Set<String> cluster : clusters) {
			for (IdentificationDocument document : folderDocs) {
				if (!cluster.contains(document.getFileName())) {

					int rankedFromClusterCount = 0;
					int minCountToAddToCluster = cluster.size() / 2;

					String[] clusterDocs = cluster.toArray(new String[0]);
					for (String clusterDoc : clusterDocs) {
						if (areDocumentsRanked(clusterDoc, document.getFileName(), folderName)) {
							rankedFromClusterCount++;
						}
					}

					if (rankedFromClusterCount > minCountToAddToCluster) {
						cluster.add(document.getFileName());
					}
				}
			}
		}

		// Find docs which exist in more than one clusters
		// Delete it from the more irrelevant cluster
		for (int i = 0; i < clusters.size() - 1; i++) {
			Set<String> currentCluster = clusters.get(i);
			for (int j = i + 1; j < clusters.size(); j++) {
				Set<String> otherCluster = clusters.get(j);
				
				List<String> docsToRemoveFromCluster1 = new ArrayList<String>();
				List<String> docsToRemoveFromCluster2 = new ArrayList<String>();
				
				for(String cluster1Doc : currentCluster) {
					for(String cluster2Doc : otherCluster) {
						if(cluster1Doc.equals(cluster2Doc)) {
							if(getSimilarDocumentsPercentage(currentCluster, cluster1Doc, folderName)
									> getSimilarDocumentsPercentage(otherCluster, cluster1Doc, folderName)) {
								docsToRemoveFromCluster2.add(cluster1Doc);
							}
							else {
								docsToRemoveFromCluster1.add(cluster1Doc);
							}
						}
					}
				}

				for(String doc : docsToRemoveFromCluster1) {
					currentCluster.remove(doc);
				}

				for(String doc : docsToRemoveFromCluster2) {
					otherCluster.remove(doc);
				}
			}
		}

		// Add single docs
		for (IdentificationDocument doc : Globals.IdentificationDocs.get(folderName).getDocuments()) {
			String document = doc.getFileName();
			if (!isDocumentClustered(document, clusters)) {
				Set<String> cluster = new HashSet<String>();
				cluster.add(document);
				clusters.add(cluster);
			}
		}
		
		// Generate clusters in needed format
		List<Set<ClusterDocument>> jsonClusters = new ArrayList<Set<ClusterDocument>>();
		for (Set<String> cluster : clusters) {
			if(cluster.size() > 0) {
				Set<ClusterDocument> clustersSet = new HashSet<ClusterDocument>();
				for (String document : cluster) {
					clustersSet.add(new ClusterDocument(document));
				}
	
				jsonClusters.add(clustersSet);
			}
		}
		JsonClusters.put(folderName, jsonClusters);
	}

	private static boolean isDocumentClustered(String document, List<Set<String>> clusters) {
		for (Set<String> cluster : clusters) {
			for (String doc : cluster) {
				if (doc.equals(document)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean areDocumentsRanked(String document1, String document2, String folderName) {
		for (DocumentsDifference difference : JsonRankings.get(folderName)) {
			if ((document1.equals(difference.getDocument1()) && document2.equals(difference.getDocument2()))
					|| (document1.equals(difference.getDocument2()) && document2.equals(difference.getDocument1()))) {
				return true;
			}
		}

		return false;
	}
	
	private static double getSimilarDocumentsPercentage(Set<String> cluster, String document, String folderName) {
		double totalCount = cluster.size();
		double similarCount = 0;
		
		for(String clusterDoc : cluster) {
			if(!clusterDoc.equals(document) && areDocumentsRanked(clusterDoc, document, folderName)) {
				similarCount++;
			}
		}
		
		return similarCount / totalCount;
	}

	public static void generateOutput(File outputDir) throws Exception {
		outputDir.mkdir();

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		File ranking = new File(outputDir, "ranking.json");
		ranking.createNewFile();
		FileUtils.write(ranking, gson.toJson(JsonRankings.get(outputDir.getName())));

		File clustering = new File(outputDir, "clustering.json");
		clustering.createNewFile();
		FileUtils.write(clustering, gson.toJson(JsonClusters.get(outputDir.getName())));
	}
}
