package com.itcag.demo;

public enum FormFields {
    
    QUERY("query"),
    ID("id"),
    PARAGRAPH_INDEX("paragraphIndex"),
    SENTENCE_TEXT("sentenceText"),
    CATEGORY_ID("categoryId"),
    IS_CATEGORY_ADDITION("isCategoryAddition"),
    IS_BEING_MODIFIED("isBeingModified"),
    NEW_CATEGORY("newCategory"),
    REMOVE_TAG("removeTag"),
    ADD_TAG("addTag"),
    REPLACE_TAG("replaceTag"),
    
    ;
    
    private final String fieldName;
    
    FormFields(String fieldName) {
        this.fieldName = fieldName;
    }
    
    public final String getName() {
        return fieldName;
    }
    
}
