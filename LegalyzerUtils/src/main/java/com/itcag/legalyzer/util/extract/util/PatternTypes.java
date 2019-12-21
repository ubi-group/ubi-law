package com.itcag.legalyzer.util.extract.util;

public enum PatternTypes {

    PERSONNEL(""),
    COURT_CASE(""),
    PENALTY(""),
    DECISION(""),
    LAW(""),
    
    ;
    
    private final String field;
    
    private PatternTypes(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
