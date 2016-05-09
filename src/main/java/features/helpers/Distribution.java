package features.helpers;

import java.util.Collections;
import java.util.List;

public class Distribution {

	/**
	 * Sorts the specified dataset object and returns it to the caller.
	 * 
	 * @param dataset
	 *            original dataset (not ordered)
	 * @return
	 */
	public static List<Integer> getRankedDataSet(List<Integer> dataset) {
		Collections.sort(dataset);
		return dataset;
	}

	/**
	 * 
	 * 
	 * @return first quartile of the ranked data set
	 */
	public static Float getFirstQuartile(List<Integer> rankedDataSet) {
		int rankedDatasetSize = rankedDataSet.size();
		if (rankedDataSet == null || rankedDatasetSize == 0) {
			return null;
		}
		return getMedian(rankedDataSet.subList(0, rankedDatasetSize / 2));
	}
	
	/**
	 * 
	 * 
	 * @return second quartile of the ranked data set
	 */
	public static Float getMedian(List<Integer> rankedDataSet) {
		int rankedDatasetSize = rankedDataSet.size();
		if (rankedDataSet == null || rankedDatasetSize == 0) {
			return null;
		}
		Float result = null;
		// In case when the size of the dataset is even number
		// as median we have to use the average number between the middle index
		// and middle index minus one
		if (rankedDatasetSize % 2 == 0) {
			result = new Float(
					rankedDataSet.get((rankedDatasetSize / 2) - 1) + rankedDataSet.get(rankedDatasetSize / 2)) / 2;
		} else {
			result = new Float(rankedDataSet.get((rankedDatasetSize / 2)));
		}
		return result;
	}

	/**
	 * 
	 * @param rankedDataSet
	 * @return third quartile of the ranked data set
	 */
	public static Float getThirdQuartile(List<Integer> rankedDataSet) {
		int rankedDatasetSize = rankedDataSet.size();
		if (rankedDataSet == null || rankedDatasetSize == 0) {
			//TODO revise !
			return 0.0f;
		}
		Float result = null;
		if (rankedDatasetSize % 2 == 0) {
			result = getMedian(rankedDataSet.subList(rankedDatasetSize / 2, rankedDatasetSize));
		} else {
			result = getMedian(rankedDataSet.subList((rankedDatasetSize / 2) + 1, rankedDatasetSize));
		}
		return result;
	}
}
