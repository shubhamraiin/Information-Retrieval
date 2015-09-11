import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopDocsCollector;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class Searcher {
	
	public static String INDEX_DIRECTORY = "N://IR project//IR_Part3_Index";
	public static HashMap<String,Double> DOCID_Score_Pair = new HashMap<String,Double>();
	
	@SuppressWarnings("deprecation")
	public static ArrayList<String> SearchIndex(String Word_Searched){
		ArrayList<String> listOfRetrievedDocuments = new ArrayList<String>();
		
		try {
			String FIELD_CONTENTS = "content";
			
			IndexReader reader = IndexReader.open(FSDirectory.open(new File(INDEX_DIRECTORY)));
			
			//IndexSearcher searcher = new IndexSearcher(reader);
			IndexSearcher searcher = new IndexSearcher(reader);
			
			//Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_34);
			 SnowballAnalyzer analyzer = new SnowballAnalyzer(Version.LUCENE_34, "English");
			
			QueryParser queryParser = new QueryParser(Version.LUCENE_34,FIELD_CONTENTS, analyzer);
			Query query = queryParser.parse(Word_Searched);
			
			TopDocs hits = searcher.search(query, 100);
			ScoreDoc[] document = hits.scoreDocs;
			
					
			//System.out.println("Total no of hits according to content: " + hits.totalHits);
			
			
			
			for(int i = 0;i <document.length;i++)
			{                 
				Document doc = searcher.doc(document[i].doc);     
				
				float score = document[i].score;
				
				String filename= doc.get("filename");
						
				//System.out.println("Doc ID - " + filename + "  Score - " + (double) score);
				DOCID_Score_Pair.put(filename, (double) score);
				
				listOfRetrievedDocuments.add(filename);			
				//System.out.println(filename);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfRetrievedDocuments;
	}
	
	
	public static HashMap<String,Double> return_DOCID_Score_Pair(){
		return DOCID_Score_Pair;
	}
/*public static void main (String args[]){
	new Searcher();
	Searcher.SearchIndex("companies bailed out by government"); 
}*/
}
