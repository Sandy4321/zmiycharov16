package features;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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
