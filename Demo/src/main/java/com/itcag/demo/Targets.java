package com.itcag.demo;

public enum Targets {

    ABOUT("אודות", "About"),
    TEST("ניסוי", "Test"),
    EXAMPLES("דוגמאות רב משמעותיות", "Examples"),

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
