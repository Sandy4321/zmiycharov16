package nlp.stopwords;

public class StopWordItem {
	private int count;
	private int position;
	private String word;
	
	public StopWordItem(String word, int count, int position){
		this.word     = word;
		this.count    = count;
		this.position = position;
	}
	
	public String toString(){
		return word + " " + position + " " + count;
	}
	
	public void increment(){ this.count++; }
	public int getCount(){return this.count;}
	public int getPosition(){return this.position;}
}