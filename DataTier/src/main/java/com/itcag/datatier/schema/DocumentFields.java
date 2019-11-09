package com.itcag.datatier.schema;

public enum DocumentFields {

    id("id"),
    
    ;

    private final String fieldName;

    DocumentFields(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }
    
}
