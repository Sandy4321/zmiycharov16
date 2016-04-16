package opennlp;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

public class SentenceDetector {
	private SentenceDetectorME sdetector;
	public SentenceDetector(String fileName) throws IOException {
		// always start with a model, a model is learned from training data
		InputStream is = new FileInputStream(fileName);
		SentenceModel model = new SentenceModel(is);
		this.sdetector = new SentenceDetectorME(model);
		is.close();
	}
	
	public String[] detect(String input){
		return this.sdetector.sentDetect(input);
	}
}