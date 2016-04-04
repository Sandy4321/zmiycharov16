package features;

import java.util.List;

public abstract class Feature {
	private String name;
	// Between 0 and 1
	private double weight;
	private List<DocumentsSimilarity> similarities;
	
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
	public List<DocumentsSimilarity> getSimilarities() {
		return similarities;
	}
	public void setSimilarities(List<DocumentsSimilarity> similarities) {
		this.similarities = similarities;
	}
	
	public abstract double getSimilarity(String text1, String text2);
}
