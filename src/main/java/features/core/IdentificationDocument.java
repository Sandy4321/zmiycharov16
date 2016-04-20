package features.core;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.AnalyzerUtil;

import features.helpers.DocumentFeaturesHelpers;

public class IdentificationDocument {
	// COMMON PROPERTIES
	private String content;
	private String fileName;
	private String folderName;
	private String language;
	private String genre;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
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

	// FEATURES
	private double meanSentenceLength;

	public double getMeanSentenceLength() {
		return meanSentenceLength;
	}
	
	private void setMeanSentenceLength() {
		String[] sentences = AnalyzerUtil.getSentences(this.getContent(), 0);
		this.meanSentenceLength = DocumentFeaturesHelpers.getMeanSentenceLength(sentences);
	}

	public IdentificationDocument() {}
	
	public IdentificationDocument(File file, String language, String genre) throws Exception {
		this.content = FileUtils.readFileToString(file);
		this.fileName = file.getName();
		this.folderName = file.getParentFile().getName();
		
		this.language = language;
		this.genre = genre;
		
		setFeatures();
	}
	
	private void setFeatures() {
		setMeanSentenceLength();
	}
}
