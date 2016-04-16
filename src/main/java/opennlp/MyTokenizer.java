package opennlp;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class MyTokenizer {
	private Tokenizer tokenizer;
	public MyTokenizer(String fileName) throws IOException {
		InputStream is = new FileInputStream(fileName);
		TokenizerModel model = new TokenizerModel(is);
		this.tokenizer = new TokenizerME(model);
		is.close();
	}
	
	public String[] tokenize(String input){
		return this.tokenizer.tokenize(input);
	}
}