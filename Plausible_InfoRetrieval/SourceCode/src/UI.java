import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class RetrievalFunctions {
	
	public HashMap<String, String> DocID_URL_Pair = null;
	public HashMap<String, String> DocID_Title_Pair = null;
	public String DocID_URL_TextFile_Path = "DOC_ID_URL_Pair_3.txt";
	public String DocID_Title_TextFile_Path = "DOC_ID_Title_Pair_3.txt";
	public String firedQuery = "";
	
	public HashMap<String, Integer> collectionOfTagsAndStopWords = new HashMap<>();
	public static HashMap<String,Double> DOCID_Score_Pair = new HashMap<String,Double>();
	
	/*This constructor is used to run the initial tasks like reading the hashmaps from the files*/
	public RetrievalFunctions(){
		
		DocID_URL_Pair = ReadHashMapFromFile(DocID_URL_TextFile_Path);
		DocID_Title_Pair = ReadHashMapFromFile(DocID_Title_TextFile_Path);
		Create_Stopwords_Collection();
	}
	
	public void SetQuery(String firedQueryFromMain){
		firedQuery=firedQueryFromMain;
	}
	/*Obtain the DOCID_Score_Pair so that we can build the relevance*/
	public void Get_DOCID_Score_Pair(){
		DOCID_Score_Pair = Searcher.return_DOCID_Score_Pair();
	}

	
	/*This method is used to create a collection of stop words*/
	public void Create_Stopwords_Collection(){
			// We will remove stop words
		
		HashMap<String, Integer> collectionOfTagsAndStopWords = new HashMap<>();
		String[] tagsAndStopWords = { "a", "all", "an", "and", "any",
				"are", "as", "be", "been", "but", "by", "few", "for",
				"have", "he", "her", "here", "him", "his", "how", "i",
				"in", "is", "it", "its", "many", "me", "my", "none", "of",
				"on", "or", "our", "she", "some", "the", "their", "them",
				"there", "they", "that", "this", "us", "was", "what",
				"when", "where", "which", "who", "why", "will", "with",
				"you", "your", "doc", "docno", "title", "author", "biblio",
				"text" };

		for (int ir = 0; ir < tagsAndStopWords.length; ir++) {
			collectionOfTagsAndStopWords.put(tagsAndStopWords[ir], 1);
		}
	}
	
	
	/*This method is used to calculate the relevance */
	public Integer CalculateRelevance(String firedQuery,String title){
					
		//Now we will remove stop words from the query
		String[] splitQuery = firedQuery.split("\\s+");
		String[] splitTitle = title.split("\\s+");
		Integer Relevance = 0;
		
		//We will create a HashMap containing the each world of the title as key, so that we can have access in constant time
		HashMap<String,Integer> hashMap_Title_Word_Value_Pair = new HashMap<String,Integer>();
		
		//First remove stop words from query as well title
		ArrayList<String> queryWithoutStopWords = new ArrayList<String>() ;
		
		
		for(int iw=0; iw < splitQuery.length; iw++){
			
			if(!collectionOfTagsAndStopWords.containsKey(splitQuery[iw])){
				queryWithoutStopWords.add(splitQuery[iw]);
			}
		}
		
		for(int iw=0; iw < splitTitle.length; iw++){
			
			if(!collectionOfTagsAndStopWords.containsKey(splitTitle[iw])){
				hashMap_Title_Word_Value_Pair.put(splitTitle[iw], 1);
			}
		}
		
		
		//Once, we have removed the stop words, we will calculate the relevance of the query with title of the document
		for(int iy = 0; iy < queryWithoutStopWords.size(); iy++){
			
			if(hashMap_Title_Word_Value_Pair.containsKey(queryWithoutStopWords.get(iy))){
				Relevance++;
			}
			
		}
		return Relevance;
		
	}
	
	
	
	/*Obtain documents using Apache Lucene*/
	public ArrayList<String> RetrieveDocumentsForQuery(String firedQuery) {
		
		
		ArrayList<String> listOfRetrievedDocuments = new ArrayList<String>();
		listOfRetrievedDocuments = Searcher.SearchIndex(firedQuery);
		Get_DOCID_Score_Pair();
		
		/*for (int ir = 0; ir < listOfRetrievedDocuments.size(); ir++) {
			System.out.println("Result - " + listOfRetrievedDocuments.get(ir));
		}*/
		return listOfRetrievedDocuments;
		

	}
	
	
	
	/*This method is used to sort the records in the decreasing order of their relevance*/
	@SuppressWarnings("unchecked")
	public TreeMap<String,Double> Sort_Records_InDecreasing_OrderOfWeights(TreeMap<String,Double> sort_On_Weights){
		TreeMap<String, Double> temp_HashMap_1 = new TreeMap();
		int Rank = 0;
		String DOC_ID = "";
		String HeadLine = "";
		String URL = "";
		Double Score;
		
		temp_HashMap_1 = sort_On_Weights;
		
		@SuppressWarnings("rawtypes")
		TreeMap<String, Double> sortedTokens = new TreeMap(new Custom_Comparator(temp_HashMap_1));
		sortedTokens.putAll(temp_HashMap_1);
		
		
		//Print the results
		int index = 0;
		
		//System.out.println(String.format("%-5s %-25s %-85s %-125s %-35s", "Rank","Doc ID", "HeadLine", "URL","Score"));
		
		/* for(Map.Entry<String, Double> entry : sortedTokens.entrySet()){
		      	
	    	 if(index <10){
	    		 
	    		//(DOCID~URL~TITLE, Score)
	    		 Rank = index+1;
	    		 DOC_ID = entry.getKey().split("~")[0];
	    		 URL = entry.getKey().split("~")[1];
	    		 HeadLine = entry.getKey().split("~")[2];
	    		 Score = entry.getValue();
	    		 
	    		 System.out.println(String.format("%-5s %-25s %-85s %-125s %-35s",
							Rank, DOC_ID, HeadLine, URL,Score));
	      		//System.out.println(entry.getKey() + "\t\t" + entry.getValue());
	      		//System.out.println(index);
	      		index++;
	      	}
	    	 else{
	    		 break;
	    	 }*/
	
		return sortedTokens;
		
		
	}
	
	
	/*Obtain URL for each retrieved document*/
	public TreeMap<String, Double> GetURLForDocID(ArrayList<String> listOfRetrievedDocuments){
		Integer relevance = 0;
		String retrievedURL = "";
		String docID = "";
		String title = "";
		Double score;
		String keyForSorting = "";
		
		//This hashMap is used to obtain topmost relevant documents
		TreeMap<String,Double> sort_On_Weights = new TreeMap<String,Double>();
		
		for(int ix = 0; ix <listOfRetrievedDocuments.size(); ix++){
			
			docID = listOfRetrievedDocuments.get(ix);
			if(DocID_URL_Pair.containsKey(docID)){
				if(DocID_Title_Pair.containsKey(docID)){
					if(DOCID_Score_Pair.containsKey(docID)){
						title = 	DocID_Title_Pair.get(docID);
						retrievedURL = DocID_URL_Pair.get(docID);
						//Now, calculate the relevance
						relevance = CalculateRelevance(firedQuery,title);
						//Add relevant factor
						score = DOCID_Score_Pair.get(docID) + relevance;
						//Now we want the top 10 most relevant documents. For this we will sort the results
						//Sorting, we want a hashmap of form <String,Double> form, so
						//we will form key-value pair as 
						//(DOCID~URL~TITLE, Score)
						keyForSorting = docID + "~" + retrievedURL + "~" + title;
						
						sort_On_Weights.put(keyForSorting, score);
						
						
						//System.out.println(" Score : " + score +" -DOC ID - " + docID + "--URL : " +retrievedURL + "--Title" + title );
					}
					
				}
				
				
			}
		}
		
		
		TreeMap<String, Double> result = Sort_Records_InDecreasing_OrderOfWeights(sort_On_Weights);
		
		return result;
		
	}
	
	
	
	/*This method is used to read the hashmap present in the binary file*/
	@SuppressWarnings("unchecked")
	public HashMap<String, String> ReadHashMapFromFile(String pathOfHashMap){
		
		 HashMap<String, String> DocID_URL_Pair = null;
	      try
	      {
	         FileInputStream fis = new FileInputStream(pathOfHashMap);
	         ObjectInputStream ois = new ObjectInputStream(fis);
	         DocID_URL_Pair = (HashMap) ois.readObject();
	         ois.close();
	         fis.close();
	      }catch(Exception ioe)
	      {
	         ioe.printStackTrace();
	      }
	      	      
	      /*for (Map.Entry<String, String> entry : DocID_URL_Pair.entrySet()) {
			System.out.println("Doc ID- " + entry.getKey()+ " URL - " + entry.getValue());
		}*/
	return DocID_URL_Pair;
	}
	

}

public class UI {

	public static void main(String args[]) {
		
		/*This section is used to find the documents relevant to the query
		
		RetrievalFunctions callRetrievalFunction = new RetrievalFunctions();
		String firedQuery = "companies bailed out by government";
		callRetrievalFunction.SetQuery(firedQuery);
		ArrayList<String> listOfRetrievedDocuments = callRetrievalFunction.RetrieveDocumentsForQuery(firedQuery);
		
		/*Now obtain URLs and Title for each retrieved document
		callRetrievalFunction.GetURLForDocID(listOfRetrievedDocuments);*/

	}

}
