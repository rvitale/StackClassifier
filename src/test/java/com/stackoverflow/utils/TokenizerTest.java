package com.stackoverflow.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TokenizerTest {

    @Test
    public void basicTest() {
        String test = "This? is, a\\ test: \tsentence(), \rwHiLe. this; " +
                      "is> anoTher[] one!\n Do< you like{} \"my\" tests?";
        Object[] golden = new Object[]{"this", "is", "a", "test", "sentence",
            "while", "this", "is", "another", "one", "do", "you", "like", "my",
            "tests"};
        Object[] ret = Tokenizer.tokenize(test).toArray();
        for (int i = 0; i < golden.length; ++i) {
        	assertEquals(golden[i], ret[i]);
        }
    }

}
