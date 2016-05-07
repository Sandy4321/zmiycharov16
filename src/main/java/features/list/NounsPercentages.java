package features.list;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import features.core.IdentificationDocument;
import features.helpers.Distribution;
import features.helpers.DocumentFeaturesHelpers;
import nlp.pos.AbstractPOSTagger;

public class NounsPercentages extends WordsPOSTagPercentages {

	public NounsPercentages() {
		super(POSTAG_TYPE_NOUN);
	} 
	
	@Override
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
		TreeMap<String, LinkedList<Integer>> dist1 = doc1.getPostagDistributions();
		TreeMap<String, LinkedList<Integer>> dist2 = doc2.getPostagDistributions();
		LinkedList<Integer> nounsDistr1 = dist1.get(AbstractPOSTagger.POSTAG_NOUN);
		LinkedList<Integer> nounsDistr2 = dist2.get(AbstractPOSTagger.POSTAG_NOUN);
		Float firstQuartile1 = Distribution.getFirstQuartile(nounsDistr1);
		Float median1 = Distribution.getMedian(nounsDistr1);
		Float thirdQuartile1 = Distribution.getThirdQuartile(nounsDistr1);
		
		Float firstQuartile2 = Distribution.getFirstQuartile(nounsDistr2);
		Float median2 = Distribution.getMedian(nounsDistr2);
		Float thirdQuartile2 = Distribution.getThirdQuartile(nounsDistr2);
		
		//constructing vectors
		Vector v1 = new Vector();
		v1.addElement(firstQuartile1);
		v1.addElement(median1);
		v1.addElement(thirdQuartile1);
		
		Vector v2 = new Vector();
		v2.addElement(firstQuartile2);
		v2.addElement(median2);
		v2.addElement(thirdQuartile2);
		double distance = DocumentFeaturesHelpers.getEuclideanDistance(v1, v2);
		if (doc1.getFileName().equals("document0022.txt") || doc1.getFileName().equals("document0026.txt")
				|| doc1.getFileName().equals("document0045.txt")) {
			System.out.println("Nouns: " + doc1.getFileName() + " <-> " + doc2.getFileName() + ": " + distance);
		}
		return 0.5;
	}
}
