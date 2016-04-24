package nlp.pos;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import nlp.tokenize.AbstractTokenizer;
import opennlp.MyTokenizer;
import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class OpennlpPOSTagger extends AbstractPOSTagger{
	private POSTaggerME tagger;
	private AbstractTokenizer tokenizer;
	
	private HashMap<String, String> mapping;
	
	public OpennlpPOSTagger(String fileName, AbstractTokenizer tokenizer){
		POSModel model = new POSModelLoader().load(new File(fileName));
		this.tagger = new POSTaggerME(model);
		this.tokenizer = tokenizer;
		// init mapping
		mapping = new HashMap<String, String>();
		mapping.put("CC",   "conjunction");
		mapping.put("CD",   "numeral");
		mapping.put("DT",   "article");
		mapping.put("EX",   "article");
		mapping.put("FW",   "noun");
		mapping.put("IN",   "conjunction");
		mapping.put("JJ",   "adjective");
		mapping.put("JJR",  "adjective");
		mapping.put("JJS",  "adjective");
		mapping.put("LS",   "other");
		mapping.put("MD",   "verb");
		mapping.put("NN",   "noun");
		mapping.put("NNS",  "noun");
		mapping.put("NNP",  "noun");
		mapping.put("NNPS", "noun");
		mapping.put("PDT",  "article");
		mapping.put("POS",  "other");
		mapping.put("PRP",  "pronoun");
		mapping.put("PRPS", "pronoun");
		mapping.put("RB",   "adverb");
		mapping.put("RBB",  "adverb");
		mapping.put("RBS",  "adverb");
		mapping.put("RP",   "particle");
		mapping.put("SYM",  "other");
		mapping.put("TO",   "other");
		mapping.put("UB",   "other");
		mapping.put("VB",   "verb");
		mapping.put("VBD",  "verb");
		mapping.put("VBG",  "verb");
		mapping.put("VBN",  "verb");
		mapping.put("VBP",  "verb");
		mapping.put("VBZ",  "verb");
		mapping.put("WDT",  "article");
		mapping.put("WP",   "pronoun");
		mapping.put("WP$",  "pronoun");
		mapping.put("WRB",  "adverb");
	}
	
	public LinkedList<String>  tag(String input) {
		String parts[] = tokenizer.tokenize(input);
		String tags[]  = this.tagger.tag(parts);
		LinkedList<String> result = new LinkedList<String>();
		for (int i = 0; i < tags.length; i++) {
			String tag = tags[i].toString();
			if(mapping.containsKey(tag)){
				result.add(this.mapping.get(tag)); 
			}
		}
		return result;
	}
}
