package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import features.Feature;
import features.Test1_Feature;
import features.Test2_Feature;

public class Config {
	public static final double MIN_SCORE_TO_RANK = 0.5;
	public static final double DEFAULT_FEATURE_WEIGHT = 0.28;
	
	// CHANGE FOR PERSONAL USE
	public static String inputFolderPath = "D:/University/Projects/pam-author-identification/_DATA/dataset";
	public static String outputFolderPath = "D:/University/Projects/pam-author-identification/_DATA/output";
	public static String truthFolderPath = "D:/University/Projects/pam-author-identification/_DATA/truth";
}
