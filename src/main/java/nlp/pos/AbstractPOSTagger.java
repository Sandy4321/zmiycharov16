package nlp.pos;

import java.util.LinkedList;

import features.helpers.POSTagEntry;


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
	public static String POSTAG_NOUN = "noun";
	public static String POSTAG_ADJECTIVE = "adjective";
	public static String POSTAG_VERB = "verb";
	
	public abstract LinkedList<POSTagEntry> tag(String input);
}
