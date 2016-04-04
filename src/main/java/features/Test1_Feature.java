package features;

import java.util.Random;

public class Test1_Feature extends Feature {
	
	public Test1_Feature() {
		this.setName("Test1 Feature");
	}
	
	@Override
	public double getSimilarity(String text1, String text2) {
		Random generator = new Random();
		double result = generator.nextDouble();
		
		return result;
	}

}
