package features.helpers;

import java.util.LinkedHashMap;
import java.util.List;

public class Distribution {
	// TODO use generics instead of concrete types
	private LinkedHashMap<String, Float> data;
	// represents the value of the 25th percentile of the distribution
	private int q1;
	// represents the median value of the distribution. Median
	// is the 50th percentile of the distribution
	private int median;
	// represents the value of the 75th percentile of the distribution
	private int q3;

	private List outliers;
	private List<Integer> rankedDataSet;
	
	public List<Integer> getRankedDataSet() {
		return rankedDataSet;
	}

	
	public int getQ1() {
		return q1;
	}

	public int getMedian() {
		return median;
	}

	public int getQ3() {
		return q3;
	}

	protected LinkedHashMap<String, Float> getData() {
		return this.data;
	}
}
