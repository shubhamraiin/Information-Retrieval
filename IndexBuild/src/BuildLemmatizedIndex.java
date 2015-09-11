/* Assignment 2,Part 1: Lemmatization
 * By Shubham Rai
 * Net id: scr130130
 */

import java.io.*;
import java.util.*;

public class BuildLemmatizedIndex {
	/*public static void main(String[] args) {
		BuildLemmatizedIndex LemIndexing = new BuildLemmatizedIndex();
		LemIndexing.processFiles();
		//new BuildLemmatizedIndex().processFiles();
	}*/

	@SuppressWarnings({ "unused", "resource" })
	public void processFiles() {
		try {
			long start = System.currentTimeMillis();
			final File folder = new File("Cranfield");
			StanfordLemmatizer lemmatizer = new StanfordLemmatizer();

			Integer count = 0;
			String stop = "";
			ArrayList<String> files = new ArrayList<String>();

			HashMap<String, Integer> hmap = new HashMap<String, Integer>();
			HashMap<String, strn> posting = new HashMap<String, strn>();

			Hashtable<String, Integer> haTab = new Hashtable<String, Integer>();
			@SuppressWarnings("rawtypes")
			Hashtable<Integer, Hashtable> wordFreqDoc = new Hashtable<Integer, Hashtable>();
			Hashtable<String, Integer> documentlength = new Hashtable<String, Integer>();

			System.out.println("Processing stop words ");

			File[] lFiles = folder.listFiles();

			File file = new File("stopwords.txt");

			int ch;
			StringBuffer Stringcontent = new StringBuffer("");
			FileInputStream fin = new FileInputStream(file);
			while ((ch = fin.read()) != -1) {
				Stringcontent.append((char) ch);
			}
			fin.close();

			System.out.println("Processing " + lFiles.length
					+ " input files...");
			int len = lFiles.length;
			for (int k = 0; k < len; k++) {
				files.add(lFiles[k].toString().trim());
			}

			for (int j = 0; j < lFiles.length; j++) {
				String line;
				BufferedReader bf = new BufferedReader(
						new FileReader(lFiles[j]));

				int tokencount = 0, number = 0;
				documentlength.put(lFiles[j].toString().trim(), number);
				stop = "";
				Hashtable<String, Integer> hashTWord = new Hashtable<String, Integer>();
				while ((line = bf.readLine()) != null) {
					line = line.replaceAll("<(.|\n)*?>", " ").trim();

					StringTokenizer token = new StringTokenizer(line, " ");

					while (token.hasMoreTokens()) {
						String temp = token.nextToken(" ").trim();

						temp = temp.toLowerCase();

						if (temp.equals(".") || temp.equals(";")
								|| temp.startsWith("<") || temp.equals(":")
								|| temp.equals(",")) {
							continue;
						}

						if (temp.contains(")") || temp.contains("(")
								|| temp.contains("/")) {
							temp = temp.replace("(", "");
							temp = temp.replace(")", "");
							temp = temp.replace("/", "");
						}

						if (temp.startsWith(",") || temp.endsWith(",")
								|| temp.contains(",") || temp.startsWith(".")
								|| temp.endsWith(".")) {
							temp = temp.replace(",", "");
							temp = temp.replace(".", "");
						}

						if (temp.startsWith(";") || temp.endsWith(";")
								|| temp.startsWith(":") || temp.endsWith(":")
								|| temp.endsWith("\\")) {
							temp = temp.replace(";", "");
							temp = temp.replace("\\", "");
							temp = temp.replace(":", "");
						}

						if (Stringcontent.toString().contains(
								temp.toLowerCase())) {
							documentlength.put(
									lFiles[j].toString().trim(),
									documentlength.get(
											lFiles[j].toString().trim())
											.intValue() + 1);
							temp = "";
							continue;
						}
						documentlength.put(lFiles[j].toString().trim(),
								documentlength.get(lFiles[j].toString().trim())
										.intValue() + 1);

						LinkedList<String> lemmatizedWords = new LinkedList<String>();

						if (!(temp.equals(""))) {
							lemmatizedWords = (LinkedList<String>) lemmatizer
									.lem(temp.toString());
						}

						for (String word : lemmatizedWords) {
							if (word.length() <= 3
									|| word.equalsIgnoreCase(" ")
									|| word.equalsIgnoreCase("  "))
								continue;
							word = temp.trim();

							if (hashTWord.get(word) != null) {
								hashTWord.put(word, hashTWord.get(word) + 1);
							} else {
								hashTWord.put(word, 1);
							}

							count++;
							tokencount++;

							int indx = files.indexOf(lFiles[j].toString()
									.trim());
							strn st;
							if (hmap.get(word) != null) {
								hmap.put(word, hmap.get(word) + 1);
								st = posting.get(word);
								st.totFreqency++;

								if (st.docId.contains(indx)) {
									int value = st.termFreq.get(st.docId
											.indexOf(indx));
									st.termFreq.set(st.docId.indexOf(indx),
											value + 1);
								} else {
									st.docId.add(indx);
									st.termFreq.add(st.docId.indexOf(indx), 1);
									st.totDoc++;
								}
								posting.put(word, st);

							} else {
								st = new strn();
								hmap.put(word, 1);

								st.totFreqency++;
								st.totDoc++;

								st.docId.add(indx);
								st.termFreq.add(st.docId.indexOf(indx), 1);
								posting.put(word, st);
							}
						}
					}
				}

				if (hashTWord != null) {
					wordFreqDoc.put(files.indexOf(lFiles[j].toString().trim()),
							hashTWord);
				}
				haTab.put(lFiles[j].toString().trim(), tokencount);
			}
			BuildLemmatizedIndex obj = new BuildLemmatizedIndex();
			obj.sizeOfInvertedindex(posting);
			long end = System.currentTimeMillis();
			System.out.println("\nTotal processing time: " + (end - start)
					/ 1000 + "sec");
		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		}
	}

