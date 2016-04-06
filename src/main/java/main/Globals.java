package main;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import features.Feature;
import features.Test1_Feature;
import features.Test2_Feature;

public class Globals {
	public static List<Feature> Features = Arrays.asList(
		new Test1_Feature()
		, new Test2_Feature()
	);
	
	public static List<File> DocFiles;
}
