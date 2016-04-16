package opennlp;

public class tester {

	public static void main(String[] args) {
		try{
			SentenceDetector detector = new SentenceDetector("./opennlp/en-sent.bin");
			MyTokenizer tokenizer = new MyTokenizer("./opennlp/en-token.bin");
			POStagger tagger = new POStagger("./opennlp/en-pos-maxent.bin", tokenizer);
			String input = "Hi. How are you? This is Mike.";
			String[] sentences = detector.detect(input);
			
			for (String s : sentences){
				System.out.println(s);
				
				String[] tokens = tokenizer.tokenize(s);
				for (String t : tokens){
					System.out.println(t);
					String[] tags = tagger.tag(input);
					for (String tag : tags){
						System.out.println(tag);
					}
				}
			}
		} catch (Exception ex){
			System.out.println(ex.getMessage());
		}
	}
}
