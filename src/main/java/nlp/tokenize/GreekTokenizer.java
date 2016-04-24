package nlp.tokenize;

public class GreekTokenizer extends AbstractTokenizer{
	public String[] tokenize(String input){
		input.replaceAll("\\s\\s", " ");
		return input.split(" ");
	}
}