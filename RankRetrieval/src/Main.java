/* Assignment 3,: Rank Retrieval
 * By Shubham Rai
 * Net id: scr130130
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

public class Main {

	public static String PathDirectory = "/people/cs/s/sanda/cs6322/Cranfield";
	public static String StopDoc = "./stopwords";
	public static String QueryDoc = "/people/cs/s/sanda/cs6322/hw3.queries";

	public static void main(String[] args) throws Exception {

		if (args.length < 2) {
			System.out.println("Enter path of  Cranfield directory ");
			Scanner scanner = new Scanner(System.in);
			PathDirectory = scanner.nextLine();
			System.out.println("Enter path of query file");
			QueryDoc = scanner.nextLine();
			scanner.close();
		} else {
			PathDirectory = args[0];
			QueryDoc = args[1];
		}

		Set<String> stopWords = readStopWords(StopDoc);
		BuildIndex buildIndex = new BuildIndex(stopWords);

		long startTime = System.currentTimeMillis();
		Map<String, BuildIndex.DictionaryEntry> uncompressedIndex = buildIndex
				.buildIndex(PathDirectory);
		long endTime = System.currentTimeMillis();
		int avgDocLenght = buildIndex.getAvgDocLength();

		long lenghtUncompressed = Utility
				.getSizeOfUnCompressedIndex(uncompressedIndex);

		Map<String, Utility.DictionaryEntry> compressedIndex = Utility
				.createCompressedIndex(uncompressedIndex);
		long lenghtCompressed = Utility
				.getSizeOfCompressedIndex(compressedIndex);

		// Display the output
		System.out.println("1. Time required to build index = "
				+ (endTime - startTime) + " milliseconds");
		System.out.println("2. Size of the  uncompressed index = "
				+ lenghtUncompressed + " bytes");
		System.out.println("3. Size of the  compressed index  = "
				+ lenghtCompressed + " bytes");
		System.out.println("4. Number of inverted lists in the index = "
				+ uncompressedIndex.size());
		Query query = new Query(uncompressedIndex, buildIndex.documentDetails,
				stopWords, avgDocLenght);
		List<String> querySet = readQuery(QueryDoc);
		for (int i = 0; i < querySet.size(); i++) {
			System.out.println("\nQuery" + (i + 1) + " :- " + querySet.get(i));
			query.process(querySet.get(i));
		}

	}

	private static List<String> readQuery(String filename) throws Exception {
		String data = new String(
				Files.readAllBytes(new File(filename).toPath()));
		String[] parts = Pattern.compile("[Q0-9:]+").split(data);
		List<String> queries = new ArrayList<>();
		for (String part : parts) {
			String query = part.trim().replaceAll("\\r\\n", " ");
			if (query.length() > 0) {
				queries.add(query);
			}
		}
		return queries;
	}

	private static Set<String> readStopWords(String filename)
			throws FileNotFoundException {
		Set<String> stopWords = new HashSet<>();
		Scanner scanner = new Scanner(new File(filename));
		while (scanner.hasNext()) {
			stopWords.add(scanner.next());
		}
		scanner.close();
		return stopWords;
	}
}
