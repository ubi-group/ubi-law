package com.itcag.tagger;

public enum Targets {

    HOME(WebConstants.GENERAL_TITLE, WebConstants.CONTEXT_PATH),
    
    ADD_TAG("New Tag", "AddTag"),
    EDIT_TAG("Edit Tag", "EditTag"),
    DELETE_TAG("Delete", "DeleteTag"),
    TAGS("Tags", "Tags"),
    
    ADD_DOCUMENT("Upload Document", "AddDocument"),
    DELETE_DOCUMENT("Delete", "DeleteDocument"),
    DOCUMENT("Evaluate", "Document"),
    DOCUMENTS("Documents", "Documents"),
    DOCUMENTS_NOT_COMPLETED("Not Completed Documents", "DocumentsNotCompleted"),
    DOCUMENTS_NOT_APPROVED("Not Approved Documents", "DocumentsNotApproved"),
    
    ERROR("Error", "Error"),

    ;
    
    private final String title;
    private final String url;
    
    Targets(String title, String url) {
        this.title = title;
        this.url = url;
    }
    
    public String getTitle() {
        return title;
    }
    
    public String getUrl() {
        return url;
    }
    
}
