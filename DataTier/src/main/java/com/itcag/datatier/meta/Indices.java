package com.itcag.datatier.meta;

public enum Indices {

    CORRECTIONS("corrections"),
    DATA_TRAINING("data_training"),
    CATEGORIES("categories"),
    
    ;

    private final String fieldName;

    Indices(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }

}