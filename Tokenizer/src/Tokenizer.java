/*
This class tokenizes the text from the cranfield folder as per given conditions in the assignment.
It cleans the text as required. i.e removing tags, converting all the words to lower case,removing Possessives,
splitting words on dashes, removing full stops and combining the acronyms etc.
  

 */

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tokenizer {

	public Map<String, Integer> data;
	private String pathDirectory;
	private int noOfDocs;

	public Tokenizer(String directoryPath) {
		this.pathDirectory = directoryPath;
		this.data = new HashMap<String, Integer>();
		this.noOfDocs = 0;

	}
	//Parsing the files and building the list
	public Map<String, Integer> buildList() throws Exception {
		File directory = new File(pathDirectory);
		Read read = new Read();
		for (File file : directory.listFiles()) {
			if (file.isFile()) {
				noOfDocs++;
				String plainText = read.parse(file);
				plainText = plainText.replaceAll("[^\\w\\s-'.]+", "")
						.toLowerCase();
				Scanner scanner = new Scanner(plainText);
				while (scanner.hasNext()) {
					String token = scanner.next();
					processToken(token);
				}
				scanner.close();
			}
		}
		return data;
	}

	// Processing token based on requirements and stemming the tokens
	private void processToken(String token) {

		if (token.endsWith("'s")) {
			addToList(token.replace("'s", ""));
		} else if (token.contains("-")) {
			splitAddToList(token, "-");
		} else if (token.contains("_")) {
			splitAddToList(token, "_");
		} else if (token.endsWith(".")) {
			addToList(token.replace(".", ""));
		} else {
			addToList(token);
		}
	}

	// Adding word to the list, i.e dictionary
	private void addToList(String token) {
		token = token.replaceAll("['.]+", "");
		if (token.length() > 0) {
			if (data.containsKey(token)) {
				data.put(token, data.get(token) + 1);
			} else {
				data.put(token, 1);
			}
		}
	}

	// Splitting the hyphenated words and adding to the list
	private void splitAddToList(String token, String splitBy) {
		String[] newTokens = token.split(splitBy);
		for (String newToken : newTokens) {
			addToList(newToken);
		}
	}

	// Total no. of documents in cranfield folder
	public int getNoOfDocs() {
		return noOfDocs;
	}

}
