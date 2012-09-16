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
        
        assertEquals(5.855, c.means[0], 0.0);
        assertEquals(176.25, c.means[1], 0.0);
        assertEquals(11.25, c.means[2], 0.0);
        
    }
    
}