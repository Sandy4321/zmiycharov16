package features;

import java.util.Random;

import featureHelpers.Document;
import featureHelpers.Feature;

public class Test1_Feature extends Feature {
	
	public Test1_Feature() {
		super("Test1 Feature");
	}
	
	@Override
	public double getSimilarity(Document doc1, Document doc2) {
		Random generator = new Random();
		double result = generator.nextDouble()*20;
		
		return result;
	}

}
