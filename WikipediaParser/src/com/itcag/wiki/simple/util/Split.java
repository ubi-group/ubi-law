package com.itcag.wiki.simple.util;

import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

public final class Split {
    
    public final ArrayList<String> split(String input) {

        if (TextToolbox.isReallyEmpty(input)) return new ArrayList<>();
        input = input.trim();

        ArrayList<String> retVal = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        StringBuilder lastWord = new StringBuilder();
        
        char p;
        char n;
        
        int i = 0;
        while (i < input.length()) {

            char c = input.charAt(i);

            if (i > 0) {
                p = input.charAt(i - 1);
            } else {
                p = 0;
            }

            if ((i + 1) < input.length()) {
                n = input.charAt(i + 1);
            } else {
                n = 0;
            }

            switch (c) {
                case 32:
                    
                    /*
                     * First character is empty space - ignore it.
                     */
                    if (p == 0) continue;
                    
                    /*
                     * Last character is empty space - ignore it.
                     * If buffer is not empty,
                     * it will be inserted into the return list by default.
                     */
                    if (n == 0) continue;
                    
                    if (isEndOfSentence(p, n, lastWord) && buffer.length() > 0) {
                        retVal.add(buffer.toString());
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(c);
                    }
                    
                    lastWord = new StringBuilder();

                    break;

                case 9:
                case 10:
                case 13:
                    /*
                     * Split on line break.
                     */
                    if (buffer.length() > 0) {
                        retVal.add(buffer.toString());
                        buffer = new StringBuilder();
                        lastWord = new StringBuilder();
                    }
                    break;
                default:
                    buffer.append(c);
                    lastWord.append(c);
                    break;
            }
             
            i++;
            
        }

        if (buffer.length() > 0) retVal.add(buffer.toString());
        
        return retVal;
    
    }
    
    private boolean isEndOfSentence(char p, char n, StringBuilder lastWord) {

        /**
         * Do not split after a single letter word.
         */
        if (lastWord.length() == 1) return false;
        
        return PunctuationToolbox.isTerminalPunctuation(Character.toString(p));

    }
    
}
