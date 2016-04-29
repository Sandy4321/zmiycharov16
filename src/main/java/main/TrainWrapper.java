package main;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.core.FastVector;
import weka.core.Instances;

public class TrainWrapper {
	private Instances trainingSet;

	public Instances getTrainingSet() {
		return trainingSet;
	}

	public void setTrainingSet(Instances trainingSet) {
		this.trainingSet = trainingSet;
	}
	
}
