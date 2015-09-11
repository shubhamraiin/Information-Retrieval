/* Assignment 1,Part 2: Stemming
 * By Shubham Rai
 * Net id: scr130130
 */

/*This class contains the main function, it takes as input the directory path to the Cranfield folder 
 * and calls the Tokenizer class to stem  the text and displays the output in the console.

 */

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;

public class Main {

	public static String PathDirectory;

	public static void main(String[] args) throws Exception {

		if (args.length >= 1) {
			PathDirectory = args[0];
		} else {
			System.out.println("Enter Path of Directory = ");
			@SuppressWarnings("resource")
			Scanner scn = new Scanner(System.in);
			PathDirectory = scn.nextLine();
		}

		// Tokenize words, in this case, its stemming the text based on the tokens that are recognized
		Tokenizer tokenizer = new Tokenizer(PathDirectory);
		long startTime = System.currentTimeMillis();
		Map<String, Integer> data = tokenizer.buildList();
		long endTime = System.currentTimeMillis();

		long noOfTokens = 0, noOfWords = 0, singleFreqWords = 0;
		for (Entry<String, Integer> entry : data.entrySet()) {
			noOfTokens += entry.getValue();
			noOfWords++;
			if (entry.getValue() == 1) {
				singleFreqWords++;
			}
		}
		// Display the output
		System.out.println("1. Time Taken = " + (endTime - startTime)
				+ " milliseconds");
		System.out.println("2. The number of Stems = " + noOfTokens);
		System.out.println("3. The number of unique Stems = " + noOfWords);
		System.out.println("4. The number of stem that occur only once = "
				+ singleFreqWords);
		System.out
				.println("5. The average number of word stems per document = "
						+ (noOfTokens / tokenizer.getNoOfDocs()));
		System.out
				.println("6. The 30 most frequent  stem words and their frequencies");
		// Tree set for sorting the tokens in descending order.
		TreeSet<Entry<String, Integer>> sortedMap = new TreeSet<Entry<String, Integer>>(
				new ValueComparator());
		sortedMap.addAll(data.entrySet());
		Iterator<Entry<String, Integer>> iterator = sortedMap
				.descendingIterator();
		for (int i = 0; i < 30 && iterator.hasNext(); i++) {
			System.out.println(iterator.next());
		}

	}

}

class ValueComparator implements Comparator<Entry<String, Integer>> {

	public int compare(Entry<String, Integer> o1, Entry<String, Integer> o2) {
		// this comparator imposes orderings that are inconsistent with equals
		if (o1.getValue() < o2.getValue()) {
			return -1;
		}
		return 1;
	}
}
