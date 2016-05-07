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
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
		TreeMap<String, LinkedList<Integer>> dist1 = doc1.getPostagDistributions();
		TreeMap<String, LinkedList<Integer>> dist2 = doc2.getPostagDistributions();
		LinkedList<Integer> verbsDistr1 = dist1.get(AbstractPOSTagger.POSTAG_VERB);
		LinkedList<Integer> verbsDistr2 = dist2.get(AbstractPOSTagger.POSTAG_VERB);
		if (verbsDistr2 == null || verbsDistr2.size() == 0) {
			System.out.println();
		}
		Float firstQuartile1 = Distribution.getFirstQuartile(verbsDistr1);
		Float median1 = Distribution.getMedian(verbsDistr1);
		Float thirdQuartile1 = Distribution.getThirdQuartile(verbsDistr1);

		Float firstQuartile2 = Distribution.getFirstQuartile(verbsDistr2);
		Float median2 = Distribution.getMedian(verbsDistr2);
		Float thirdQuartile2 = Distribution.getThirdQuartile(verbsDistr2);

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

			System.out.println("verbs: " + doc1.getFileName() + " <-> " + doc2.getFileName() + ": " + distance);
		}
		return 0.5;
	}
}
