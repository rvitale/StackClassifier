package com.stackoverflow;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stackoverflow.utils.CsvReader;
import com.stackoverflow.utils.Tokenizer;

class NaiveBayesClassifier {
   
   private Map<String, Integer> openOccurrences;
   private Map<String, Integer> closedOccurrences;
   
   public NaiveBayesClassifier() {
       openOccurrences = new HashMap<String, Integer>();
       closedOccurrences = new HashMap<String, Integer>();
    }
   
   private void addWord(Map<String, String> occurrences, String word) {
        if (!occurrences.containsKey(word)) {
            occurrences.put(word, 1);
        } else {        
            occurrences.put(word, occurrences.get(word) + 1);
        }
   }
   
   public void train(File trainingFile) {
       
       CsvReader reader = new CsvReader(trainingFile);
       Map<String, String> currentMap;
       for (Map<String, String> line : reader) {
		   currentMap = line.get("openStatus").equals("open") ? openOccurrences : closedOccurrences;
           String text = line.get("BodyMarkdown");
           List<String> tokens = Tokenizer.tokenize(text);
           for (String word : tokens) {
               addWord(currentMap, word);
           }
       }
   }
   
   public static void main(String[] args) {
       NaiveBayesClassifier c = new NaiveBayesClassifier();
       c.train(file);
   }
}
