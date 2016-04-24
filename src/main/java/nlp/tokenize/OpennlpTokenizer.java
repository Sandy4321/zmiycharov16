package nlp.tokenize;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import main.Config;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class OpennlpTokenizer extends AbstractTokenizer {
	private Tokenizer tokenizer;
	public OpennlpTokenizer(String language) throws IOException {
		String fileName = "";
		
		if(language == Config.LANG_EN){
			fileName = Config.TOKENIZR_PATH_EN;
		}
		
		if(language == Config.LANG_NL){
			fileName = Config.TOKENIZR_PATH_NL;
		}
		
		InputStream is = new FileInputStream(fileName);
		TokenizerModel model = new TokenizerModel(is);
		this.tokenizer = new TokenizerME(model);
		is.close();
	}
	
	public String[] tokenize(String input){
		return this.tokenizer.tokenize(input);
	}
}