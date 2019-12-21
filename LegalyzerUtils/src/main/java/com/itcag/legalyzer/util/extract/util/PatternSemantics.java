package com.itcag.legalyzer.util.extract.util;

public enum PatternSemantics {

    FINE(""),
    INCARCERATION(""),
    COMPENSATION(""),
    PROBATION(""),
    COMMUNITY_SERVICE(""),
    SUSPENDED_SENTENCES(""),
    JUDGE(""),
    PLAINTIFF_ATTORNEY(""),
    DEFENDANT_ATTORNEY(""),
    ACCEPTED_APPEAL(""),
    REJECTED_APPEAL(""),
    ERASED_APPEAL(""),
    ACCEPTED_PETITION(""),
    REJECTED_PETITION(""),
    ERASED_PETITION(""),
    
    ;
    
    private final String field;
    
    private PatternSemantics(String field) {
        this.field = field;
    }
    
    public String getField() {
        return this.field;
    }
    
}
