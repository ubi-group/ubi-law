/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.datatier.meta;


public enum HTTPMethods {
    
    POST("POST"), 
    PUT("PUT"), 
    GET("GET"),
    DELETE("DELETE") 
    ;
    
    private final String fieldValue;
    
    HTTPMethods(String fieldValue) {
        this.fieldValue = fieldValue;
    }
    
    public final String getFieldValue() {
        return fieldValue;
    }    
}