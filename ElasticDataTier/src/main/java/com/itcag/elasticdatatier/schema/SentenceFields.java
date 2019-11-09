package com.itcag.datatier.schema;

public enum SentenceFields {

    sentence("sentence"),
    categoryId("categoryId"),
    
    ;

    private final String fieldName;

    SentenceFields(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }

}