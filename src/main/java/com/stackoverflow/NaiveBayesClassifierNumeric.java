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
    protected Map<String, Map<String, Double>> squaredDifferences, means, variances;
    
    public NaiveBayesClassifierNumeric(String[] features, String parameter) {
        featureNames = features;
        parameterName = parameter;
        
        counters = new HashMap<String, Integer>();
        
        squaredDifferences = new HashMap<String, Map<String, Double>>();
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

                if(!squaredDifferences.containsKey(parameter)) {
                    squaredDifferences.put(parameter, newParameterMap());   
                }
                Map<String, Double> paramSquaredDiffs = squaredDifferences.get(parameter);

                if(!variances.containsKey(parameter)) {
                    variances.put(parameter, newParameterMap());   
                }
                //Map<String, Double> parameterVariances = variances.get(parameter);

                double feature = Double.parseDouble(line.get(featureName));
                
                // avg = (avg(n-1) * (n -1)) + Xn / n               avg = sum(Xi) / n | i=1..n
                //double newMean = (parameterMeans.get(featureName) * counter + feature) / (counter + 1);
                //double newSquare = parameterSquares.get(featureName) + Math.pow(feature, 2);
                
                //if(counter > 1) {
                //	double newVariance = newSquare / (counter) - Math.pow(parameterMeans.get(featureName), 2);
                //	parameterVariances.put(featureName, newVariance);
                //}
                
                double delta = feature - parameterMeans.get(featureName);
                double newMean = parameterMeans.get(featureName) + delta / (counter + 1);
                double m2 = paramSquaredDiffs.get(featureName) + delta * (feature - newMean);
                
                parameterMeans.put(featureName, newMean);
                paramSquaredDiffs.put(featureName, m2);
                
            }

            counters.put(parameter, counter + 1);
        }
        
        for (String parameterName : squaredDifferences.keySet()) {
	        for (String featureName : featureNames) {
	        	double m2 = squaredDifferences.get(parameterName).get(featureName);
	        	double n = counters.get(parameterName);
	        	//double variance_n = m2 / n;
	        	double variance = m2 / (n - 1);
	        	
	        	variances.get(parameterName).put(featureName, variance);
	        }
        }
    }
    
    public String predict(Map<String, Double> features) {
        
        String maxParam = "";
        double maxProb = 0.0;
        
        Set<String> parameterValues = counters.keySet();
        int counterSums = getTotalCounter();
        for (String parameter : parameterValues) {
            double paramPosterior = counter.get(parameter) / counterSums;
                                
            for (String featureName : features.keySet()) {
                paramPosterior *= getFeatureProbability(parameter, featureName);
            }
            
            System.out.println(parameter + ": " + paramPosterior);
            
            if (paramPosterior > maxProb) {
                maxProb = paramPosterior;
                maxParam = parameter;
            }
        }
        
        return maxParam;
    }
    
    private int getTotalCounter() {
        int sum = 0;
        for (int parameterCount : counters.values()) {
            sum += parameterCount;
        }
        return sum;
    }
    
    private Double getFeatureProbability(String parameter, String featureName) {
        double featureProb =
            1 / Math.sqrt(2 * Math.PI * variances.get(parameter).get(featureName));
        double expNumerator = Math.pow(features.get(featureName) -
                                       means.get(parameter).get(featureName), 2);
        featureProb *= Math.pow(
            Math.E, (- expNumerator / (2 * variances.get(parameter).get(featureName))));
            
        return featureProb;
    }
    
    private Map<String, Double> newParameterMap() {
        
        Map<String, Double> map = new HashMap<String, Double>();
        for(String featureName : featureNames) {
            map.put(featureName, 0.0);   
        }
        
        return map;
    }
}