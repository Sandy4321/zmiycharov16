package opennlp;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.util.ObjectStream;
import opennlp.tools.util.PlainTextByLineStream;

public class POStagger {
	private POSTaggerME tagger;
	private MyTokenizer tokenizer;
	
	public POStagger(String fileName, MyTokenizer tokenizer){
		POSModel model = new POSModelLoader().load(new File(fileName));
		this.tagger = new POSTaggerME(model);
		this.tokenizer = tokenizer;
	}
	
	public String[] tag(String input) throws IOException {
		String parts[] = tokenizer.tokenize(input);
		return this.tagger.tag(parts);
	}
}