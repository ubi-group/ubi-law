package com.itcag.datatier.schema;

public enum CategoryFields {

    id("id"),
    label("label"),
    model("model"),
    
    ;

    private final String fieldName;

    CategoryFields(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }    
    
}
