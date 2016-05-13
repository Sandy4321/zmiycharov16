package features.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import entities.DocumentsSimilarity;
import main.Globals;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;
import net.sf.javaml.core.DenseInstance;
import net.sf.javaml.core.Instance;
import net.sf.javaml.featureselection.scoring.RELIEF;
import weka.attributeSelection.ReliefFAttributeEval;

public abstract class Feature {
	private String name;
	private Map<String, List<DocumentsSimilarity>> similarities;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	public abstract double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2);
	
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
	
	public void applyRELIEF() {
		System.out.println("AAAAA");
		Dataset data = new DefaultDataset();
		
		for(String key : this.similarities.keySet()) {
			List<DocumentsSimilarity> folderSimilarities =
					this.similarities.get(key);
			
			List<Double> values = new LinkedList<Double>();
			
			for (DocumentsSimilarity similarity : folderSimilarities) {
				values.add(similarity.getScore());
			}
			
			System.out.println("Here come the drums");
			
		}
		
		RELIEF r = new RELIEF();
		r.build(data);
		
		for (int i = 0; i < r.noAttributes(); i++){
			System.out.println(r.score(i));
		}
	}
	
	public Feature(String name) {
		this.setName(name);
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
	
	public double getMinScore() {
		double min = Double.MAX_VALUE;
		
		for(String folder : this.similarities.keySet()) {
			for(DocumentsSimilarity similarity : this.getSimilaritiesForFolder(folder)) {
				if(similarity.getScore() < min) {
					min = similarity.getScore();
				}
			}
		}
		
		return min;
	}

	public double getMaxScore() {
		double max = Double.MIN_VALUE;
		
		for(String folder : this.similarities.keySet()) {
			for(DocumentsSimilarity similarity : this.getSimilaritiesForFolder(folder)) {
				if(similarity.getScore() > max) {
					max = similarity.getScore();
				}
			}
		}
		
		return max;
	}
}
