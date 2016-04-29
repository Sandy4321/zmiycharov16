package opennlp;

import java.util.HashMap;
import java.util.LinkedHashMap;

import java.util.Map;

import main.Config;
import nlp.pos.AbstractPOSTagger;
import nlp.pos.POSTaggerFactory;
import nlp.stopwords.StopWordItem;
import nlp.stopwords.StopWords;
import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.TokenizerFactory;

public class Tester {

	public static void main(String[] args) {
		try {
			String input = "and the hell comes near Hi. How are you? This is Mike.";

			AbstractTokenizer tokenizer = TokenizerFactory.get(Config.LANG_EN);
			String[] tokens = tokenizer.tokenize(input);
			
			StopWords ws = new StopWords(Config.LANG_EN, tokens);
			
			
			HashMap<String, StopWordItem> ls = ws.count(input);
			for (String key : ls.keySet()) {
				System.out.println(ls.get(key));
			}
			
			AbstractPOSTagger tagger = POSTaggerFactory.get(Config.LANG_EN);
			LinkedHashMap<String, String> tags = tagger.tag(input);
			for (Map.Entry<String, String> entry : tags.entrySet()) {
				System.out.println(entry.getKey() + " -> " + entry.getValue());
			}

			String greekInput = "Στο ΠΙΑΤΣΑ ΜΕΖΕΔΑΚΙ με οδήγησε ουσιαστικά το κινητό μου τηλέφωνο! Μετά από δουλειές στη γύρω περιοχή βρεθήκαμε ένα Σάββατο απόγευμα πεινασμένοι, να αναρωτιόμαστε, τι να φάμε και σήμερα.";
			AbstractPOSTagger greekTagger = POSTaggerFactory.get(Config.LANG_GR);
			LinkedHashMap<String, String> greekTags = greekTagger.tag(greekInput);
			for (Map.Entry<String, String> greekEntry : greekTags.entrySet()) {
				System.out.println(greekEntry.getKey() + " -> " + greekEntry.getValue());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
