package features.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entities.DocumentsDifference;
import main.Globals;

public abstract class Feature {
	private String name;
	private Map<String, List<DocumentsDifference>> differences;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DocumentsDifference> getDifferencesForFolder(String folderName) {
		return differences.get(folderName);
	}
	public void setDifferences(String folderName, List<DocumentsDifference> differences) {
		this.differences.put(folderName, differences);
	}

	public void clearDifferences() {
		this.differences = new HashMap<String, List<DocumentsDifference>>();
	}
	
	public abstract double getDifference(IdentificationDocument doc1, IdentificationDocument doc2);
	
	public void normalizeDifferences() {
		for(String key : this.differences.keySet()) {
			List<DocumentsDifference> folderDifferences = this.differences.get(key);
			
			double maxScore = 0;
			
			for (DocumentsDifference difference : folderDifferences) {
			    if(maxScore < difference.getScore()) {
			    	maxScore = difference.getScore();
			    }
			}

			for (DocumentsDifference difference : folderDifferences) {
				double score = difference.getScore() / maxScore;
				difference.setScore(score);
			}
		}
	}
	
	public Feature(String name) {
		this.setName(name);
		this.differences = new HashMap<String, List<DocumentsDifference>>();
	}
	
	public double getScore(String folderName, String document1, String document2) {
		for(DocumentsDifference difference : this.getDifferencesForFolder(folderName)) {
			if(
					(difference.getDocument1().equals(document1) && difference.getDocument2().equals(document2))
					|| (difference.getDocument1().equals(document2) && difference.getDocument2().equals(document1))
				) {
				return difference.getScore();
			}
		}
		
		return 0;
	}
	
	public double getMinScore() {
		double min = Double.MAX_VALUE;
		
		for(String folder : this.differences.keySet()) {
			for(DocumentsDifference difference : this.getDifferencesForFolder(folder)) {
				if(difference.getScore() < min) {
					min = difference.getScore();
				}
			}
		}
		
		return min;
	}

	public double getMaxScore() {
		double max = Double.MIN_VALUE;
		
		for(String folder : this.differences.keySet()) {
			for(DocumentsDifference difference : this.getDifferencesForFolder(folder)) {
				if(difference.getScore() > max) {
					max = difference.getScore();
				}
			}
		}
		
		return max;
	}
}
