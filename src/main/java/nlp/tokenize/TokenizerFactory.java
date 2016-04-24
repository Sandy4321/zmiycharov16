package nlp.tokenize;

import main.Config;

public class TokenizerFactory {
	public static AbstractTokenizer get(String language){
		try{
			if(language.equals(Config.LANG_EN)){
				return new OpennlpTokenizer(Config.LANG_EN);
			}
			
			if(language.equals(Config.LANG_NL)){
				return new OpennlpTokenizer(Config.LANG_NL);
			}
			
			if(language.equals(Config.LANG_GR)){
				return new GreekTokenizer();
   			}
			
			return null;
		}catch ( Exception e ){
			return null;
		}
	}
}
