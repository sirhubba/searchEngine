import java.io.*; //File, IOException
import java.util.*; //Arrays, List, ArrayList, Scanner, Map, HashMap, TreeMap

public class SearchApp{

  public static void main(String[] args) throws IOException{

    //Add the name of all files in target location to an ArrayList
    File f = new File("textfiles");
    ArrayList<String> textFiles = new ArrayList<String>(Arrays.asList(f.list()));

    //Read a word from System.in with scanner and store it as the query
    Scanner sc = new Scanner(System.in);
    String query = sc.next();

    //Create a map where we can store a document as a key and a map with all
    //words and counts as a value pair.
    Map<String, Map<String, Integer>> docToWordCount = new TreeMap<>();
    //Variables needed to calculate IDFscore
    int termTotalFreq = 0;
    int totalDocs = textFiles.size();

    for (String s : textFiles) {
      //Create a String containing the path to the current text file we want to
      //work with
      StringBuilder filePath = new StringBuilder();
      filePath.append("textfiles/");
      filePath.append(s);

      //The nested map of docToWordCount containing words as keys and the amount
      //of times it appears in the document as value pair
      Map<String, Integer> frequencies = new TreeMap<>();

      //Use the current file path to read words from document with a scanner
      File file = new File(filePath.toString());
      Scanner input = new Scanner(file);

      while(input.hasNext()){
        String word = input.next();
        //Purge the word of unwanted characters and replace upper case chars
        //with lower case
        word = word.toLowerCase();
        word = word.replaceAll("[^a-zA-Z0-9]", "");
        //Check how frequent the usage of current word is in the document
        Integer frequency = frequencies.get(word);
        if(frequency == null){ //word didn't already exist in map
          frequency = 0;
          if(word.equals(query)){
            termTotalFreq++;
          }
        }
        //Add the word and frequency (+ 1) of it to the map
        frequencies.put(word, frequency + 1);
      }
      docToWordCount.put("Document" + Integer.toString(textFiles.indexOf(s) + 1), frequencies);
    }

    //Divide the total amount of docs with the amount of documents containing
    //the term and calculate log of that to get the IDF score
    double IDFscore = Math.log((double) totalDocs / termTotalFreq);
    double TFscore = 0;
    //double TFIDFscore = 0;
    Map<String, Double> docToScore = new HashMap<String, Double>();

    //Loop through all the documents (m)
    for (Map.Entry<String, Map<String, Integer>> m : docToWordCount.entrySet()){
      int termFreq = 0;
      int docSize = 0;
      double TFIDFscore = 0;

      //Check which entries of docToWordCount that contains the search term
      if(m.getValue().containsKey(query)){
        //termTotalFreq += 1;
        for (Map.Entry<String, Integer> m1 : m.getValue().entrySet()) {
          if(m1.getKey().equals(query)){
            termFreq += m1.getValue();
          } else {
            docSize += m1.getValue();
          }
        }
        //Divide the amount of times the search term is used in this doc by the
        //total amount of words in this doc to determine how big % of words it is
        TFscore = (double)termFreq / docSize;
        //take TFscore times IDFscore to find the TFIDFscore
        TFIDFscore = TFscore * IDFscore;
        docToScore.put(m.getKey(), TFIDFscore);
        TFscore = 0;
      }
    }
    //System.out.println(TFIDFscore);
    System.out.println(docToScore);
  }
}
