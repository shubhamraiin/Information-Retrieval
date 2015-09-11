/* Assignment 2,Part 2: StemmedIndex
 * By Shubham Rai
 * Net id: scr130130
 */


import java.io.*;
import java.util.*;
import java.util.Map.Entry;

public class BuildStemmedIndex {
	static HashMap<Integer, ArrayList<String>> dict = new HashMap<Integer, ArrayList<String>>();
	static HashMap<Integer, String[]> hashTF = new HashMap<Integer, String[]>();
	Hashtable<String, Integer> documentlength = new Hashtable<String, Integer>();
	Hashtable<Integer, String> docLen = new Hashtable<Integer, String>();
	static ArrayList<String> termBag = new ArrayList<String>();

	public static void main(String[] args) {
		
		BuildLemmatizedIndex LemIndexing = new BuildLemmatizedIndex();
		LemIndexing.processFiles();
		
		BuildStemmedIndex wIndexing = new BuildStemmedIndex();
		wIndexing.processFiles();
		wIndexing.findMaxWordForEachDoc();
		

	}

	@SuppressWarnings("rawtypes")
	public void processFiles() {
		try {
			long start = System.currentTimeMillis();
			final File folder = new File("Cranfield");

			Integer count = 0;
			@SuppressWarnings("unused")
			String stop = "";
			ArrayList<String> files = new ArrayList<String>();

			HashMap<String, Integer> hmap = new HashMap<String, Integer>();
			HashMap<String, strn> posting1 = new HashMap<String, strn>();

			Hashtable<String, Integer> hTabCnt = new Hashtable<String, Integer>();
			Hashtable<Integer, Hashtable> wordFreqDoc = new Hashtable<Integer, Hashtable>();

			System.out.println("Processing  stop words ");
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

				@SuppressWarnings("resource")
				BufferedReader bf = new BufferedReader(
						new FileReader(lFiles[j]));
				int tokencount = 0, number = 0;
				documentlength.put(lFiles[j].toString().trim(), number);
				docLen.put(j, lFiles[j].toString().trim());
				stop = "";
				Hashtable<String, Integer> hashTWord = new Hashtable<String, Integer>();

				termBag = new ArrayList<String>();

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

						termBag.add(temp);

						Stem stemobj = new Stem();
						if (!(temp.equals(" ")))
							temp = stemobj.stem(temp);

						if (temp.length() <= 3 || temp.equalsIgnoreCase(" ")
								|| temp.equalsIgnoreCase("  "))
							continue;
						temp = temp.trim();

						if (hashTWord.get(temp) != null) {
							hashTWord.put(temp, hashTWord.get(temp) + 1);
						} else {
							hashTWord.put(temp, 1);
						}

						count++;
						tokencount++;

						int indx = files.indexOf(lFiles[j].toString().trim());
						strn st;
						if (hmap.get(temp) != null) {
							hmap.put(temp, hmap.get(temp) + 1);
							st = posting1.get(temp);
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
							posting1.put(temp, st);

						} else {
							st = new strn();
							hmap.put(temp, 1);

							st.totFreqency++;
							st.totDoc++;

							st.docId.add(indx);
							st.termFreq.add(st.docId.indexOf(indx), 1);
							posting1.put(temp, st);
						}
					}
				}

				if (hashTWord != null) {
					wordFreqDoc.put(files.indexOf(lFiles[j].toString().trim()),
							hashTWord);
				}
				hTabCnt.put(lFiles[j].toString().trim(), tokencount);

