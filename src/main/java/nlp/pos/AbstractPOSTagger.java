package nlp.pos;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.LinkedList;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

abstract public class AbstractPOSTagger {
	/**
	 * Implement this feature to receive POS tag the input
	 * @param input
	 * @return LinkedHashMap<String, String>

	    article
	    verb
	    punctuation
	    adjective
	    adverb
	    conjunction
	    noun
		numeral
		particle
		preposition
		pronoun
		other* 
		
	 * 
	 * 
	 */
	public abstract LinkedHashMap<String, String> tag(String input);
}
