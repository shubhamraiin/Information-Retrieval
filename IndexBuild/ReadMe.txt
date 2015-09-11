Build Index

This assignments was developed in Ecilpse IDE (java 1.7 sdk) on Windows 8.1 platform using java as a programming language.

Instructions for compiling the program:
I have provide the script, but am not sure if it will work!

How To Run
$chmod +x compile && ./compile
$chmod +x Index && ./Index 


If it doesn't then do the following:
Include all the libarires from StanfordCoreNLP version 3.4.1 in the project

1.Go to the specified directory using the command prompt
2.Type: javac BuildLemmatizedIndex.java
        javac BuildStemmedIndex.java
        javac Stem.java
        javac StanfordLemmatizer.java
        
Instructions for running the program:

1.The Cranfield folder and the stopword.txt required as input must be in the same folder as the program 
2.Enter the following command:
-java BuildLemmatizedIndex
-java BuildStemmedIndex

Major algorithms and data structures 
        
1. For storage purpose, a  HashMap<String, Integer> is used for both the versions.
2. For lemmatization, Standford-core-NLP libraries and standfordlemmatizer class is used with corrosponding jar files that supports lemmatization for this class(URL:- http://nlp.stanford.edu/software/corenlp.shtml#Download)
3. For index building a static class (which implements serializable interface) of dictionary entry and posting entry are used which keeps a trak of all the info like term, doc-freq and term-freq for both the versions of the index.
4. For data compression delta and gamma encoding is used for both the versions of the index.
5. For stemming Porter stemming algorithm is used.



