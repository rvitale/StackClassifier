package com.stackoverflow;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.stackoverflow.utils.CsvLine;
import com.stackoverflow.utils.CsvReader;
import com.stackoverflow.utils.StackOverflowCsvLine;
import com.stackoverflow.utils.Tokenizer;

class NaiveBayesClassifier {
   
   private Map<String, Integer> openOccurrences;
   private Map<String, Integer> closedOccurrences;
   private Map<String, Double> openProbabilities;
   private int totalSamples;
   private int openedSamples;
   private double openedProbability;
   
   private final static double SMOOTHING_ALPHA = 1.0;
   
   public NaiveBayesClassifier() {
       openOccurrences = new HashMap<String, Integer>();
       closedOccurrences = new HashMap<String, Integer>();
       openProbabilities = new HashMap<String, Double>();
       totalSamples = 0;
       openedSamples = 0;
       openedProbability = 0.0;
    }
   
   private void addWord(Map<String, Integer> occurrences, String word) {
        if (!occurrences.containsKey(word)) {
            occurrences.put(word, 1);
        } else {        
            occurrences.put(word, occurrences.get(word) + 1);
        }
   }
   
   public void train(File trainingFile) throws IOException {
       
       CsvReader<CsvLine> reader = new CsvReader<CsvLine>(trainingFile, CsvLine.class);
       Map<String, Integer> currentMap;
       for (CsvLine line : reader) {
           
           if (line.get("OpenStatus").equals("open")) {
               currentMap = openOccurrences;
               ++openedSamples;
           } else {
               currentMap = closedOccurrences;
           }
           ++totalSamples;
           
           String text = line.get("BodyMarkdown");
           List<String> tokens = Tokenizer.tokenize(text);
           for (String word : tokens) {
               addWord(currentMap, word);
           }
           
           openedProbability = openedSamples / (double) totalSamples;
       }
       
       // openProbabilities contains P(word|opened)
       
       for (String word : openOccurrences.keySet()) {
            double probability;
            if (closedOccurrences.containsKey(word)) {
                int total = openOccurrences.get(word) + closedOccurrences.get(word);
                probability = (openOccurrences.get(word) + SMOOTHING_ALPHA) / (double) (total + (SMOOTHING_ALPHA * 2));
            } else {
                probability = (openOccurrences.get(word) + SMOOTHING_ALPHA) / (double) (openOccurrences.get(word) + (SMOOTHING_ALPHA * 2));
            }
            openProbabilities.put(word, probability);
       }
       
       for (String word : closedOccurrences.keySet()) {
            if (!openOccurrences.containsKey(word)) {
                openProbabilities.put(word,
                    1 - (closedOccurrences.get(word) + SMOOTHING_ALPHA) / (double) (closedOccurrences.get(word) + (SMOOTHING_ALPHA * 2)));   
            }
       }
   }
   
   public String predict(String text) {
        List<String> tokens = Tokenizer.tokenize(text);
        // P'(opened|word) = P(word|opened)P(opened)
        // P'(closed|word) = P(word|closed)P(closed)
        // P(opened|word) = P'(opened|word) / (P'(opened|word) + P'(closed|word))
        
        double openProbTotal = 1.0;
        double closedProbTotal = 1.0;
        
        for(String word : tokens) {
            double openedProb = openProbabilities.get(word) * openedProbability;
            double closedProb = (1 - openProbabilities.get(word)) * (1 - openedProbability);
            
            double normalizer = openedProb + closedProb;
            openedProb /= normalizer;
            closedProb /= normalizer;
            
            openProbTotal *= openedProb;
            closedProbTotal *= closedProb;
        }
        
        //System.out.println("Opened Prob: " + openProbTotal);
        //System.out.println("Closed Prob: " + closedProbTotal);
        
        
        if (openProbTotal > closedProbTotal) {
            return "open";
        } else {
            return "closed";
        }
   }
   
   protected double verifyTrainData() throws URISyntaxException, IOException {
        int correctMatch = 0;
        int numberOfSamples = 0;
        File trainFile = new File(NaiveBayesClassifier.class.getResource("/train-sample.csv").toURI());
        CsvReader<CsvLine> reader = new CsvReader<CsvLine>(trainFile, CsvLine.class);
        for (CsvLine line :  reader) {
            String prediction = predict(line.get("BodyMarkdown"));
            if ((prediction.equals("open") && line.get("OpenStatus").equals("open")) ||
                (!prediction.equals("open") && !line.get("OpenStatus").equals("open"))) {
                ++correctMatch;
            }
            ++numberOfSamples;
        }
        return correctMatch / (double) numberOfSamples;
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
       
       System.out.println(c.verifyTrainData());
   }
}
