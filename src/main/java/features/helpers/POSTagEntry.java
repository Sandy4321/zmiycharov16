package features.helpers;

public class POSTagEntry {
	private String word;
	private String postag;
	private Integer numberOfOccurrences;

	public POSTagEntry() {
		super();
	}

	public POSTagEntry(String word, String postag, Integer numberOfOccurrences) {
		this.word = word;
		this.postag = postag;
		this.numberOfOccurrences = numberOfOccurrences;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public String getPostag() {
		return postag;
	}

	public void setPostag(String postag) {
		this.postag = postag;
	}

	public Integer getNumberOfOccurrences() {
		return numberOfOccurrences;
	}

	public void setNumberOfOccurrences(Integer numberOfOccurrences) {
		this.numberOfOccurrences = numberOfOccurrences;
	}

}
