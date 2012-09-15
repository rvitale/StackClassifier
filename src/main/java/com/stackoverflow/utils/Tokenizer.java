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
    private static final String IN_WORD_SEPARATORS = "-'";
    private static final int MINIMUM_LENGTH = 2;
    	
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
			if(isSeparator(i, rawText)) {
                // If the word is shorter than MINIMUM_LENGTH
                // characters it is ignored.
                if (i - currentOffset < MINIMUM_LENGTH) {                    
                    String word;
    				if (caseSensitive) {
    					word = rawText.substring(currentOffset, i);
    				} else {
    					word = rawText.substring(currentOffset, i).toLowerCase();
    				}
                    
                    //TODO: add stopwords
                    words.add(word);
                }
                
				while (i < rawText.length() && isSeparator(i, rawText)) {
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
    protected static boolean isSeparator(String text, int pos) {
        char c = text.charAt(pos);
        if (IN_WORD_SEPARATOR.indexOf(c) != - 1) {
            if (pos != 0 || pos != text.length() - 1) {
                return isCharacter(text.charAt(pos - 1)) && isCharacter(text.charAt(pos + 1));
            } else {
                return false;
            }
        } else {
            return isCharacter(c);   
        }
    }
	
    protected static boolean isCharacter(char c) {
        return (c < 65 || c > 90) && (c < 97 || c > 122);
    }
	
}
