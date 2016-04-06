package features;

import java.util.List;

public abstract class Feature {
	private String name;
	// Positive number
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
	
	public abstract double getSimilarity(Document doc1, Document doc2);
	
	public void normalizeSimilarities() {
		double maxScore = 0;
		
		for (DocumentsSimilarity similarity : this.similarities) {
		    if(maxScore < similarity.getScore()) {
		    	maxScore = similarity.getScore();
		    }
		}

		for (DocumentsSimilarity similarity : this.similarities) {
			double score = similarity.getScore() / maxScore;
			similarity.setScore(score);
		}
	}
	
	public Feature() {
		this.weight = Config.DEFAULT_FEATURE_WEIGHT;
	}
}
