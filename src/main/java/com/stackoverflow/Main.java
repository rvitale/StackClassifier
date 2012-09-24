package com.stackoverflow;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.stackoverflow.utils.CsvLine;
import com.stackoverflow.utils.CsvReader;
import com.stackoverflow.utils.StackOverflowCsvLine;

public class Main {

    protected static double verifyTrainData(NaiveBayesClassifierNumeric c, File trainFile, String[] features) throws IOException {
        int correctMatch = 0;
        int numberOfSamples = 0;
        CsvReader<StackOverflowCsvLine> reader = new CsvReader<StackOverflowCsvLine>(trainFile, StackOverflowCsvLine.class);
        
        Map<String, Double> currentFeatures = new HashMap<String, Double>();
        for(String feature : features) {
        	currentFeatures.put(feature, 0.0);
        }
        
        
        for (CsvLine line : reader) {
        	
        	for(String feature : features) {
        		currentFeatures.put(feature, Double.parseDouble(line.get(feature)));
        	}
        	
            String prediction = c.predict(currentFeatures);
            if (prediction.equals(line.get("OpenStatus"))) {
                ++correctMatch;
            }
            ++numberOfSamples;
        }
        return correctMatch / (double) numberOfSamples;
    }
	
	public static void main(String[] args) throws IOException {
		File trainFile = new File("../train-sample.csv");
		String[] features = new String[] {"ReputationAtPostCreation","OwnerUndeletedAnswerCountAtPostTime"};
        NaiveBayesClassifierNumeric c = new NaiveBayesClassifierNumeric(
                features,
                "OpenStatus"
            );
        c.train(trainFile);
        
        System.out.println(verifyTrainData(c, trainFile, features));
        
        /* {not constructive={ReputationAtPostCreation=667.6553419758613, OwnerUndeletedAnswerCountAtPostTime=26.060348681269556}, open={ReputationAtPostCreation=657.4547450667332, OwnerUndeletedAnswerCountAtPostTime=25.405825824113094}, too localized={ReputationAtPostCreation=323.10620331276436, OwnerUndeletedAnswerCountAtPostTime=11.488957453718758}, off topic={ReputationAtPostCreation=399.2398745008554, OwnerUndeletedAnswerCountAtPostTime=15.576896748431281}, not a real question={ReputationAtPostCreation=179.0291013024143, OwnerUndeletedAnswerCountAtPostTime=6.399883075124171}}
		 * {not constructive={ReputationAtPostCreation=6610261.152182949, OwnerUndeletedAnswerCountAtPostTime=9222.493803199419}, open={ReputationAtPostCreation=8736423.594387345, OwnerUndeletedAnswerCountAtPostTime=10948.660753968337}, too localized={ReputationAtPostCreation=5512924.080646912, OwnerUndeletedAnswerCountAtPostTime=4732.814804142743}, off topic={ReputationAtPostCreation=3755471.873941912, OwnerUndeletedAnswerCountAtPostTime=5245.778700620847}, not a real question={ReputationAtPostCreation=1692268.95666899, OwnerUndeletedAnswerCountAtPostTime=3143.8272911387107}}
		 */

	}
}
