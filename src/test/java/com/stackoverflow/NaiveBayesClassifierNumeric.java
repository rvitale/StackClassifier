package com.stackoverflow;

import org.junit.Test;

class NaiveBayesClassifierNumericTest {
    
    @Test
    public void testMeans() {
        
        File trainFile = new File(getClass().getResource("/sampleSex.csv").toURI());
        NaiveBayesClassifierNumeric c = new NaiveBayesClassifierNumeric(
                new String[] {"Height", "Weight", "FootSize"},
                "Sex"
            );
        c.train(trainFile);
        
        assertEquals(5.855, c.means[0]);
        assertEquals(176.25, c.means[1]);
        assertEquals(11.25, c.means[2]);
        
    }
    
}