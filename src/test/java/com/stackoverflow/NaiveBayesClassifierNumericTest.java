package com.stackoverflow;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

public class NaiveBayesClassifierNumericTest {
    
    @Test
    public void testMeans() throws Exception {
        
        File trainFile = new File(getClass().getResource("/com/stackoverflow/sampleSex.csv").toURI());
        NaiveBayesClassifierNumeric c = new NaiveBayesClassifierNumeric(
                new String[] {"Height", "Weight", "FootSize"},
                "Sex"
            );
        c.train(trainFile);
        
        assertEquals(5.855, c.means.get("male").get("Height"), 0.01);
        assertEquals(176.25, c.means.get("male").get("Weight"), 0.01);
        assertEquals(11.25, c.means.get("male").get("FootSize"), 0.01);
        
        assertEquals(5.4175, c.means.get("female").get("Height"), 0.01);
        assertEquals(132.5, c.means.get("female").get("Weight"), 0.01);
        assertEquals(7.5, c.means.get("female").get("FootSize"), 0.01);
        
        assertEquals(3.5033e-02, c.variances.get("male").get("Height"), 0.01);
        assertEquals(1.2292e+02, c.variances.get("male").get("Weight"), 0.01);
        assertEquals(9.1667e-01, c.variances.get("male").get("FootSize"), 0.01);
        
        assertEquals(9.7225e-02, c.variances.get("female").get("Height"), 0.01);
        assertEquals(5.5833e+02, c.variances.get("female").get("Weight"), 0.01);
        assertEquals(1.6667e+00, c.variances.get("female").get("FootSize"), 0.01);
        
    }
    
}