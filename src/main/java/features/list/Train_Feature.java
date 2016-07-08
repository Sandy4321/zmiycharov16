package features.list;

import entities.DocumentsDifference;
import features.core.Feature;
import features.core.IdentificationDocument;
import main.Globals;

public class Train_Feature extends Feature {

	public Train_Feature() {
		super("Train Feature");
	}

	@Override
	public double getDifference(IdentificationDocument doc1, IdentificationDocument doc2) {
		for (DocumentsDifference difference : Globals.TrainDifferences.get(doc1.getFolderName())) {
			if ((difference.getDocument1().equals(doc1.getFileName())
					&& difference.getDocument2().equals(doc2.getFileName()))
					|| (difference.getDocument1().equals(doc2.getFileName())
							&& difference.getDocument2().equals(doc1.getFileName()))) {
				// In this case documents are from the same author: difference = 0
				return 0;
			}
		}

		return 1;
	}
}
