import java.util.List;

import features.*;

public class Main {

	public static void main(String[] args) throws Exception {
		String folderName = "problem001";
		
		FeaturesGenerator.generateFeaturesSimilarities(folderName);
		
		List<DocumentsSimilarity> trainSimilarities = FeaturesGenerator.getActualSimilarities(folderName);
	}

}
