

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.snowball.SnowballAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
 

public class Indexer {
	
   // private  String sourceFilePath ="C://Data//IR//NEWDATA//SourceFiles";   
	private  String sourceFilePath = "C://Users//Kamlesh//Desktop//Classes//IR//Project//Indices//IR_Part1";
   
	// private  String indexFilePath = "C://Data//IR//NEWDATA//Index";   
	 private  String indexFilePath = "C://Users//Kamlesh//Desktop//Classes//IR//Project//Indices//IR_Part1_Index";  
	
	 private IndexWriter writer = null;
    private File indexDirectory = null;

   
    
    
    
    private Indexer() throws FileNotFoundException, CorruptIndexException, IOException {
    	
        try {
            createIndexWriter();
            
            checkFileValidity();
           // System.out.println("Here");
            closeIndexWriter();
           // System.out.println("Total Document Indexed : " + TotalDocumentsIndexed());
        } catch (Exception e) {
            System.out.println("Please try again");
        }
    }

    /**
     * This method is used to create index using Lucene.
     * Stop words are removed automatically
     * 
     */
    private void createIndexWriter() {
        try {
            indexDirectory = new File(indexFilePath);
            if (!indexDirectory.exists()) {
                indexDirectory.mkdir();
            }
            FSDirectory dir = FSDirectory.open(indexDirectory);
           // StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_34);
            SnowballAnalyzer analyzer = new SnowballAnalyzer(Version.LUCENE_34, "English");
            @SuppressWarnings("deprecation")
			
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, analyzer);
            writer = new IndexWriter(dir, config);
        } catch (Exception ex) {
            System.out.println("Problem in index writing");
        }
    }

    private void checkFileValidity() {

        File[] filesToIndex = new File[10]; 
        filesToIndex = new File(sourceFilePath).listFiles();
        int count = 0;
       for (File file : filesToIndex) {
            try {
               
              /*  if (!file.isDirectory()
                        && !file.isHidden()
                        && file.exists()
                        && file.canRead()
                        && file.length() > 0.0
                        && file.isFile()) {*/
                       
                   
                    	indexTextFiles(file);
                    	count++;
                    //System.out.println("File indexing completed for " + file.getAbsolutePath());
               // }
            } catch (Exception e) {
            	e.printStackTrace();
                System.out.println("Error in indexing " + file.getAbsolutePath());
                
            }
        }
       System.out.println("Index built for " + count + " files");
    }

   
    @SuppressWarnings("deprecation")
	private void indexTextFiles(File file) {
        Document doc = new Document();
      //  String Content = Files.readFromFile(file,"ASCII");

        try {
			doc.add(new Field("content", new FileReader(file)));
			//System.out.println("File name - " + file);
			 doc.add(new Field("filename", file.getName(),
		                Field.Store.YES, Field.Index.ANALYZED));
		        doc.add(new Field("fullpath", file.getAbsolutePath(),
		                Field.Store.YES, Field.Index.ANALYZED));
		        if (doc != null) {
		            writer.addDocument(doc);
		        }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
    }

    private int TotalDocumentsIndexed() {
        try {
            @SuppressWarnings("deprecation")
			IndexReader reader = IndexReader.open(FSDirectory.open(indexDirectory));
            
            return reader.maxDoc();
        } catch (Exception ex) {
            System.out.println("Sorry no index found");
        }
        return 0;
    }

    /**
     * closes the IndexWriter
     */
    private void closeIndexWriter() {
        try {
            writer.close();
        } catch (Exception e) {
            System.out.println("Indexer Cannot be closed");
        }
    }

    public static void main(String arg[]) {
        try {
            new Indexer();
            
    		
        } catch (Exception ex) {
            System.out.println("Cannot Start :(");
        }
    }

}
