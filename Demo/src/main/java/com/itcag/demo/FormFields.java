package com.itcag.demo;

public enum FormFields {
    
    QUERY("query"),
    
    
    ;
    
    private final String fieldName;
    
    FormFields(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public final String getName() {
        return fieldName;
    }
    
}
