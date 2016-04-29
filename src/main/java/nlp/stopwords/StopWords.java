package nlp.stopwords;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import main.Config;
import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.TokenizerFactory;
import opennlp.MyTokenizer;

public class StopWords {
	private HashMap<String, Integer> stopWords;
	private String[] tokens;
	private Locale locale;
	
	public StopWords(String language, String[] tokens){
		this.tokens = tokens;
		
		String line = "";
		try {
			if (language.equals(Config.LANG_EN)) {
				locale = Locale.ENGLISH;
				line   = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_EN));
			}
			else if (language.equals(Config.LANG_GR)) {
				locale = new java.util.Locale("el-GR");
				line   = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_GR));
			}
			else if (language.equals(Config.LANG_NL)) {
				locale = new java.util.Locale("nl_NL");
				line = FileUtils.readFileToString(new File(Config.STOP_WORDS_PATH_NL));
			}
		} catch (IOException e) {
			
		}
		
		stopWords = new HashMap<String, Integer>();
		List<String> words = Arrays.asList(line.split(","));
		int position = 0;
		for (Iterator iterator = words.iterator(); iterator.hasNext();) {
			String word = (String) iterator.next();
			stopWords.put(word.toUpperCase(locale), position++);
		}
	}

	public HashMap<String, StopWordItem> count(String input){
		HashMap<String, StopWordItem> result 
			= new HashMap<String, StopWordItem>();
		
		for(String word : stopWords.keySet()) {
			result.put(word, new StopWordItem(word, 0, stopWords.get(word)));
		}
		
		input = input.replaceAll("[,.;!?(){}\\[\\]<>%]", "");
		input = input.toUpperCase(locale);
		
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			if(stopWords.containsKey(token)){
				StopWordItem item = result.get(token);
				item.increment();
				result.put(token, item);
			}
		}
		
		for (String key : result.keySet()) {
			StopWordItem item = (StopWordItem) result.get(key);
			item.setDocumentWordCount(tokens.length);
			result.put(key, item);
		}
		
		return result;
	}
}