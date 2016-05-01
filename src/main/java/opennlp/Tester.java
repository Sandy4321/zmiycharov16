package opennlp;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import features.helpers.POSTagEntry;
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
//				System.out.println(ls.get(key));
			}
			
			AbstractPOSTagger tagger = POSTaggerFactory.get(Config.LANG_EN);
			LinkedList<POSTagEntry> tags = tagger.tag(input);
			for (POSTagEntry entry : tags) {
//				System.out.println(entry.getWord() + " -> " + entry.getPostag() + ": " + entry.getNumberOfOccurrences());
			}
			
			String dutchInput = "hej";//"Er zijn in de Europese Unie in verscheidene landen verkiezingen op komst en dat verlamt het Spaanse EU-voorzitterschap.";
			
			//TODO fix problems with the NL classifier
			AbstractPOSTagger dutchTagger = POSTaggerFactory.get(Config.LANG_NL);
			LinkedList<POSTagEntry> dutchTags = dutchTagger.tag(dutchInput);
			for (POSTagEntry entry : dutchTags) {
				System.out.println(entry.getWord() + " -> " + entry.getPostag() + ": " + entry.getNumberOfOccurrences());
			}

			String greekInput = "Στο ΠΙΑΤΣΑ ΜΕΖΕΔΑΚΙ με οδήγησε ουσιαστικά το κινητό μου τηλέφωνο! Μετά από δουλειές στη γύρω περιοχή βρεθήκαμε ένα Σάββατο απόγευμα πεινασμένοι, να αναρωτιόμαστε, τι να φάμε και σήμερα.";
			AbstractPOSTagger greekTagger = POSTaggerFactory.get(Config.LANG_GR);
			LinkedList<POSTagEntry> greekTags = greekTagger.tag(greekInput);
			for (POSTagEntry entry : greekTags) {
//				System.out.println(entry.getWord() + " -> " + entry.getPostag() + ": " + entry.getNumberOfOccurrences());
			}
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}
