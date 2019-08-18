package com.itcag.util.punct;

public enum PunctuationSigns {

    QUESTION("?", (char) 63, 0),
    EXCLAMATION("!", (char) 33, 1),
    PERIOD(".", (char) 46, 2),
    COLON(":", (char) 58, 3),
    SEMICOLON(";", (char) 59, 4),
    COMMA(",", (char) 44, 5),
    ELLIPSIS("…", (char) 8230, 6),

    ;

    private final String sign;
    private final char character;
    private final int index;

    private PunctuationSigns(String sign, char character, int index) {
        this.sign = sign;
        this.character = character;
        this.index = index;
    }

    public String getSign() {
        return sign;
    }

    public char getCharacter() {
        return character;
    }

    public int getIndex() {
        return index;
    }
        
}
