package com.stackoverflow;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.stackoverflow.utils.CsvReader;
import com.stackoverflow.utils.Tokenizer;

class NaiveBayesClassifier {
   
   private Map<String, Integer> wordCounter;
   
   public NaiveBayesClassifier() {
       wordCounter = new HashMap<String, Integer>();
    }
   
   private void addWord(String word) {
        if (!wordCounter.containsKey(word)) {
            wordCounter.put(word, 1);
        } else {        
            wordCounter.put(word, wordCounter.get(word) + 1);
        }
   }
   
   public void train(File trainingFile) {
       
       CsvReader reader = new CsvReader(trainingFile);
       for (Map<String, String> line : reader) {
           String text = line.get("BodyMarkdown");
           List<String> tokens = Tokenizer.tokenize(text);
           for (String word : tokens) {
               addWord(word);
           }
       }
   }
   
   public static void main(String[] args) {
       NaiveBayesClassifier c = new NaiveBayesClassifier();
       c.train(file);
       
       
   }
   
    
}
