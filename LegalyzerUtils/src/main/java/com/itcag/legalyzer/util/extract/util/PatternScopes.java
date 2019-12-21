package com.itcag.legalyzer.util.extract.util;

public enum PatternScopes {

    HIGH_COURT(""),
    CRIMINAL_COURT(""),
    
    ;
    
    private final String field;
    
    private PatternScopes(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
