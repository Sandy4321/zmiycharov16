package entities;

import java.util.List;

import features.core.IdentificationDocument;

public class FolderInfo {
	private List<IdentificationDocument> documents;
	private String language;
	private String genre;
	
	public List<IdentificationDocument> getDocuments() {
		return documents;
	}
	public void setDocuments(List<IdentificationDocument> documents) {
		this.documents = documents;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getGenre() {
		return genre;
	}
	public void setGenre(String genre) {
		this.genre = genre;
	}
}
