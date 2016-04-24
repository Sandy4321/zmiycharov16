package nlp.pos;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;

import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

abstract public class AbstractPOSTagger {
	/**
	 * Implement this feature to receive POS tag the input
	 * @param input
	 * @return String[]

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
	public abstract LinkedList<String> tag(String input);
}
