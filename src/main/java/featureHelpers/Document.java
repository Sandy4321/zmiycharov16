package featureHelpers;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.AnalyzerUtil;

public class Document {
	// COMMON PROPERTIES
	private String content;
	private String fileName;
	private String folderName;

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

	// FEATURES
	private double meanSentenceLength;

	public double getMeanSentenceLength() {
		return meanSentenceLength;
	}
	
	private void setMeanSentenceLength() {
		String[] sentences = AnalyzerUtil.getSentences(this.getContent(), 0);
		this.meanSentenceLength = DocumentFeaturesHelpers.getMeanSentenceLength(sentences);
	}

	public Document() {}
	
	public Document(File file) throws Exception {
		this.content = FileUtils.readFileToString(file);
		this.fileName = file.getName();
		this.folderName = file.getParentFile().getName();
		
		setFeatures();
	}
	
	private void setFeatures() {
		setMeanSentenceLength();
	}
}
