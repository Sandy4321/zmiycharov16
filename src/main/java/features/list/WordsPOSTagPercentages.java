package features.list;

import features.core.Feature;

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
public abstract class WordsPOSTagPercentages extends Feature {
	public static final String POSTAG_TYPE_NOUN = "PostagNoun";
	public static final String POSTAG_TYPE_VERB = "PostagVerb";
	public static final String POSTAG_TYPE_ADJ = "PostagAdjective";
	public static final String POSTAG_TYPE_CONJ = "PostagConjunction";
	

	protected String postagType;

	public WordsPOSTagPercentages(String postag) {
		super(postag);
		this.postagType = postag;
		if (!POSTAG_TYPE_NOUN.equals(postag) && !POSTAG_TYPE_VERB.equals(postag) && !POSTAG_TYPE_ADJ.equals(postag)) {
			throw new IllegalArgumentException(postag + "type is not implemented. The default behavior will be used.");
		}
	}
}
