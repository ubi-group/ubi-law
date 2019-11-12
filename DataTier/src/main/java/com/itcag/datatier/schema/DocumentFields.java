package com.itcag.datatier.schema;

public enum DocumentFields {

    id("id"),
    paragraphs("paragraphs"),
    sentences("sentences"),
    paragraphIndex("paragraphIndex"),
    index("index"),
    text("text"),
    
    ;

    private final String fieldName;

    DocumentFields(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }
    
}
