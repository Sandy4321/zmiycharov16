import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import features.*;

public class Results {
	public static List<DocumentsSimilarity> CalculatedRankings;
	public static List<DocumentsSimilarity> JsonRankings;
	public static List<List<ClusterDocument>> JsonClusters;

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
		
		for(DocumentsSimilarity similarity : CalculatedRankings) {
			if(similarity.getScore() >= Config.MIN_SCORE_TO_RANK) {
				JsonRankings.add(similarity);
			}
		}
	}

	private static void generateClusters() {
		JsonClusters = new ArrayList<List<ClusterDocument>>();
	}
}
