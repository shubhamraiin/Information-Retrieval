Stemming

This assignments was developed in Ecilpse IDE (java 1.7 sdk) on Windows 8.1 platform using java as a programming language.


Instructions for compiling the program:

1.Go to the specified directory using the command prompt
2.Type: javac Main.java
        javac Read.java
        javac Tokenizer.java
	javac Stemmer.java

Instructions for running the program:

1.The Cranfield folder required as input must be in the same folder as the program or you can enter the path to the Cranfield folder in the command line .
2.Enter the following command:
-java Main Cranfield
         or 
-java Main
then as prompted ,enter the path to the Cranfield folder  


Program Description

1. How long the program took to acquire the text characteristics.
-> The program takes 3 seconds on an average to compute the characteristics.

2. How the program handles:
A. Upper and lower case words (e.g. "People", "people", "Apple", "apple");
-> The program converts all tokens to lowercase. 
B. Words with dashes (e.g. "1996-97", "middle-class", "30-year", "tean-ager")
-> The program splits the  token on dashes .i.e middle-class will be treated      as two separate words middle and class.
C. Possessives (e.g. "sheriff's", "university's")
-> The program removes all "'s" from token
D. Acronyms (e.g., "U.S.", "U.N.")
-> Program removes . and joins the letters of the acronym to become one word. e.g U.S. will become US ,etc. 

3. Major algorithms and data structures.
        - Used  implementation of the Porter stemmer available as open-source (e.g. http://tartarus.org/martin/PorterStemmer/).
	- Used SAXParser to extract text from the file.
	- Used HashMap<String, Integer> for data.
	- For each token, if it's not on the list, add new token to list with count 1, else increament count for that token
	- Used TreeSet to sort and print top 30 words.