import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class Query {
	Map<String, BuildIndex.DictionaryEntry> dictionary;
	Set<String> stopWords;
	Tokenizer token;
	Map<Integer, BuildIndex.DocumentInfo> documentDetails;
	double avgDoclength;

	public Query(Map<String, BuildIndex.DictionaryEntry> index,
			Map<Integer, BuildIndex.DocumentInfo> documentDetails,
			Set<String> stopWords, int avgDoclength) throws Exception {
		this.dictionary = index;
		this.stopWords = stopWords;
		this.documentDetails = documentDetails;
		this.token = new Tokenizer();
		this.avgDoclength = avgDoclength;
	}

	@SuppressWarnings("unused")
	public void process(String query) throws Exception {
		Map<String, Integer> termFreqTable = token.termFreqTable(query);
		removeStopWords(termFreqTable);
		Map<Integer, Double> W1Table = new HashMap<>();
		Map<Integer, Double> W2Table = new HashMap<>();
		int queryLenght = getQueryLenght(termFreqTable);
		int collectionSize = documentDetails.size();
		for (String queryTerm : termFreqTable.keySet()) {
			BuildIndex.DictionaryEntry dictEntry = dictionary.get(queryTerm);
			if (dictEntry == null) {
				continue;
			}

			int docFreq = dictEntry.docFrequency;
			for (BuildIndex.PostingEntry postingEntry : dictEntry.postingList) {
				int termFreq = postingEntry.frequency;
				int maxTermFreq = (int) documentDetails.get(postingEntry.docID).maxFreq;
				int docLenght = documentDetails.get(postingEntry.docID).docLength
						.intValue();

				double w1 = W1(termFreq, maxTermFreq, docFreq, collectionSize);
				double w2 = W2(termFreq, docLenght, avgDoclength, docFreq,
						collectionSize);

				addWeight(W1Table, postingEntry.docID, w1);
				addWeight(W2Table, postingEntry.docID, w2);
			}
		}

		System.out.print("Stemmed Query :- ");
		for (String queryTerm : termFreqTable.keySet()) {
			System.out.print(queryTerm + " ");
		}
		System.out.println();
		System.out.println("\nTop 10 documents according to W1");
		showTopTen(W1Table);
		System.out.println("\nTop 10 documents according to W2");
		showTopTen(W2Table);
	}

	private int getQueryLenght(Map<String, Integer> termFreqTable) {
		int length = 0;
		for (String queryTerm : termFreqTable.keySet()) {
			length += termFreqTable.get(queryTerm);
		}
		return length;
	}

	private void addWeight(Map<Integer, Double> wTable, int docID, double w) {
		if (wTable.get(docID) == null) {
			wTable.put(docID, w);
			return;
		}
		wTable.put(docID, w + wTable.get(docID));
	}

	public double W1(int termFreq, int maxTermFreq, int docFreq,
			int collectionSize) {
		double temp = 0;
		try {
			temp = (0.4 + 0.6 * Math.log(termFreq + 0.5)
					/ Math.log(maxTermFreq + 1.0))
					* (Math.log(collectionSize / docFreq) / Math
							.log(collectionSize));
		} catch (Exception e) {
			temp = 0;
		}
		return temp;
	}

	public double W2(int termFreq, int doclength, double avgDoclength,
			int docFreq, int collectionSize) {
		double temp = 0;
		try {
			temp = (0.4 + 0.6
					* (termFreq / (termFreq + 0.5 + 1.5 * (doclength / avgDoclength)))
					* Math.log(collectionSize / docFreq)
					/ Math.log(collectionSize));
		} catch (Exception e) {
			temp = 0;
		}
		return temp;
	}

	private void removeStopWords(Map<String, Integer> termFreqTable) {
		Iterator<String> iterator = termFreqTable.keySet().iterator();
		while (iterator.hasNext()) {
			if (stopWords.contains(iterator.next())) {
				iterator.remove();
			}
		}
	}

	private void showTopTen(Map<Integer, Double> w1_table) {
		TreeSet<Entry<Integer, Double>> sortedSet = new TreeSet<Entry<Integer, Double>>(
				new ValueComparator());
		sortedSet.addAll(w1_table.entrySet());
		System.out.println("Rank | " + "\t Score   " + "    | " + " DocId"
				+ " | " + "Document Name" + " | " + "Headline");
		Iterator<Entry<Integer, Double>> iterator = sortedSet.iterator();
		for (int i = 0; i < 10 && iterator.hasNext(); i++) {
			Entry<Integer, Double> entry = iterator.next();
			BuildIndex.DocumentInfo documentInfo = documentDetails.get(entry
					.getKey());
			System.out.println((i + 1) + " | " + entry.getValue() + " | "
					+ documentInfo.docId + " | " + documentInfo.docName + " | "
					+ documentInfo.title);
		}

	}

	class ValueComparator implements Comparator<Entry<Integer, Double>> {
		@Override
		public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
			if (o1.getValue() < o2.getValue()) {
				return 1;
			}
			return -1;
		}
	}
}
