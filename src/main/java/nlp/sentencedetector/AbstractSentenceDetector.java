package nlp.sentencedetector;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

abstract public class AbstractSentenceDetector {
	public abstract String[] detect(String input);
}