				dict.put(j, termBag);
			}
			BuildStemmedIndex obj = new BuildStemmedIndex();
			obj.sizeOfInvertedindex(posting1);

			String[] words = { "reynold", "nasa", "prandtl", "flow", "pressur",
					"boundari", "shock" };

			for (int j = 0; j < words.length; j++) {
				System.out.println();
				System.out.println("\n");
				System.out.println("Term:" + words[j]);
				strn sm = posting1.get(words[j]);
				System.out.println("Total Number of documents (df):"
						+ sm.totDoc);
				System.out.println("Total term frequency (tf):"
						+ sm.totFreqency);
				System.out.println("Inverted List (df,tf):");
				File fileword = new File("word" + j);
				Writer fileword2 = new BufferedWriter(new FileWriter(fileword));

				@SuppressWarnings("unused")
				double b = 0;
				for (int i = 0; i < sm.docId.size(); i++) {
					System.out.print("[");
					fileword2.write("[");
					System.out.print(sm.docId.get(i) + 1);
					fileword2.write(Integer.toString(sm.docId.get(i) + 1));
					System.out.print(",");
					fileword2.write(",");
					System.out.print(sm.termFreq.get(i));
					fileword2.write(Integer.toString(sm.termFreq.get(i)));
					System.out.print("] ");
					fileword2.write("]");
					fileword2.write(" ");
				}

				fileword2.close();
				File f = new File("word" + j);
				System.out.println("");
				System.out.println("Size of the inverted list in bytes: "
						+ f.length());
			}

			long end = System.currentTimeMillis();
			System.out.println("\nTotal processing time: " + (end - start)
					/ 1000 + "sec");
		}

		catch (Exception e) {
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
			File file1 = new File("StemCompressed.txt");
			File file2 = new File("StemUncompresed.txt");
			Writer uncomp = new BufferedWriter(new FileWriter(file2));
			FileOutputStream comp = new FileOutputStream(file1);
			int first = 0, sizeuncomp = 0, sizecomp = 0, count = 0, last = 0;
			String delta = null, gamma = null;

			List<String> e = new ArrayList<String>(posting.keySet());
			for (String s : e) {
				strn getList = posting.get(s);
				int i = 0;
				uncomp.write(s + " " + getList.docId.size());
				BuildStemmedIndex p = new BuildStemmedIndex();
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

			System.out.println("\nSize of inverted list :" + count
					+ "\nUncompressed size :" + sizeuncomp
					+ "\nCompressed size :" + sizecomp);
		}
	}

	public static void findTF() {
		@SuppressWarnings("unused")
		int c = 0;
		Iterator<Entry<Integer, ArrayList<String>>> iterator = dict.entrySet()
				.iterator();
		HashMap<String, Integer> wordcount = new HashMap<String, Integer>();
		ArrayList<String> termBaglocal = new ArrayList<String>();
		while (iterator.hasNext()) {
			wordcount = new HashMap<String, Integer>();
			Map.Entry<Integer, ArrayList<String>> tokenhash = iterator.next();
			termBaglocal = tokenhash.getValue();
			for (String s : termBaglocal) {
				if (wordcount.containsKey(s)) {
					int count = wordcount.get(s);
					count++;
					wordcount.put(s, count);
				} else {
					wordcount.put(s, 1);
				}
			}
			Entry<String, Integer> maxEntry = null;
			for (Entry<String, Integer> entry : wordcount.entrySet()) {
				if (maxEntry == null || entry.getValue() > maxEntry.getValue()) {
					maxEntry = entry;
				}
			}
			String[] maxKeyVal = new String[2];
			maxKeyVal[0] = maxEntry.getKey();
			maxKeyVal[1] = maxEntry.getValue().toString();
			hashTF.put(tokenhash.getKey(), maxKeyVal);
		}
	}

	public void findMaxWordForEachDoc() {
		findTF();
		Iterator<Entry<Integer, String[]>> iterator = hashTF.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<Integer, String[]> tokenhash = iterator.next();
			String[] values = new String[2];
			values = tokenhash.getValue();
			Integer count = documentlength.get(docLen.get(tokenhash.getKey()));
			System.out.println(tokenhash.getKey() + " :" + values[0] + " :"
					+ values[1] + " DocLen: " + count.intValue());
		}
	}
}

class strn {
	List<Integer> docId = new ArrayList<Integer>();
	List<Integer> termFreq = new ArrayList<Integer>();
	int totFreqency = 0, totDoc = 0;
}