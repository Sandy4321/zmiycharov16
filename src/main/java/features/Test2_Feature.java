package features;

import java.util.Random;

public class Test2_Feature extends Feature {

	public Test2_Feature() {
		this.setName("Test2 Feature");
	}
	
	@Override
	public double getSimilarity(String text1, String text2) {
		Random generator = new Random();
		double result = generator.nextDouble()*50;
		
		return result;
	}

}
