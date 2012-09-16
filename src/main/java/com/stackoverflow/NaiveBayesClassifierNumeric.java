package com.stackoverflow;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;

import com.stackoverflow.utils.CsvReader;

public class NaiveBayesClassifierNumeric {
    
    private String[] featureNames;
    private String parameterName;
    
    protected Map<String, Map<String, Double>> means, variances;
    
    public NaiveBayesClassifierNumeric(String[] features, String parameter) {
        featureNames = features;
        parameterName = parameter;
        
        means = new HashMap<String, Map<String, Double>>();
        variances = new HashMap<String, Map<String, Double>>();
    }
    
    public void train(File trainingFile) throws IOException {
        CsvReader reader = new CsvReader(trainingFile);
        int counter = 0;
        for (Map<String, String> line : reader) {
            for (String featureName : featureNames) {
                
                if(!line.containsKey(featureName)) {
                    //TODO: Better error message
                    throw new IOException("Training File does not contain expected data");
                }
                double feature = Double.parseDouble(line.get(featureName));
                String parameter = line.get(parameterName);
                
                Map<String, Double> means;
                if(!means.containsKey(parameter)) {
                    means = newMeansMap();
                    means.put(parameter, means);   
                } else {
                    means = means.get(parameter);
                }
                // avg = (avg(n-1) * (n -1)) + Xn / n               avg = sum(Xi) / n | i=1..n
                double newMean = (means.get(featureName) * counter + feature) / (counter + 1);
                means.put(featureName, newMean);
            }
            ++counter;
        }
    }
    
    private Map<String, Double> newMeansMap() {
        
        Map<String, Double> means = new HashMap<String, Double>();
        for(String featureName : featureNames) {
            means.put(featureName, 0.0);   
        }
        
        return means;
    }
}