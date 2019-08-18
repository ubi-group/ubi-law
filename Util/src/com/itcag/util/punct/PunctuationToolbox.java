package com.itcag.util.punct;

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
    
    public final static void punctuate(StringBuilder input) {

        TextToolbox.trim(input);
        if (input.length() == 0) return;

        if (!isPunctuation(input.substring(input.length() - 1))) {
            input.append(".");
        }

    }
    
    public final static boolean isPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

        return SIGNS.contains(input);

    }
    
    public final static boolean isPunctuation(char c) {
        return CHARS.contains(c);
    }

    public final static boolean isTerminalPunctuation(String input) {

        if (input.length() != 1) return false;
        if (TextToolbox.isEmpty(input)) return false;

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
    
    /*
     * Assumes that punctuation is already normalized: empty space AFTER punctuation, but NONE before. 
     */
    public final static void detachMarks(StringBuilder input) {

        detachPunctuation(input);
        dettachSlashes(input);

    }
    
    public final static void reattachMarks(StringBuilder input) {
        
        reattachPunctuation(input);
        reattachSlashes(input);

    }

    /*
     * Assumes that punctuation is already normalized: empty space AFTER punctuation, but NONE before. 
     */
    public final static void detachPunctuation(StringBuilder input) {

        SIGNS.stream().forEach((punctuation) -> {
            TextToolbox.replace(input, punctuation, " " + punctuation + " ");
        });
        
    }
    
    public final static void reattachPunctuation(StringBuilder input) {
        
        SIGNS.stream().forEach((punctuation) -> {
            TextToolbox.replace(input, " " + punctuation, punctuation);
        });

    }

    public final static void dettachSlashes(StringBuilder input) {

        TextToolbox.replace(input, "/", " / ");

    }
    
    public final static void reattachSlashes(StringBuilder input) {
        
        while (input.indexOf(" / ") != -1) TextToolbox.replace(input, " / ", "/");
        while (input.indexOf("/ ") != -1) TextToolbox.replace(input, "/ ", "/");
        while (input.indexOf(" /") != -1) TextToolbox.replace(input, " /", "/");

    }

}
