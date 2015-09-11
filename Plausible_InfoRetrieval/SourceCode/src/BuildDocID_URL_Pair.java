import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ReadAllFiles {
	String directoryPath = "C://Users//Kamlesh//Desktop//Classes//IR//Project//Indices//IR_Part3";
	
	ArrayList<String> arrayListofFiles = new ArrayList<>();

	/* This method is used to create list of files in the given folder */
	public ArrayList<String> CreateListOfFiles() {
		File getFiles = new File(directoryPath);
		File[] arrayOfFiles = getFiles.listFiles();

		for (int ix = 0; ix < arrayOfFiles.length; ix++) {

			if (arrayOfFiles[ix].isFile()) {
				arrayListofFiles.add(arrayOfFiles[ix].getName());

			}

		}

		return arrayListofFiles;
	}

	/*
	 * This method is used to create a HashMap which will store (FileName, URL)
	 * pair
	 */
	public HashMap<String, String> CreateDocID_URL_Pair(
			ArrayList<String> arrayListofFiles) {

		String document = "";
		String URL = "";
		HashMap<String, String> DocID_URL_Pair = new HashMap<String, String>();
		
		String fileName = "";

		for (int it = 0; it < arrayListofFiles.size() ; it++) {
			
			document = arrayListofFiles.get(it);
			// Form fileName as C://Data//IR//NEWDATA//SourceFiles//document"
			fileName = directoryPath + "//" + document;
			
			URL = RetrieveURLFromFile(fileName);
			if(URL.length()>0) 
			DocID_URL_Pair.put(document, URL);
		}

		return DocID_URL_Pair;

	}

	/*
	 * This method is used process the XML file and retrieve theURL from
	 * <Link>...</Link> tag
	 */
	public String RetrieveURLFromFile(String fileName) {
		String URL = "";

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));

			String line = bufferedReader.readLine();

			while (line != null) {
				if(line.length()>0){
					if (line.startsWith("<link>")) {
						if(line.length()>0){
							URL = line.substring(0, line.length() - 7);
							//System.out.println(fileName);
							return URL.substring(6, URL.length());
						}
						
					}
				}
				
				line = bufferedReader.readLine();

			}// end of while

			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// <link>http://mayonaka-isilme.livejournal.com/93873.html
		// now, remove first <link> tag and return value
		return URL.substring(6, URL.length());
	}

	
	/*This method is used to write the hashmap in the file*/
	public void WriteIndexToBinaryFile(Object tokenCollection, String fileName) {
		double bytes = 0;
		try {

			File file = new File(fileName);
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(fileName));
			out.writeObject(tokenCollection);
			bytes = file.length();
			out.flush();
			out.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		

	}

	
	/*This method is used to read the hashmap present in the binary file*/
	@SuppressWarnings("unchecked")
	public void ReadHashMapFromFile(){
		
		 HashMap<String, String> DocID_URL_Pair = null;
	      try
	      {
	         FileInputStream fis = new FileInputStream("DOC_ID_URL_Pair_3.txt");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         DocID_URL_Pair = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(Exception ioe)
	      {
	         ioe.printStackTrace();
	      }
	      	      
	      for (Map.Entry<String, String> entry : DocID_URL_Pair.entrySet()) {
			System.out.println("Doc ID- " + entry.getKey()+ " URL - " + entry.getValue());
		}
	}
}

public class BuildDocID_URL_Pair {

	public static void main(String args[]) {
		ReadAllFiles readAllFiles = new ReadAllFiles();
		HashMap<String, String> DocID_URL_Pair = new HashMap<String, String>();

		// First, create list of all files in the directory
		ArrayList<String> arrayListofFiles = readAllFiles.CreateListOfFiles();
		
		// Obtain a HashMap containing (DOC_ID,URL) pair
		DocID_URL_Pair = readAllFiles.CreateDocID_URL_Pair(arrayListofFiles);

		System.out.println("Size - " + DocID_URL_Pair.size());
		readAllFiles.WriteIndexToBinaryFile(DocID_URL_Pair, "DOC_ID_URL_Pair_3.txt");
		//readAllFiles.ReadHashMapFromFile();
		/*for (Map.Entry<String, String> entry : DocID_URL_Pair.entrySet()) {
			System.out.println("Doc ID- " + entry.getKey()+ " Title - " + entry.getValue());
		}*/

	}

}
