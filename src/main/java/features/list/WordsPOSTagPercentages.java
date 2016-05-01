package features.list;

import features.core.Feature;
import features.core.IdentificationDocument;

public class WordsPOSTagPercentages extends Feature {
	String postag;
	
	public WordsPOSTagPercentages(String name, String postag) {
		super(name);
		this.postag = postag;
	}

	@Override
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
	
		return 0;
	}

}
