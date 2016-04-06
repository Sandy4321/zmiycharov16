package features;

import java.util.Random;

public class Test2_Feature extends Feature {

	public Test2_Feature() {
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
