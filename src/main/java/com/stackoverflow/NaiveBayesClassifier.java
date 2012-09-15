package com.stackoverflow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stackoverflow.utils.CsvReader;
import com.stackoverflow.utils.Tokenizer;

class NaiveBayesClassifier {
   
   private Map<String, Integer> openOccurrences;
   private Map<String, Integer> closedOccurrences;
   
   public NaiveBayesClassifier() {
       openOccurrences = new HashMap<String, Integer>();
       closedOccurrences = new HashMap<String, Integer>();
    }
   
   private void addWord(Map<String, Integer> occurrences, String word) {
        if (!occurrences.containsKey(word)) {
            occurrences.put(word, 1);
        } else {        
            occurrences.put(word, occurrences.get(word) + 1);
        }
   }
   
   public void train(File trainingFile) throws IOException {
       
       CsvReader reader = new CsvReader(trainingFile);
       Map<String, Integer> currentMap;
       for (Map<String, String> line : reader) {
		   currentMap = line.get("OpenStatus").equals("open") ? openOccurrences : closedOccurrences;
           String text = line.get("BodyMarkdown");
           List<String> tokens = Tokenizer.tokenize(text);
           for (String word : tokens) {
               addWord(currentMap, word);
           }
       }
   }
   
   protected void writeWordsToFile(File wordsFile) throws IOException {
	   FileWriter writer = null;
	   
	   try {
		writer = new FileWriter(wordsFile);
		   Set<String> words = new HashSet<String>();
		   for (String word : openOccurrences.keySet()) {
			   words.add(word);
		   }
		   for (String word : closedOccurrences.keySet()) {
			   words.add(word);
		   }
		   
		   for (String word : words) {
			   writer.write(word);
			   writer.write('\n');
		   }
	} finally {
		if(writer != null) {
			writer.close();
		}
	}
	   
   }
   
   public static void main(String[] args) throws Exception {
       NaiveBayesClassifier c = new NaiveBayesClassifier();
       File trainFile = new File(NaiveBayesClassifier.class.getResource("/train-sample.csv").toURI());
       c.train(trainFile);
       c.writeWordsToFile(new File("words.txt"));
   }
}
