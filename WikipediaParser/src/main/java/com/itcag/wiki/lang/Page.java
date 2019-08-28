package com.itcag.wiki.lang;

import com.itcag.wiki.Config;
import com.itcag.util.txt.TextToolbox;

public class Page {
    
    public enum Type {
        CATEGORY,
        PAGE,
        UTIL
    }
    
    private final String id;
    private final String title;
    private final Type type;
    
    public Page(String id, String title, Type type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Type getType() {
        return type;
    }
    
    @Override
    public String toString() {

        StringBuilder retVal = new StringBuilder();
        
        retVal.append(id);
        String tmp = title;
        if (Type.PAGE.equals(type) && title.contains("(")) {
            tmp = TextToolbox.removeParentheses(tmp, "(", ")");
            tmp = tmp.trim();
        } 
        retVal.append(Config.PAGE_DELIMITER).append(tmp);
        retVal.append(Config.PAGE_DELIMITER).append(type.name());
        
        return retVal.toString();
        
    }
    
}
