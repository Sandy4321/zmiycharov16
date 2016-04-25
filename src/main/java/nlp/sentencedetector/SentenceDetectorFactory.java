package nlp.sentencedetector;

import main.Config;
import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.OpennlpTokenizer;
import nlp.tokenize.TokenizerFactory;
import opennlp.MyTokenizer;
import opennlp.POStagger;

public class SentenceDetectorFactory {
	
	public static AbstractSentenceDetector get(String language){
		try{
			if(language.equals(Config.LANG_EN)){
				return new OpennlpSentenceDetector(Config.POSTAGGER_PATH_EN);
			}
			
			if(language.equals(Config.LANG_NL)){
				return new OpennlpSentenceDetector(Config.TOKENIZR_PATH_NL);
			}
			
			if(language.equals(Config.LANG_GR)){
				return new GreekSentenceDetector();
			}
			
			return null;
		}catch ( Exception e ){
			return null;
		}
	}
	
	
	
}
