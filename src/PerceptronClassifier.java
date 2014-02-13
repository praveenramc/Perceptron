/*
 *  @author Praveen Ram Chandiran
 *  
 *  Contains methods for training and testing. 
 */


import java.util.ArrayList;

public class PerceptronClassifier {

	public static final int HAM = 1;
	public static final int SPAM = -1;
	public static final int NO_STOP_WORDS = 0;
	public static final int WITH_STOP_WORDS = 1;
	Document doc = new Document();

	public final int weightUpdateIterationsLimit = 25;
	public final double eta = 0.001;
	int w0 = 1;

	public void train() {

		System.out.println("Loading training datasets ...");
		ArrayList<String> vocabOfDoc = new ArrayList<String>();
		int spamOffset = 0;

		System.out.println("Creating Dictionary from the training set ...");
		System.out
				.println("Updating the weights for the distinct words in Dictionary ...");
		System.out.println("Step Size is " + eta + " and Number of Iteration "
				+ weightUpdateIterationsLimit);

		for (int looper = 0; looper < Document.totalDocCount; looper++) {

			if (looper < Document.hamFileList.length) {

				vocabOfDoc = doc.createVocabForDoc(
						Document.hamFileList[looper], NO_STOP_WORDS);
				updateWeights(HAM, vocabOfDoc);
				// System.out.println("Ham File"+looper);
			}

			else {
				vocabOfDoc = doc.createVocabForDoc(
						Document.spamFileList[spamOffset], NO_STOP_WORDS);
				updateWeights(SPAM, vocabOfDoc);

				spamOffset++;
			}
		}

		/*
		 * for (String key : Document.WordWeight.keySet()) {
		 * 
		 * Double value = Document.WordWeight.get(key);
		 * System.out.println("Key = " + key + ", Value = " + value);
		 * 
		 * }
		 */

	}

	public void test(int stopWordsIndicator) {

		int classifiedAsHam = 0;
		int classifiedAsSpam = 0;

		ArrayList<String> vocabOfDoc = new ArrayList<String>();
		int spamOffset = 0;

		System.out.println("Loading test data set ...");
		for (int looper = 0; looper < Document.totalDocCount; looper++) {

			if (looper < Document.hamFileList.length) {

				vocabOfDoc = doc.createVocabForDoc(
						Document.hamFileList[looper], stopWordsIndicator);
				int classifiedAs = classify(HAM, vocabOfDoc);
				if (classifiedAs == 1)
					classifiedAsHam++;

			}

			else {
				vocabOfDoc = doc.createVocabForDoc(
						Document.spamFileList[spamOffset], stopWordsIndicator);
				int classifiedAs = classify(SPAM, vocabOfDoc);
				if (classifiedAs == -1)
					classifiedAsSpam++;
				spamOffset++;
			}
		}

		calculateAccuracy(classifiedAsHam, Document.hamFileList.length,
				classifiedAsSpam, Document.spamFileList.length);

		doc.stopWordsDictionary();
		doc.removeStopWords();
		/*
		 * System.out.println("Ham Classification:" + classifiedAsHam);
		 * System.out.println("Spam Classification:" + classifiedAsSpam);
		 */

	}

	public void calculateAccuracy(int classifiedAsHam, int hamDocCount,
			int classifiedAsSpam, int spamDocCount) {
		double accuracy = 0.0;
		double hamAccuracy = 0.0;
		double spamAccuracy = 0.0;

		System.out.println("Calculating Accuracy ...");
		hamAccuracy = ((double) classifiedAsHam / hamDocCount) * 100;
		spamAccuracy = ((double) classifiedAsSpam / spamDocCount) * 100;
		accuracy = ((double) (classifiedAsHam + classifiedAsSpam) / (hamDocCount + spamDocCount)) * 100;
		/*
		 * System.out.println("Ham Accuracy: "+ hamAccuracy);
		 * System.out.println("Spam Accuracy: " + spamAccuracy);
		 */
		System.out.println("Accuracy: " + accuracy);

	}

	public int classify(int type, ArrayList<String> vocab) {
		int classifiedAs = ((w0 + dotProduct(vocab)) > 0.0 ? 1 : -1);
		return classifiedAs;
	}

	public void updateWeights(int classification, ArrayList<String> vocab) {

		for (int iterator = 0; iterator < weightUpdateIterationsLimit; iterator++) {
			double currDocClassification = ((w0 + dotProduct(vocab)) > 0.0 ? 1.0
					: -1.0);
			// Update each weight
			for (int looper = 0; looper < vocab.size(); looper++) {
				String word = vocab.get(looper);
				double weight = (eta * (classification - currDocClassification) * Document.WordWeight
						.get(word));
				if (weight != 0.0) {
					Document.WordWeight.put(word, weight);
				}
			}
		}

	}

	public double dotProduct(ArrayList<String> vocab) {

		double sum = 0.0;

		for (int looper = 0; looper < vocab.size(); looper++) {
			double frequency = 1;
			double wordWeight = -1;

			if ((Document.Dictionary.containsKey(vocab.get(looper)))
					&& (Document.WordWeight.containsKey(vocab.get(looper)))) {
				frequency = Document.Dictionary.get(vocab.get(looper));
				wordWeight = Document.WordWeight.get(vocab.get(looper));
				// System.out.println("Word Weight: "+wordWeight);
			}

			sum += (frequency * wordWeight);
		}

		return sum;

	}

}
