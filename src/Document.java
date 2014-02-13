/*
 *  @author Praveen Ram Chandiran
 *  
 *  Contains methods for creating dictionary, stop words. 
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Document {

	public static HashMap<String, Integer> Dictionary = new HashMap<String, Integer>();
	public static HashMap<String, Double> WordWeight = new HashMap<String, Double>();
	public static ArrayList<String> VocabOfADocument = new ArrayList<String>();
	public static ArrayList<String> StopWords = new ArrayList<String>();

	public static String directory = null;
	public static File[] hamFileList = null;
	public static File[] spamFileList = null;

	public static int totalDocCount = 0;

	Document() {

	}

	Document(String directory, File hamDataSet, File spamDataSet) {
		this.directory = directory;
		this.hamFileList = hamDataSet.listFiles();
		this.spamFileList = spamDataSet.listFiles();
		totalDocCount = getTotalDocCount(hamFileList, spamFileList);

	}

	public void iterateOverAllTheFiles() {

		int spamOffset = 0;
		for (int looper = 0; looper < totalDocCount; looper++) {

			if (looper < hamFileList.length) {
				createVocabulary(hamFileList[looper]);
			}

			else {
				createVocabulary(spamFileList[spamOffset]);
				spamOffset++;
			}
		}

		for (String key : Dictionary.keySet()) {
			WordWeight.put(key, 0.5);

		}
	}

	public void createVocabulary(File fileName) {

		UtilityFunctions uf = new UtilityFunctions();
		try {

			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String word = tokens.nextToken();

					if (Dictionary.containsKey(word)) {
						int value = Dictionary.get(word);
						Dictionary.put(word, value + 1);
					}

					else {
						Dictionary.put(word, 1);
					}
				}

			}

		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

	}

	public void stopWordsDictionary() {
		
		
		StopWords.clear();
		UtilityFunctions uf = new UtilityFunctions();

		try {

			FileReader file = new FileReader(directory + "stopwords.txt");
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {
				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String word = tokens.nextToken();

					if (!StopWords.contains(word)) {
						StopWords.add(word);
					}
				}
			}			
		}

		catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

	}

	public void removeStopWords() {

		
		for (int stopWordsIterator = 0; stopWordsIterator < StopWords.size(); stopWordsIterator++) {
			if (WordWeight.containsKey(StopWords.get(stopWordsIterator))) {
				WordWeight.remove(StopWords.get(stopWordsIterator));
			}
		}
	}

	public ArrayList<String> createVocabForDoc(File fileName,
			int stopWordsIndicator) {

		VocabOfADocument.clear();
		UtilityFunctions uf = new UtilityFunctions();
		try {

			FileReader file = new FileReader(fileName);
			BufferedReader br = new BufferedReader(file);

			String line = null;

			while ((line = br.readLine()) != null) {

				StringTokenizer tokens = uf.readALine(line.toLowerCase());

				while (tokens.hasMoreTokens()) {
					String word = tokens.nextToken();

					if (!VocabOfADocument.contains(word)
							&& (0 == stopWordsIndicator)) {
						VocabOfADocument.add(word);
					}

					if (1 == stopWordsIndicator) {
						if (!VocabOfADocument.contains(word)
								&& (!StopWords.contains(word))) {
							VocabOfADocument.add(word);
						}
					}
				}

			}

		} catch (FileNotFoundException fnfe) {
			fnfe.getMessage();

		} catch (IOException ioe) {
			ioe.getMessage();
		}

		return VocabOfADocument;
	}

	public int getTotalDocCount(File[] hamFileList, File[] spamFileList) {
		return hamFileList.length + spamFileList.length;
	}

}
