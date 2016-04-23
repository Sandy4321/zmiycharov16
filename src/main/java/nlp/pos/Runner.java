package nlp.pos;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import entities.DocumentsSimilarity;
import entities.FolderInfo;
import entities.JsonProblem;
import features.core.Feature;
import features.core.FeaturesGenerator;
import features.core.IdentificationDocument;
import main.Config;
import main.Errors;
import main.Globals;
import main.Logistic;
import main.Results;

public class Runner {
	public static void main(String[] args) {
		try{

			File inputFolder = new File("_DATA/dataset");
			File inputInfoJson = new File(inputFolder, "info.json");
			
			Type jsonProblemListType = new TypeToken<ArrayList<JsonProblem>>() {}.getType();
			Globals.JsonProblems = new Gson().fromJson(FileUtils.readFileToString(inputInfoJson), jsonProblemListType);
			
			for(JsonProblem problem : Globals.JsonProblems) {
				String folderName = problem.getFolder();
				System.out.println(folderName);
				
				FeaturesGenerator.generateIdentificationDocs(inputFolder, problem);
				
				FolderInfo folderInfo = Globals.IdentificationDocs.get(folderName);
				List<IdentificationDocument> docs = folderInfo.getDocuments();
			    for(int i = 0; i < docs.size()-1;i++) {
			    	System.out.println(i);
					IdentificationDocument doc1 = docs.get(i);
			    	System.out.println(doc1.getContent());
			    }
			}
			
		}catch(Exception e){

			System.out.println(e.getMessage());
			
		}
		
	}
}
