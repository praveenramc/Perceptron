/*
 *  @author Praveen Ram Chandiran
 *  
 *  Contains the main method. 
 */

import java.io.File;

public class Perceptron {

	public static final String TRAIN = "train";
	public static final String TEST = "test";

	public static void main(String[] args) {

		String directory = args[0];
		File hamData = new File(directory + args[1] + "/ham/");
		File spamData = new File(directory + args[1] + "/spam/");

		Document trainds = new Document(directory, hamData, spamData);
		trainds.iterateOverAllTheFiles();

		PerceptronClassifier pc = new PerceptronClassifier();
		pc.train();

		trainds = null;
		hamData = new File(directory + args[2] + "/ham/");
		spamData = new File(directory + args[2] + "/spam/");

		Document testds = new Document(directory, hamData, spamData);
		for (String key : Document.WordWeight.keySet()) {
			Double value = Document.WordWeight.get(key);
		}
		pc.test(0);

		System.out.println();
		System.out.println("Removing Stop Words and recalculating accuracy");
		pc.test(1);

	}

}
