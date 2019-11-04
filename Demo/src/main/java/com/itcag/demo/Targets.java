package com.itcag.demo;

public enum Targets {

    ABOUT("אודות", "About"),
    TEST("ניסוי", "Test"),
    EXAMPLES("דוגמאות רב משמעותיות", "Examples"),
    PROCESS_DOCUMENT_INPUT("עיבוד קלט מסמך", "ProcessDocumentInput"),
    PROCESS_DOCUMENT_OUTPUT("עיבוד פלט מסמך", "ProcessDocumentOutput"),

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
