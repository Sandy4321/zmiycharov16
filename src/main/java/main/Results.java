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

import featureHelpers.*;

public class Results {
	public static Map<String, List<DocumentsSimilarity>> CalculatedRankings = new HashMap<String, List<DocumentsSimilarity>>();
	public static Map<String, List<DocumentsSimilarity>> JsonRankings = new HashMap<String, List<DocumentsSimilarity>>();
	public static Map<String, List<Set<ClusterDocument>>> JsonClusters = new HashMap<String, List<Set<ClusterDocument>>>();

	public static void generateResults(String folderName) {
		calculateRankings(folderName);
		generateRankings(folderName);
		generateClusters(folderName);
	}

	private static void calculateRankings(String folderName) {
		List<DocumentsSimilarity> rankings = new ArrayList<DocumentsSimilarity>();

		for (int i = 0; i < Globals.Features.get(0).getSimilaritiesForFolder(folderName).size(); i++) {
			DocumentsSimilarity configSimilarity = Globals.Features.get(0).getSimilaritiesForFolder(folderName).get(i);

			DocumentsSimilarity similarity = new DocumentsSimilarity();
			similarity.setDocument1(configSimilarity.getDocument1());
			similarity.setDocument2(configSimilarity.getDocument2());

			similarity.setScore(Logistic.classify(folderName, i));

			rankings.add(similarity);
		}

		CalculatedRankings.put(folderName, rankings);
	}

	private static void generateRankings(String folderName) {
		List<DocumentsSimilarity> rankings = new ArrayList<DocumentsSimilarity>();

		for (DocumentsSimilarity similarity : CalculatedRankings.get(folderName)) {
			if (similarity.getScore() >= Config.MIN_SCORE_TO_RANK) {
				rankings.add(similarity);
			}
		}

		JsonRankings.put(folderName, rankings);
	}

	private static void generateClusters(String folderName) {
		List<Set<ClusterDocument>> clusters = new ArrayList<Set<ClusterDocument>>();

		for (int i = 0; i < JsonRankings.get(folderName).size(); i++) {
			DocumentsSimilarity ranking = JsonRankings.get(folderName).get(i);

			// Skip if ranking is clustered
			if (isDocumentClustered(ranking.getDocument1(), clusters)) {
				continue;
			}

			// Create new cluster
			Set<ClusterDocument> cluster = new LinkedHashSet<ClusterDocument>();
			cluster.add(new ClusterDocument(ranking.getDocument1()));
			cluster.add(new ClusterDocument(ranking.getDocument2()));

			for (int j = i + 1; j < JsonRankings.get(folderName).size(); j++) {
				DocumentsSimilarity nextRanking = JsonRankings.get(folderName).get(j);
				String doc1 = nextRanking.getDocument1();
				String doc2 = nextRanking.getDocument2();

				if (isDocumentContainedInCluster(cluster, doc1) && !isDocumentContainedInCluster(cluster, doc2)) {
					cluster.add(new ClusterDocument(doc2));
					j = i;
				}

				if (!isDocumentContainedInCluster(cluster, doc1) && isDocumentContainedInCluster(cluster, doc2)) {
					cluster.add(new ClusterDocument(doc1));
					j = i;
				}
			}

			clusters.add(cluster);
		}

		// Add single docs
		for (IdentificationDocument doc : Globals.DocFiles.get(folderName).getDocuments()) {
			String document = doc.getFileName();
			if (!isDocumentClustered(document, clusters)) {
				Set<ClusterDocument> cluster = new HashSet<ClusterDocument>();
				cluster.add(new ClusterDocument(document));
				clusters.add(cluster);
			}
		}

		JsonClusters.put(folderName, clusters);
	}

	private static boolean isDocumentClustered(String document, List<Set<ClusterDocument>> clusters) {
		for (Set<ClusterDocument> cluster : clusters) {
			for (ClusterDocument doc : cluster) {
				if (doc.getContent().equals(document)) {
					return true;
				}
			}
		}

		return false;
	}

	private static boolean isDocumentContainedInCluster(Set<ClusterDocument> cluster, String document) {
		for (ClusterDocument doc : cluster) {
			if (doc.getContent().equals(document)) {
				return true;
			}
		}

		return false;
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
