package com.itcag.legalyzer.util.parse;

public enum ParserFields {

    MAX_LINE_LENGTH("maxLineLength"),
    MAX_NUM_PARAGRAPHS("maxNumParagraphs"),

    STRIP_OFF_BULLETS("stripOffBullets"),
    REMOVE_PARENTHESES("removeParentheses"),
    REMOVE_QUOTES("removeQuotes"),
    
    ;
        
    private final String name;
    
    private ParserFields(String name) {
        this.name = name;
    }
    
    public final String getName() {
        return this.name;
    }
    
}
