package features.core;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.wordnet.AnalyzerUtil;

import features.helpers.DocumentFeaturesHelpers;
import features.helpers.DocumentPOSDistribution;
import nlp.stopwords.StopWordItem;
import nlp.stopwords.StopWords;
import nlp.tokenize.AbstractTokenizer;
import nlp.tokenize.TokenizerFactory;

public class IdentificationDocument {
	// COMMON PROPERTIES
	private String content;
	private String fullPath;
	private String fileName;
	private String folderName;
	private String language;
	private String genre;
	private TreeMap<String, LinkedList<Integer>> postagDistributions;
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public TreeMap<String, LinkedList<Integer>> getPostagDistributions() {
		return postagDistributions;
	}

	public String getFullPath() {
		return fullPath;
	}

	public void setFullPath(String fullPath) {
		this.fullPath = fullPath;
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

	public IdentificationDocument() {}
	
	public IdentificationDocument(File file, String language, String genre) throws Exception {
		this.content = FileUtils.readFileToString(file);
		this.fullPath = file.getAbsolutePath();
		this.fileName = file.getName();
		this.folderName = file.getParentFile().getName();
		
		this.language = language;
		this.genre = genre;
		this.postagDistributions = new DocumentPOSDistribution(file, language).getPOStagDistributions();
		for(Map.Entry<String, LinkedList<Integer>> entry: this.postagDistributions.entrySet()) {
			System.out.println(entry.getKey() + "  -> " + entry.getValue().toString());
		}
		setProperties();
	}
	
	// PROPERTIES
	private double meanSentenceLength;
	private Map<String, StopWordItem> stopWordsMap;
	private double[] punctuationMarksPercentages;
	private double uniqueWordsPercentage;

	public double getMeanSentenceLength() {
		return meanSentenceLength;
	}
	
	private void setMeanSentenceLength() {
		String[] sentences = AnalyzerUtil.getSentences(this.getContent(), 0);
		this.meanSentenceLength = DocumentFeaturesHelpers.getMeanSentenceLength(sentences);
	}

	public Map<String, StopWordItem> getStopWordsMap() {
		return stopWordsMap;
	}

	private void setStopWordsMap(String[] tokens) throws Exception {
		StopWords sw = new StopWords(this.getLanguage(), tokens);
		this.stopWordsMap = sw.count(this.content);
	}

	public double[] getPunctuationMarksPercentages() {
		return punctuationMarksPercentages;
	}

	public void setPunctuationMarksPercentages() throws Exception {
		this.punctuationMarksPercentages = DocumentFeaturesHelpers.getPunctuationMarksPercentages(this.getContent(), this.getLanguage());
	}

	public double getUniqueWordsPercentage() {
		return uniqueWordsPercentage;
	}

	public void setUniqueWordsPercentage(String[] tokens) throws Exception {
		this.uniqueWordsPercentage = DocumentFeaturesHelpers.getUniqueWordsPercentage(this.getContent(), this.getLanguage(), tokens);
	}

	private void setProperties() throws Exception {
		AbstractTokenizer tokenizer = TokenizerFactory.get(this.language);
		String[] tokens = tokenizer.tokenize(this.content);
		
		setMeanSentenceLength();
		setStopWordsMap(tokens);
		setPunctuationMarksPercentages();
		setUniqueWordsPercentage(tokens);
	}
}
