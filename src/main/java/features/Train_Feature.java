package features;

import java.util.Random;

import featureHelpers.Document;
import featureHelpers.Feature;

public class Train_Feature extends Feature {

	public Train_Feature() {
		super();
		this.setName("Test2 Feature");
	}
	
	@Override
	public double getSimilarity(Document doc1, Document doc2) {
		Random generator = new Random();
		double result = generator.nextDouble()*50;
		
		return result;
	}

}
