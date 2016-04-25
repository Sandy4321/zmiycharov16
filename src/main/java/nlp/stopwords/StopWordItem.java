package nlp.stopwords;

public class StopWordItem {
	private int count;
	private int position;
	private double percent;
	
	private String word;
	
	public StopWordItem(String word, int count, int position){
		this.word     = word;
		this.count    = count;
		this.position = position;
	}
	
	public String toString(){
		return word + " " + position + " " + count + " -> " + percent + "%";
	}
	
	public void setDocumentWordCount(int documentWordCount){
		this.percent = (count * 1.0) / documentWordCount;
	}
	
	public void increment(){ this.count++; }
	public double getPercent(){return this.percent;}
	public int getCount(){return this.count;}
	public int getPosition(){return this.position;}
}