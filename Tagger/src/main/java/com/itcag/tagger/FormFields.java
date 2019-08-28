package com.itcag.tagger;

public enum FormFields {

    _ID("_id"),
    ID("id"),
    
    LABEL("label"),
    
    QUERY("query"),
    
    UPLOAD("upload"),
    
    ;
    
    private final String fieldName;
    
    FormFields(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public final String getName() {
        return fieldName;
    }
    
}
