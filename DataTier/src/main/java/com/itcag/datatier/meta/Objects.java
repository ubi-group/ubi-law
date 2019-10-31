package com.itcag.datatier.meta;

public enum Objects {

    CORRECTION("correction"),

    ;
    
    private final String fieldName;

    Objects(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }

}