package main;

public class Utils {
	public static int calculateCouplesCountFromTotal(int number) {
		int result = 0;

		for (int i = 1; i < number; i++) {
			result += i;
		}

		return result;
	}
}
