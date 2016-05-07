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
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
		TreeMap<String, LinkedList<Integer>> dist1 = doc1.getPostagDistributions();
		TreeMap<String, LinkedList<Integer>> dist2 = doc2.getPostagDistributions();
		LinkedList<Integer> adjDistr1 = dist1.get(AbstractPOSTagger.POSTAG_ADJECTIVE);
		LinkedList<Integer> adjDistr2 = dist2.get(AbstractPOSTagger.POSTAG_ADJECTIVE);
		Float firstQuartile1 = Distribution.getFirstQuartile(adjDistr1);
		Float median1 = Distribution.getMedian(adjDistr1);
		Float thirdQuartile1 = Distribution.getThirdQuartile(adjDistr1);

		Float firstQuartile2 = Distribution.getFirstQuartile(adjDistr2);
		Float median2 = Distribution.getMedian(adjDistr2);
		Float thirdQuartile2 = Distribution.getThirdQuartile(adjDistr2);

		// constructing vectors
		Vector<Float> v1 = new Vector<Float>();
		v1.addElement(firstQuartile1);
		v1.addElement(median1);
		v1.addElement(thirdQuartile1);

		Vector<Float> v2 = new Vector<Float>();
		v2.addElement(firstQuartile2);
		v2.addElement(median2);
		v2.addElement(thirdQuartile2);
		double distance = DocumentFeaturesHelpers.getEuclideanDistance(v1, v2);

		if (doc1.getFileName().equals("document0022.txt") || doc1.getFileName().equals("document0026.txt")
				|| doc1.getFileName().equals("document0045.txt")) {
			System.out.println("adjectivesL " + doc1.getFileName() + " <-> " + doc2.getFileName() + ": " + distance);
		}
		return 0.5;
	}
}
