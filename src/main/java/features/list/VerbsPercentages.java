package features.list;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import features.core.IdentificationDocument;
import features.helpers.Distribution;
import features.helpers.DocumentFeaturesHelpers;
import nlp.pos.AbstractPOSTagger;

public class VerbsPercentages extends WordsPOSTagPercentages {
	public VerbsPercentages() {
		super(POSTAG_TYPE_VERB);
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		TreeMap<String, LinkedList<Integer>> dist1 = doc1.getPostagDistributions();
		TreeMap<String, LinkedList<Integer>> dist2 = doc2.getPostagDistributions();
		LinkedList<Integer> verbsDistr1 = dist1.get(AbstractPOSTagger.POSTAG_VERB);
		LinkedList<Integer> verbsDistr2 = dist2.get(AbstractPOSTagger.POSTAG_VERB);
		
		if (verbsDistr1 == null || verbsDistr1.size() == 0 || verbsDistr2 == null || verbsDistr2.size() == 0) {
			return 10.0;
		}
		Float minValue1 = (float) verbsDistr1.get(0);
		Float firstQuartile1 = Distribution.getFirstQuartile(verbsDistr1);
		Float median1 = Distribution.getMedian(verbsDistr1);
		Float thirdQuartile1 = Distribution.getThirdQuartile(verbsDistr1);
		Float maxValue1 = (float) verbsDistr1.getLast();
		
		// constructing vector
		Vector<Float> v1 = new Vector<Float>();
		v1.addElement(minValue1);
		v1.addElement(firstQuartile1);
		v1.addElement(median1);
		v1.addElement(thirdQuartile1);
		v1.addElement(maxValue1);
		
		Float minValue2 = (float) verbsDistr1.get(0);
		Float firstQuartile2 = Distribution.getFirstQuartile(verbsDistr2);
		Float median2 = Distribution.getMedian(verbsDistr2);
		Float thirdQuartile2 = Distribution.getThirdQuartile(verbsDistr2);
		Float maxValue2 = (float) verbsDistr2.getLast();
		
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
