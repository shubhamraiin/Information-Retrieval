import java.io.BufferedReader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

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

//import org.apache.commons.io.FileUtils;
//import org.apache.commons.lang.StringUtils;

class GetContentFromFiles{

	String directoryPath = "C://Users//Kamlesh//Desktop//Classes//IR//Project//Indices//IR_Part1";
	
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



	/*This method is used to retrieve information embedded in the <description>...</description> tags*/
	public void ReadDescriptionFromFile(ArrayList<String> arrayListofFiles,HashMap<String, String> DocID_Description_Pair){
		String document = "";
		String description = "";
		
		String fileName = "";
		
		for (int it = 0; it < arrayListofFiles.size(); it++) {
			
			document = arrayListofFiles.get(it);
			
			// Form fileName as C://Data//IR//NEWDATA//SourceFiles//document"
			fileName = directoryPath + "//" + document;
			description = ProcessDescriptionFromFile(fileName);
			if(description.length()>0) 
				DocID_Description_Pair.put(document, description.trim());
			
		}
	}
	
	
	/*
	 * This method is used process the XML file and retrieve theURL from
	 * <Link>...</Link> tag
	 */
	/*public String ProcessDescriptionFromFile(String fileName) {
		String filterThree = "";
		String filterfour = "";
		try {
			String testHtml = FileUtils.readFileToString(new File(fileName));
			String title = StringUtils.substringBetween(testHtml, "<description>", "</description>");
			String filterOne = title.replaceAll("&lt;.+?&gt;", "");
			String filterTwo = filterOne.replaceAll("&\\w+?;", "");
			filterThree = filterTwo.replaceAll("[^\\x00-\\x7F]", "");
			filterfour = filterThree.replaceAll("#[0-9]+", "");
			return filterThree;
			//System.out.println(filterThree.replaceAll("#+", ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filterfour;
	}*/
	
	
	public String ProcessDescriptionFromFile(String fileName) {
		
		
		
		StringBuilder appendLines = new StringBuilder();
		String originalDescription="";
		String originalWithoutDescription = "";
		String filterOne, filterTwo, filterThree, filterFour = "";
		boolean continueReading = false;

		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(
					fileName));

			String line = bufferedReader.readLine();

			while (line != null) {
				if (line.startsWith("<description>")) {
					appendLines = appendLines.append(line);
					continueReading = true;
				}
				
				if(line.startsWith("</description>")){
					originalDescription = appendLines.toString();
					
					originalWithoutDescription = originalDescription.substring(13, originalDescription.length());
					filterOne = originalWithoutDescription.replaceAll("&lt;.+?&gt;", "");
					filterTwo = filterOne.replaceAll("&\\w+?;", "");
					filterThree = filterTwo.replaceAll("[^\\x00-\\x7F]", "");
					filterFour = filterThree.replaceAll("#[0-9]+", "");
					bufferedReader.close();
					continueReading = false;
					return filterFour;
					
				}
				if (continueReading)
					appendLines = appendLines.append(line);
				
				line = bufferedReader.readLine();

			}// end of while

			bufferedReader.close();

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(fileName);
		}

		// <link>http://mayonaka-isilme.livejournal.com/93873.html
		// now, remove first <link> tag and return value
		return filterFour;
		
		
		
		/*String filterThree = "";
		String filterfour = "";
		try {
			String testHtml = FileUtils.readFileToString(new File(fileName));
			String title = StringUtils.substringBetween(testHtml, "<description>", "</description>");
			String filterOne = title.replaceAll("&lt;.+?&gt;", "");
			String filterTwo = filterOne.replaceAll("&\\w+?;", "");
			filterThree = filterTwo.replaceAll("[^\\x00-\\x7F]", "");
			filterfour = filterThree.replaceAll("#[0-9]+", "");
			return filterThree;
			//System.out.println(filterThree.replaceAll("#+", ""));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return filterfour;*/
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
		
		 HashMap<String, String> DocID_Desc_Pair = null;
	      try
	      {
	         FileInputStream fis = new FileInputStream("C://Users//Kamlesh//Desktop//Classes//IR//Project//Indices//IR_Part_3_Desc//Doc_ID_Description_Pair");
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         DocID_Desc_Pair = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(Exception ioe)
	      {
	         ioe.printStackTrace();
	      }
	      	      
	      int count = 0;
	      
	      for (Map.Entry<String, String> entry : DocID_Desc_Pair.entrySet()) {
	    	  if (count < 10)
			System.out.println("Doc ID- " + entry.getKey()+ " URL - " + entry.getValue());
	    	  count++;
		}
	}
	
}



public class GetDescription_From_Files {

	public static void main (String args[]){
		GetContentFromFiles getContentFromFiles = new GetContentFromFiles();
		
		//ArrayList<String> arrayListofFiles = new ArrayList<>();
		
		
		//arrayListofFiles = getContentFromFiles.CreateListOfFiles();
		//System.out.println("Size - " + arrayListofFiles.size());
		HashMap<String, String> DocID_Description_Pair = new HashMap<String, String>();
		//getContentFromFiles.ReadDescriptionFromFile(arrayListofFiles,DocID_Description_Pair);
		//getContentFromFiles.WriteIndexToBinaryFile(DocID_Description_Pair, "Doc_ID_Description_Pair_1");
		
		//System.out.println(DocID_Description_Pair.size());
		//getContentFromFiles.WriteIndexToBinaryFile(DocID_Description_Pair, "DOC_ID_Description_Pair_3.txt");
		/*for (Map.Entry<String, String> entry : DocID_Description_Pair.entrySet()) {
		System.out.println("Doc ID- " + entry.getKey()+ " Text to NER - " + entry.getValue());
	}*/
		
		getContentFromFiles.ReadHashMapFromFile();
		
	}
}
