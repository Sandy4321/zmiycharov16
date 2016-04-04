import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import features.*;

public class FeaturesGenerator {
	
	public static List<DocumentsSimilarity> getActualSimilarities(String folderName) throws Exception {
		File truthFile = new File(FeaturesGenerator.class.getResource( "/dataset/truth/" + folderName + "/ranking.json" ).toURI());
		
		Type listType = new TypeToken<ArrayList<DocumentsSimilarity>>() {}.getType();
		List<DocumentsSimilarity> result = new Gson().fromJson(FileUtils.readFileToString(truthFile), listType);

		return result;
	}

	public static void generateFeaturesSimilarities(String folderName) throws Exception {
		URI docsDirUri = FeaturesGenerator.class.getResource( "/dataset/" + folderName ).toURI();
		
		List<File> docFiles = getDocFiles(docsDirUri);
		
		setFeaturesSimilarities(docFiles);
	}

	private static List<File> getDocFiles(URI docsDirUri) {
		File docsDir = new File(docsDirUri);
		
		List<File> result = new ArrayList<File>();
		
		for(File file : docsDir.listFiles()) {
			result.add(file);
		}
		
		return result;
	}
	
	private static void setFeaturesSimilarities(List<File> docFiles) throws Exception {
		for (Feature feature : Config.Features) {
		    List<DocumentsSimilarity> similarities = new ArrayList<DocumentsSimilarity>();
		    for(int i = 0; i < docFiles.size()-1;i++) {
		    	File doc1 = docFiles.get(i);
		    	for(int j = i+1; j < docFiles.size();j++) {
			    	File doc2 = docFiles.get(j);
			    	
			    	DocumentsSimilarity similarity = new DocumentsSimilarity();
			    	similarity.setDocument1(doc1.getName());
			    	similarity.setDocument2(doc2.getName());
			    	similarity.setScore(feature.getSimilarity(
		    				FileUtils.readFileToString(doc1),
		    				FileUtils.readFileToString(doc2)
		    			));
			    	
			    	similarities.add(similarity);
			    }
		    }

	    	feature.setSimilarities(similarities);
		}
	}
}
