package com.itcag.datatier.meta;

public enum BulkOperations {

    INDEX("index"),
    CREATE("create"),
    DELETE("delete"),
    UPDATE("update");
    
    private final String fieldName;

    BulkOperations(String fieldValue) {
        this.fieldName = fieldValue;
    }

    public final String getFieldName() {
        return fieldName;
    }

}