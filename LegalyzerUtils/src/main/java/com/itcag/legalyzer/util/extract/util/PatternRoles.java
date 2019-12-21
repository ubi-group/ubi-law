package com.itcag.legalyzer.util.extract.util;

public enum PatternRoles {

    TRIGGER(""),
    SKIPPER(""),
    STOPPER(""),
    OTHER(""),
    
    ;
    
    private final String field;
    
    private PatternRoles(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
