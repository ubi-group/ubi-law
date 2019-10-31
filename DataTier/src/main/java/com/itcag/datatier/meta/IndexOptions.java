/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.datatier.meta;

public enum IndexOptions {

    TRUE("true"),
    FALSE("false")
    ;
    
    private final String fieldValue;
    
    IndexOptions(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public final String getFieldValue() {
        return fieldValue;
    }
    
}