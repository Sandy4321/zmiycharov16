package features;

import featureHelpers.IdentificationDocument;
import featureHelpers.DocumentsSimilarity;
import featureHelpers.Feature;
import main.Globals;

public class Train_Feature extends Feature {

	public Train_Feature() {
		super("Train Feature");
	}

	@Override
	public double getSimilarity(IdentificationDocument doc1, IdentificationDocument doc2) {
		for (DocumentsSimilarity similarity : Globals.TrainSimilarities.get(doc1.getFolderName())) {
			if ((similarity.getDocument1().equals(doc1.getFileName())
					&& similarity.getDocument2().equals(doc2.getFileName()))
					|| (similarity.getDocument1().equals(doc2.getFileName())
							&& similarity.getDocument2().equals(doc1.getFileName()))) {
				return similarity.getScore();
			}
		}

		return 0;
	}
}
