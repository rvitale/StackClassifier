package com.stackoverflow;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.stackoverflow.utils.CsvReader;

public class NaiveBayesClassifierNumeric {
    
    private String[] featureNames;
    private String parameterName;
    
    protected Map<String, Integer> counters;
    protected Map<String, Map<String, Double>> squares, means, variances;
    
    public NaiveBayesClassifierNumeric(String[] features, String parameter) {
        featureNames = features;
        parameterName = parameter;
        
        counters = new HashMap<String, Integer>();
        
        squares = new HashMap<String, Map<String, Double>>();
        means = new HashMap<String, Map<String, Double>>();
        variances = new HashMap<String, Map<String, Double>>();
    }
    
    public void train(File trainingFile) throws IOException {
        CsvReader reader = new CsvReader(trainingFile);
        for (Map<String, String> line : reader) {
        	String parameter = line.get(parameterName);
        	
        	if(!counters.containsKey(parameter)) {
            	counters.put(parameter, 0);
            }
            int counter = counters.get(parameter);
            
            for (String featureName : featureNames) {
                
                if(!line.containsKey(featureName)) {
                    //TODO: Better error message
                    throw new IOException("Training File does not contain expected data");
                }
                
                if(!means.containsKey(parameter)) {
                    means.put(parameter, newParameterMap());   
                }
                Map<String, Double> parameterMeans = means.get(parameter);

                if(!squares.containsKey(parameter)) {
                    squares.put(parameter, newParameterMap());   
                }
                Map<String, Double> parameterSquares = squares.get(parameter);

                if(!variances.containsKey(parameter)) {
                    variances.put(parameter, newParameterMap());   
                }
                Map<String, Double> parameterVariances = variances.get(parameter);

                double feature = Double.parseDouble(line.get(featureName));
                
                // avg = (avg(n-1) * (n -1)) + Xn / n               avg = sum(Xi) / n | i=1..n
                double newMean = (parameterMeans.get(featureName) * counter + feature) / (counter + 1);
                double newSquare = parameterSquares.get(featureName) + Math.pow(feature, 2);
                double newVariance = newSquare / counter + 1 - Math.pow(newMean, 2);
                
                parameterMeans.put(featureName, newMean);
                parameterSquares.put(featureName, newSquare);
                pprameterVariances.put(featureNames, neVariance);
            }

            counters.put(parameter, counter + 1);
        }
    }
    
    private Map<String, Double> newParameterMap() {
        
        Map<String, Double> map = new HashMap<String, Double>();
        for(String featureName : featureNames) {
            map.put(featureName, 0.0);   
        }
        
        return map;
    }
}