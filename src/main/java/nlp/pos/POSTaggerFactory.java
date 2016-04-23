package nlp.pos;

import opennlp.MyTokenizer;
import opennlp.POStagger;

public class POSTaggerFactory {
	
	public AbstractPOSTagger getTagger(String language){
		try{
			if(language.equals("English")){
				MyTokenizer tokenizer = new MyTokenizer("./opennlp/en-token.bin");
				return new opennlpPOSTagger("./opennlp/en-pos-maxent.bin", tokenizer);
			}
			
			if(language.equals("Dannish")){
				MyTokenizer tokenizer = new MyTokenizer("./opennlp/da-token.bin");
				return new opennlpPOSTagger("./opennlp/da-pos-maxent.bin", tokenizer);
			}
			
			if(language.equals("Dutsch")){
				MyTokenizer tokenizer = new MyTokenizer("./opennlp/nl-token.bin");
				return new opennlpPOSTagger("./opennlp/nl-pos-maxent.bin", tokenizer);
			}
		}catch ( Exception e ){
			return null;
		}
		
		
		if(language.equals("Greek")){
			// Bind the Greek tagger here
		}
		
		return null;
	}
	
	
	
}
