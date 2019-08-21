package scraper;

import com.itcag.util.txt.TextToolbox;

import java.util.HashMap;
import java.util.HashSet;

public final class PunctuationToolbox {
    
    private final static HashSet<String> SIGNS = new HashSet<>();
    private final static HashSet<Character> CHARS = new HashSet<>();
    private final static HashMap<Character, Integer> CHARMAP = new HashMap<>();
    
    static {
        
        for (PunctuationSigns punctuation : PunctuationSigns.values()) {
            SIGNS.add(punctuation.getSign());
            CHARS.add(punctuation.getCharacter());
            CHARMAP.put(punctuation.getCharacter(), punctuation.getIndex());
        }
        
    }

    public final static boolean isPunctuation(String input) {

        if (TextToolbox.isReallyEmpty(input)) return false;
        if (input.length() != 1) return false;

        return SIGNS.contains(input);

    }
    
    public final static boolean isPunctuation(char c) {
        return CHARS.contains(c);
    }

    public final static boolean isTerminalPunctuation(String input) {

        if (TextToolbox.isReallyEmpty(input)) return false;
        if (input.length() != 1) return false;

        switch (input) {
            case".":
            case"!":
            case"?":
            case"…":
                return true;
            default:
                return false;
        }

    }
    
    public final static boolean isNonTerminalPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

        switch (input) {
            case",":
            case":":
            case";":
                return true;
            default:
                return false;
        }

    }
    
    public final static int indexOf(char c) {
        return CHARMAP.get(c);
    }
    
}
