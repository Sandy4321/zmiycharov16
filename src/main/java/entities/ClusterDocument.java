package entities;

public class ClusterDocument {
	private String document;

	public String getContent() {
		return document;
	}

	public void setContent(String document) {
		this.document = document;
	}
	
	public ClusterDocument(){}
	
	public ClusterDocument(String document) {
		this.document = document;
	}

}
