package featureHelpers.lucene;

import java.io.IOException;

import org.apache.lucene.wordnet.AnalyzerUtil;

public class LuceneTester {

	String indexDir = "./_DATA/index_output";
	String dataDir = "./_DATA/dataset/dataset/problem001";//use problem001 for test purposes

	public static void main(String[] args) {
		LuceneTester tester;
		try {
			tester = new LuceneTester();
			tester.createIndex();
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void createIndex() throws IOException {
		Indexer indexer = null;

		try {
			indexer = new Indexer(indexDir);

			int numIndexed;
			long startTime = System.currentTimeMillis();
			numIndexed = indexer.createIndex(dataDir, new TextFileFilter());
			long endTime = System.currentTimeMillis();
			System.out.println(numIndexed + " File indexed, time taken: " + (endTime - startTime) + " ms");

		} finally {
			indexer.close();
		}
	}
}
