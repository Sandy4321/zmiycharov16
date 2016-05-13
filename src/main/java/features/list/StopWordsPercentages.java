package features.list;

import java.util.Map;

import features.core.Feature;
import features.core.IdentificationDocument;
import nlp.stopwords.StopWordItem;

public class StopWordsPercentages extends Feature {

	public StopWordsPercentages() {
		super("StopWordsPercentagesFeature");
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		Map<String, StopWordItem> doc1Map = doc1.getStopWordsMap();
		Map<String, StopWordItem> doc2Map = doc2.getStopWordsMap();

		double result = 0.0;

		for(String key : doc1Map.keySet()) {
			result += Math.abs(doc1Map.get(key).getPercent() - doc2Map.get(key).getPercent());
		}

		return result;
	}
}
