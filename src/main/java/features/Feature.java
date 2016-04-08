package features;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import main.Config;
import main.Globals;

public abstract class Feature {
	private String name;
	private Map<String, List<DocumentsSimilarity>> similarities;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public double getWeight() {
		return Globals.FeaturesWeights.get(this.getName());
	}
	public void setWeight(double weight) {
		Globals.FeaturesWeights.put(this.getName(), weight);
	}
	public List<DocumentsSimilarity> getSimilaritiesForFolder(String folderName) {
		return similarities.get(folderName);
	}
	public void setSimilarities(String folderName, List<DocumentsSimilarity> similarities) {
		this.similarities.put(folderName, similarities);
	}

	public void clearSimilarities() {
		this.similarities = new HashMap<String, List<DocumentsSimilarity>>();
	}
	
	public abstract double getSimilarity(Document doc1, Document doc2);
	
	public void normalizeSimilarities() {
		for(String key : this.similarities.keySet()) {
			List<DocumentsSimilarity> folderSimilarities = this.similarities.get(key);
			
			double maxScore = 0;
			
			for (DocumentsSimilarity similarity : folderSimilarities) {
			    if(maxScore < similarity.getScore()) {
			    	maxScore = similarity.getScore();
			    }
			}

			for (DocumentsSimilarity similarity : folderSimilarities) {
				double score = similarity.getScore() / maxScore;
				similarity.setScore(score);
			}
			
			
		}
	}
	
	public Feature() {
		this.setWeight(0);
		this.similarities = new HashMap<String, List<DocumentsSimilarity>>();
	}
	
	public double getScore(String folderName, String document1, String document2) {
		for(DocumentsSimilarity similarity : this.getSimilaritiesForFolder(folderName)) {
			if(
					(similarity.getDocument1().equals(document1) && similarity.getDocument2().equals(document2))
					|| (similarity.getDocument1().equals(document2) && similarity.getDocument2().equals(document1))
				) {
				return similarity.getScore();
			}
		}
		
		return 0;
	}
}
