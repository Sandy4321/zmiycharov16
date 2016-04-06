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
	}
	
	private static void generateRankings() {
		JsonRankings = new ArrayList<DocumentsSimilarity>();
	}
	
	private static void generateClusters() {
		JsonClusters = new ArrayList<List<ClusterDocument>>();
	}
}
