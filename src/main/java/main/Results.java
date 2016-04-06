package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import features.*;

public class Results {
	public static List<DocumentsSimilarity> CalculatedRankings;
	public static List<DocumentsSimilarity> JsonRankings;
	public static List<Set<ClusterDocument>> JsonClusters;

	public static void generateResults() {
		calculateRankings();
		generateRankings();
		generateClusters();
	}

	private static void calculateRankings() {
		CalculatedRankings = new ArrayList<DocumentsSimilarity>();

		for (int i = 0; i < Config.Features.get(0).getSimilarities().size(); i++) {
			DocumentsSimilarity configSimilarity = Config.Features.get(0).getSimilarities().get(i);

			DocumentsSimilarity similarity = new DocumentsSimilarity();
			similarity.setDocument1(configSimilarity.getDocument1());
			similarity.setDocument2(configSimilarity.getDocument2());

			CalculatedRankings.add(similarity);
		}

		for (int i = 0; i < CalculatedRankings.size(); i++) {
			DocumentsSimilarity similarity = CalculatedRankings.get(i);

			double score = 0;
			for (Feature feature : Config.Features) {
				score += feature.getWeight() * feature.getSimilarities().get(i).getScore();
			}

			similarity.setScore(score);
		}
	}

	private static void generateRankings() {
		JsonRankings = new ArrayList<DocumentsSimilarity>();

		for (DocumentsSimilarity similarity : CalculatedRankings) {
			if (similarity.getScore() >= Config.MIN_SCORE_TO_RANK) {
				JsonRankings.add(similarity);
			}
		}
	}

	private static void generateClusters() {
		JsonClusters = new ArrayList<Set<ClusterDocument>>();

		for (int i = 0; i < JsonRankings.size(); i++) {
			DocumentsSimilarity ranking = JsonRankings.get(i);

			// Skip if ranking is clustered
			if (isDocumentClustered(ranking.getDocument1())) {
				continue;
			}

			// Create new cluster
			Set<ClusterDocument> cluster = new HashSet<ClusterDocument>();
			cluster.add(new ClusterDocument(ranking.getDocument1()));
			cluster.add(new ClusterDocument(ranking.getDocument2()));

			for (int j = i + 1; j < JsonRankings.size(); j++) {
				DocumentsSimilarity nextRanking = JsonRankings.get(j);
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

			JsonClusters.add(cluster);
		}

		// Add single docs
		for (File file : Config.DocFiles) {
			String document = file.getName();
			if (!isDocumentClustered(document)) {
				Set<ClusterDocument> cluster = new HashSet<ClusterDocument>();
				cluster.add(new ClusterDocument(document));
				JsonClusters.add(cluster);
			}
		}
	}

	private static boolean isDocumentClustered(String document) {
		for (Set<ClusterDocument> cluster : JsonClusters) {
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
}
