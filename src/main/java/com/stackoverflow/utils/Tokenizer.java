package com.stackoverflow.utils;

import java.util.ArrayList;
import java.util.List;


/**
 * Static class providing methods for the tokenization of an input text.
 * 
 * @author Riccardo Vitale (ric.vitale@gmail.com)
 * @author Lorenzo Lucherini (lorenzo.lucherini@gmail.com)
 * 
 */
public class Tokenizer {

    // Small sample of possible word separators.
    private static final String SEPARATORS = " ,.;:-?!()[]{}\"<>\\\t\n\r";
    	
	/**
	 * Tokenizes the text in the instance to produce the words contained in it. 
     * The text is tokenized case sensitively or not, according to the 
     * caseSensitive parameter.
	 *
     * @param rawText the text to tokenize.
     * @param caseSensitive true if the words must be treated s the same word if
     *      they have different cases.
     * 
     * @return a List<String> containing the tokens extracted from the text.
     */
	public static List<String> tokenize(String rawText, boolean caseSensitive) {
        ArrayList<String> words = new ArrayList<String>();
        // currentOffset always points to the start of the last word, that is 
        // the last character that is not a separator.
		int currentOffset = 0;
		for (int i = 0; i < rawText.length(); i++) {
			if(isSeparator(rawText.charAt(i))) {
                String word;
				if (caseSensitive) {
					word = rawText.substring(currentOffset, i);
				} else {
					word = rawText.substring(currentOffset, i).toLowerCase();
				}
                
                words.add(word);
                
				while (i < rawText.length() &&
					   isSeparator(rawText.charAt(i))) {
					i++;
				}
				currentOffset = i;
			}
		}
        
        return words;
	}
    
    /**
     * Tokenizes the text in the instance to produce the words contained in it. 
     * The text is tokenized case insensitively.
	*/
    public static List<String> tokenize(String rawText) {
        return tokenize(rawText, false);
    }
	
	/**
	 * Checks if a char is a word separator.
	 * 
     * @param c the character to check.
	 * @return whether or not the char is a separator.
	 */
	protected static boolean isSeparator(char c) {
		return SEPARATORS.indexOf(c) != -1;
	}
	
}
