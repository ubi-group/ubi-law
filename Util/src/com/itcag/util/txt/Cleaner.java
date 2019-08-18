package com.itcag.util.txt;

import com.itcag.util.punct.PunctuationToolbox;

public final class Cleaner {
    
    public final static void sanitize(StringBuilder input) {

        if (TextToolbox.isEmpty(input)) return;

        int count = 0;

        int cursor = 0;
        
        while (cursor < input.length()) {
            
            int c = input.charAt(cursor);

            if (!Character.isLetterOrDigit(c) && !Character.isWhitespace(c) && !PunctuationToolbox.isPunctuation((char) c)) {
                count++;
            } else {
                if (count > 2) {
                    cursor = cursor - count;
                    input.delete(cursor, cursor + count);
                    input.insert(cursor, ". ");
                }
                count = 0;
            }

            cursor++;
            
        }
        
        if (count > 2) input.delete(input.length() - count, input.length());
        
    }
    
    public final static void fixWhiteSpaces(StringBuilder input) {
        
        if (TextToolbox.isEmpty(input)) return;

        /*
         * Replace multiple spaces with a single space.
         */
        while (input.indexOf("  ") != -1) {
            TextToolbox.replace(input, "  ", " ");
            if (TextToolbox.isEmpty(input)) break;
        }

        if (TextToolbox.isEmpty(input)) return;

        /*
         * Trim.
         */
        TextToolbox.trim(input);
            
    }

}
