package main;
import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Main {

	public static void main(String[] args) throws Exception {
		File inputFolder = new File(Config.inputFolderPath);
		File infoJson = new File(inputFolder, "info.json");
		
		File outputFolder = new File(Config.outputFolderPath);
		FileUtils.cleanDirectory(outputFolder);
		
		Type listType = new TypeToken<ArrayList<JsonProblem>>() {}.getType();
		List<JsonProblem> jsonProblems = new Gson().fromJson(FileUtils.readFileToString(infoJson), listType);
		
		for(JsonProblem problem : jsonProblems) {
			String folderName = problem.getFolder();
			
			FeaturesGenerator.generateFeaturesSimilarities(folderName);
			
			Results.generateResults(folderName);
			
			Results.generateOutput(new File(outputFolder, folderName));
		}
	}

}