	String delta(int num) throws IOException {
		String binary = Integer.toBinaryString(num);
		int len = binary.length();
		String offset = gamma(len);
		return (offset + binary.substring(1, len));
	}

	String gamma(int num) throws IOException {
		String binary = Integer.toBinaryString(num);
		int len = binary.length();
		String offset = binary.substring(1, len);
		String unary = "";
		for (int i = 0; i < offset.length(); i++) {
			unary = unary + "1";
		}
		unary = unary + "0";
		return (unary + offset);
	}

	
	void sizeOfInvertedindex(HashMap<String, strn> posting) throws IOException {
		{
			File file1 = new File("LemmaCompressed.txt");
			File file2 = new File("LemmaUncompressed.txt");
			Writer uncomp = new BufferedWriter(new FileWriter(file2));
			FileOutputStream comp = new FileOutputStream(file1);
			@SuppressWarnings("unused")
			int first = 0, sizeuncomp = 0, sizecomp = 0, count = 0, last = 0;
			String delta = null, gamma = null;

			List<String> e = new ArrayList<String>(posting.keySet());
			for (String s : e) {
				strn getList = posting.get(s);
				int i = 0;
				uncomp.write(s + " " + getList.docId.size());
				BuildLemmatizedIndex p = new BuildLemmatizedIndex();
				count++;

				for (i = 0; i < getList.docId.size(); i++) {
					int value = getList.docId.get(i).intValue();
					String val = getList.docId.get(i).toString();
					int freq = getList.termFreq.get(i).intValue();
					String fq = getList.termFreq.get(i).toString();
					uncomp.write("[" + val + "," + fq + "]");

					sizeuncomp += val.getBytes().length + fq.getBytes().length;
					if (first == 0) {
						delta = p.delta(value);
					} else {
						delta = delta + p.delta(value - last);
					}
					last = value;
					gamma = gamma + p.gamma(freq);
				}

				comp.write(delta.getBytes().length);
				comp.write(gamma.getBytes().length);
				uncomp.write("\n");
			}

			uncomp.write("\n\n" + sizeuncomp);
			sizecomp = gamma.getBytes().length + delta.getBytes().length;
			comp.write(sizecomp);

			uncomp.close();
			comp.close();
			System.out.println(/*"\nSize of inverted list :" + count
					+*/ "\nLemmatized Uncompressed size :" + sizeuncomp
					+ "\nLemmatized Compressed size :" + sizecomp);

		}
	}
}

class clsStr {
	List<Integer> docId = new ArrayList<Integer>();
	List<Integer> termFreq = new ArrayList<Integer>();
	int totFreqency = 0, totDoc = 0;
}