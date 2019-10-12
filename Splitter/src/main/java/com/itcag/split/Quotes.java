package com.itcag.split;

import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

public final class Quotes {
    
    public final static void remove(StringBuilder input) {

        if (TextToolbox.isEmpty(input)) return;

        int i = 0;
        while (i < input.length()) {

            if (input.charAt(i) == 39) {

                Character previous = null;
                Character next = null;
                
                if (i - 1 > -1) previous = input.charAt(i - 1);
                if (i + 1 < input.length()) next = input.charAt(i + 1);
                
                /**
                 * Apostrophe is the first character.
                 */
                if (previous == null) {
                    input.deleteCharAt(i);
                    continue;
                }

                /**
                 * Apostrophe preceded by empty space.
                 */
                if (previous == 32) {
                    input.deleteCharAt(i);
                    continue;
                }

                /**
                 * Apostrophe is the last character,
                 * and it is not preceded by "s",
                 * so that it cannot be a Saxon genitive for plural,
                 * or by a digit, so that it is not a measuring unit (a foot).
                 */
                if (next == null && !previous.toString().equalsIgnoreCase("s") && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }
                
                /**
                 * Apostrophe followed by an empty space,
                 * and not preceded by "s",
                 * so that it cannot be a Saxon genitive for plural
                 * or by a digit, so that it is not a measuring unit (a foot).
                 */
                if (next == 32 && !previous.toString().equalsIgnoreCase("s") && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }
                
                /**
                 * Apostrophe followed by punctuation,
                 * and not preceded by "s",
                 * so that it cannot be a Saxon genitive for plural
                 * or by a digit, so that it is not a measuring unit (a foot).
                 */
                if (PunctuationToolbox.isPunctuation(next) && !previous.toString().equalsIgnoreCase("s") && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }

            } else if (input.charAt(i) == 34) {

                Character previous = null;
                Character next = null;
                
                if (i - 1 > -1) previous = input.charAt(i - 1);
                if (i + 1 < input.length()) next = input.charAt(i + 1);
                
                /**
                 * Quote is the first character.
                 */
                if (previous == null) {
                    input.deleteCharAt(i);
                    continue;
                }

                /**
                 * Quote preceded by empty space.
                 */
                if (previous == 32) {
                    input.deleteCharAt(i);
                    continue;
                }

                /**
                 * Quote is the last character,
                 * and it is not preceded by a digit,
                 * so that it is not a measuring unit (an inch).
                 */
                if (next == null && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }
                
                /**
                 * Quote followed by an empty space,
                 * and it is not preceded by a digit,
                 * so that it is not a measuring unit (an inch).
                 */
                if (next == 32 && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }
                
                /**
                 * Quote followed by punctuation,
                 * and it is not preceded by a digit,
                 * so that it is not a measuring unit (an inch).
                 */
                if (PunctuationToolbox.isPunctuation(next) && !Character.isDigit(previous)) {
                    input.deleteCharAt(i);
                    continue;
                }
                
            }

            i++;
            
        }

        TextToolbox.trim(input);

    }
    
}
