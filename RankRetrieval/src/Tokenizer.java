import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Tokenizer {

	private Read read;
	private Map<String, Integer> indexTable;
	private Porter stemmer;

	public Tokenizer() throws Exception {
		read = new Read();
		stemmer = new Porter();
	}

	public Map<String, Integer> buildtermFreqTable(File file) throws Exception {
		indexTable = new HashMap<String, Integer>();
		if (file.isFile()) {
			String plainText = read.parse(file);
			processPlainText(plainText);
		}
		return indexTable;
	}

	public Map<String, Integer> termFreqTable(String plainText)
			throws Exception {
		indexTable = new HashMap<String, Integer>();
		processPlainText(plainText);
		return indexTable;
	}

	private void processPlainText(String plainText) {
		plainText = plainText.replaceAll("[^\\w\\s-']+", " ").toLowerCase();
		Scanner scanner = new Scanner(plainText);
		while (scanner.hasNext()) {
			String token = scanner.next();
			processToken(token);
		}
		scanner.close();
	}

	private void processToken(String token) {
		if (token.endsWith("'s")) {
			addToList(token.replace("'s", ""));
		} else if (token.contains("-")) {
			splitAddToList(token, "-");
		} else if (token.contains("_")) {
			splitAddToList(token, "_");
		} else {
			addToList(token);
		}

	}

	private void splitAddToList(String token, String splitBy) {
		String[] newTokens = token.split(splitBy);
		for (String newToken : newTokens) {
			addToList(newToken);
		}
	}

	private void addToList(String token) {
		token = token.replaceAll("[']+", "");
		if (token.length() > 0)
			token = stemmer.stripAffixes(token);
		if (token.length() > 0) {
			if (indexTable.containsKey(token)) {
				indexTable.put(token, indexTable.get(token) + 1);
			} else {
				indexTable.put(token, 1);
			}
		}
	}
}
