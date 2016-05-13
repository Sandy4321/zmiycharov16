package features.list;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import features.core.IdentificationDocument;
import features.helpers.Distribution;
import features.helpers.DocumentFeaturesHelpers;
import nlp.pos.AbstractPOSTagger;

public class AdjectivesPercentages extends WordsPOSTagPercentages {
	public AdjectivesPercentages() {
		super(POSTAG_TYPE_ADJ);
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		TreeMap<String, LinkedList<Integer>> dist1 = doc1.getPostagDistributions();
		TreeMap<String, LinkedList<Integer>> dist2 = doc2.getPostagDistributions();
		LinkedList<Integer> adjDistr1 = dist1.get(AbstractPOSTagger.POSTAG_ADJECTIVE);
		LinkedList<Integer> adjDistr2 = dist2.get(AbstractPOSTagger.POSTAG_ADJECTIVE);
		
		if (adjDistr1 == null || adjDistr1.size() == 0 || adjDistr2 == null || adjDistr2.size() == 0) {
			return 10.0;
		}
		Float minValue1 = (float) adjDistr1.get(0);
		Float firstQuartile1 = Distribution.getFirstQuartile(adjDistr1);
		Float median1 = Distribution.getMedian(adjDistr1);
		Float thirdQuartile1 = Distribution.getThirdQuartile(adjDistr1);
		Float maxValue1 = (float) adjDistr1.getLast();
		
		// constructing vector
		Vector<Float> v1 = new Vector<Float>();
		
		v1.addElement(minValue1);
		v1.addElement(firstQuartile1);
		v1.addElement(median1);
		v1.addElement(thirdQuartile1);
		v1.addElement(maxValue1);
		
		
		Float minValue2 = (float) adjDistr2.get(0);
		Float firstQuartile2 = Distribution.getFirstQuartile(adjDistr2);
		Float median2 = Distribution.getMedian(adjDistr2);
		Float thirdQuartile2 = Distribution.getThirdQuartile(adjDistr2);
		Float maxValue2 = (float) adjDistr2.getLast();
		
		// constructing vector
		Vector<Float> v2 = new Vector<Float>();
		v2.addElement(minValue2);
		v2.addElement(firstQuartile2);
		v2.addElement(median2);
		v2.addElement(thirdQuartile2);
		v2.addElement(maxValue2);
		
		double distance = DocumentFeaturesHelpers.getEuclideanDistance(v1, v2);
		return distance;
	}
}
