package features;

public abstract class Feature {
	private String name;
	// Between 0 and 1
	private double weight;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return weight;
	}
	public void setWeight(double weight) {
		this.weight = weight;
	}
	
	public abstract double getSimilarity(String text1, String text2);
}
