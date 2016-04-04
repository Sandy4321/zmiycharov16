import features.*;

public class Main {

	public static void main(String[] args) {
		for (Feature feature : Config.Features) {
		    System.out.println(feature.getName());
		}
	}

}
