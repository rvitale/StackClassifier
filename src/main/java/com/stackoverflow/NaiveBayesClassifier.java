package com.stackoverflow;

import com.stackoverflow.utils.CsvReader;

class NaiveBayesClassifier {
   
   private Map<String, Integer> wordCounter;
   
   public NaiveBayesClassifier() {
       wordCounter = new HashMap<String, Integer>();
    }
   
   private void addWord(String word) {
        if (!wordCounter.contains(word)) {
            wordCounter.set(word, 1);
        } else {        
            wordCounter.set(word, wordCounter.get(word) + 1);
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
