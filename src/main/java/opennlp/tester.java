package opennlp;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

import main.Config;
import nlp.pos.AbstractPOSTagger;
import nlp.pos.POSTaggerFactory;
import nlp.stopwords.StopWordItem;
import nlp.stopwords.StopWords;
import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.TokenizerFactory;

public class tester {

	public static void main(String[] args) {
		try{
			String input = "and the hell comes near Hi. How are you? This is Mike.";
			AbstractPOSTagger  tagger = POSTaggerFactory.get(Config.LANG_EN);
			LinkedList<String> tags   = tagger.tag(input);
			for (String tag : tags){
				System.out.println(tag);
			}			
		} catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
