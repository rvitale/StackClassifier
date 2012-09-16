package com.stackoverflow;

import java.io.IOException;
import java.util.Map;

import com.stackoverflow.utils.CsvReader;

public class NaiveBayesClassifierNumeric {
    
    private String[] featureNames;
    private String parameterName;
    
    protected double[] means, variances;
    
    public NaiveBayesClassifierNumeric(String[] features, String parameter) {
        featureNames = features;
        parameterName = parameter;
        
        means = new double[features.length];
        variances = new double[features.length];
    }
    
    public void train(File trainingFile) throws IOException {
        CsvReader reader = new CsvReader(trainingFile);
        int counter = 0;
        for (Map<String, String> line : reader) {
            for (int i = 0; i < featureNames.length; ++i) {
                String featureName = featureNames[i];
                
                if(!line.containsKey(featureName)) {
                    //TODO: Better error message
                    throw new IOException("Training File does not contain expected data");
                }
                double feature = Double.parseDouble(line.get(featureName));
                
                // avg = (avg(n-1) * (n -1)) + Xn / n               abg = sum(Xi) / n | i=1..n
                means[i] = means[i] * counter + feature / (counter + 1);
            }
            ++counter;
        }
    }
    
    
}