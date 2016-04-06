package features;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config {
	public static List<Feature> Features = Arrays.asList(
		new Test1_Feature()
		, new Test2_Feature()
	);
	
	public static final double MIN_SCORE_TO_RANK = 0.5;
}
