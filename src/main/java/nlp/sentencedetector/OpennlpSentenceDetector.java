package nlp.sentencedetector;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import nlp.tokenize.AbstractTokenizer;
import opennlp.MyTokenizer;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class OpennlpSentenceDetector extends AbstractSentenceDetector{
	private SentenceDetectorME sdetector;
	
	public OpennlpSentenceDetector(String fileName) throws IOException{
		InputStream is = new FileInputStream(fileName);
		SentenceModel model = new SentenceModel(is);
		this.sdetector = new SentenceDetectorME(model);
		is.close();
	}
	
	public String[] detect(String input) {
		return this.sdetector.sentDetect(input);
	}
}
