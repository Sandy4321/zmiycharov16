package features.list;

import java.util.LinkedList;
import java.util.TreeMap;
import java.util.Vector;

import features.core.Feature;
import features.core.IdentificationDocument;
import features.helpers.Distribution;
import features.helpers.DocumentFeaturesHelpers;

/**
 * This class is used to provide similarity measure between two instances of
 * {@link features.core.IdentificationDocument IdentificationDocument} by using
 * already generated statistical distributions. These distributions contains
 * information about all POS tags that appears in the document, and the number
 * of all occurrrences in any sentence of the document.
 * <p>
 * The similarity is measured by using Euclidean distance between the first,
 * second and the third quartiles of the two documents.
 * 
 */
public class WordsPOSTagPercentages extends Feature {
	public static final String POSTAG_TYPE_NOUN = "PostagNoun";
	public static final String POSTAG_TYPE_VERB = "PostagVerb";
	public static final String POSTAG_TYPE_ADJ = "PostagAdjective";
	public static final String POSTAG_TYPE_CONJ = "PostagAdjective";
	

	private String postagType;

	public WordsPOSTagPercentages(String postag) {
		super(postag);
		this.postagType = postag;
		if (!POSTAG_TYPE_NOUN.equals(postag) && !POSTAG_TYPE_VERB.equals(postag) && !POSTAG_TYPE_ADJ.equals(postag)) {
			throw new IllegalArgumentException(postag + "type is not implemented. The default behavior will be used.");
		}
	}

	@Override
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {

		return 0.5;
	}

	private double getSimililarityForPOSTag(String type, IdentificationDocument doc1, IdentificationDocument doc2) {
		
//		TreeMap<String, LinkedList<Integer>> posModel1 = doc1.getPostagDistributions();
//		TreeMap<String, LinkedList<Integer>> posModel2 = doc2.getPostagDistributions();
//		float posModel1FirstQuartile = Distribution.getFirstQuartile(rankedDataSet);
//		Vector<Float> first = new Vector<Float>();
//		first.add(doc1.get)
//		Vector<Float> second = new Vector<Float>();
//		
//		double distance = DocumentFeaturesHelpers.getEuclideanDistance(first, second);
		
		return 0.5;
	}
	
//	/**
//	 *  Returns the real postag id as it`s persisted in the file, which holds the distributions
//	 *  of the document. This method should be removed in future and all postag should be with
//	 *  the same same.
//	 *  
//	 * @param postagType
//	 * @param language
//	 * @return
//	 */
//	private String getPOSTagNameByPOSTagType(String postagType, String language) {
//		if (POSTAG_TYPE_ADJ.equals(postagType)) {
//			
//		} else if (POSTAG_TYPE_NOUN.equals(postagType)) {
//			
//		} else if (POSTAG_TYPE_VERB.equals(postagType)) {
//			
//		}
//		return null;
//	}

}
