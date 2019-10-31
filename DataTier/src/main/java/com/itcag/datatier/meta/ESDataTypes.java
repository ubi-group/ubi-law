/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.datatier.meta;

public enum ESDataTypes {
    
    TEXT("text"),
    KEYWORD("keyword"),
    INTEGER("integer"),
    LONG("long"),
    DOUBLE("double"),
    BOOLEAN("boolean"),
    DATE("date"),
    NESTED("nested")
    ;
    
    private final String fieldValue;
    
    ESDataTypes(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public final String getFieldValue() {
        return fieldValue;
    }
    
}