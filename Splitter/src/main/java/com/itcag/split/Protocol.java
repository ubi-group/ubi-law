package com.itcag.split;

import com.itcag.util.punct.Punctuation;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Iterator;

public final class Protocol {
    
    final ArrayList<StringBuilder> execute(StringBuilder input) throws Exception {
        
        if (TextToolbox.isEmpty(input)) throw new SplitterException("Input is empty.");

        /**
         * Standardize Unicode.
         */
        int originalLength = input.length();
        Unicode unicode = new Unicode();
        unicode.standardize(input);
        TextToolbox.trim(input);
        if (input.length() == 0) throw new SplitterException("Input is not in Latin alphabet.");
        if (input.length() < 0.5 * originalLength) throw new SplitterException("Input is mostly not in Latin alphabet.");
        if (TextToolbox.isEmpty(input)) throw new SplitterException("Input is empty after Unicode standardization.");

        /**
         * Replace HTML special characters with the Unicode.
         * Remove HTML tags of any were left by the collection.
         * Break the text on tags that imply text display in a new line. 
         */
        HTMLCleaner html = new HTMLCleaner();
        html.clean(input);
        TextToolbox.trim(input);
        if (TextToolbox.isEmpty(input)) throw new SplitterException("Input is empty after cleaning.");
        
        /**
         * Lock URLs, acronyms, decimal numbers.
         * URLs must be locked before the removal of emoticons (e.g., because of ":/").
         */
        Locker locker = new Locker();
        locker.lock(input);

        /**
         * Normalize punctuation.
         */
        Punctuation.normalize(input);
        TextToolbox.trim(input);
        if (TextToolbox.isEmpty(input)) throw new SplitterException("Input is empty after punctuation normalization.");
        
        /**
         * Split into sentences.
         */
        Split split = new Split();
        ArrayList<StringBuilder> sentences = split.split(input);

        /**
         * Postprocess sentences.
         */
        Iterator<StringBuilder> sentenceIterator = sentences.iterator();
        while (sentenceIterator.hasNext()) {
            
            StringBuilder sentence = sentenceIterator.next();
            
            /**
             * Reinsert punctuation after the split.
             */
            locker.unlock(sentence);

            /**
             * Remove leftovers originating in erroneous punctuation.
             */
            Punctuation.removePunctuationAtBeginning(sentence);

            /**
             * Remove empty sentences.
             */
            TextToolbox.trim(sentence);
            if (sentence.length() == 0) {
                sentenceIterator.remove();
                continue;
            }

            /**
             * Trim extra spaces.
             */
            TextToolbox.trim(sentence);

        }
        
        return sentences;
        
    }
    
    public static void main(String[] args) throws Exception {
   
        StringBuilder text = new StringBuilder("הושתו על המערערבע\"פ 4907/17 אורן חי נחום נ מדינת ישראל [11.3.18] נדחה ערעורו של המערער (הנקרא בפסק הדין מערער 2, שכן נדונו במאוחד ערעורם של שני המורשעים באותו מקרה). ");
        
        Protocol protocol = new Protocol();
        for (StringBuilder sentence : protocol.execute(text)) {
            System.out.println(sentence.toString());
        }
        
    
    }
        
}
