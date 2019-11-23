package com.itcag.demo.html;

import java.util.ArrayList;

public class HTMLHeader {
    
    
    public static ArrayList<String> getScripts() {
        ArrayList<String> arrScripts = new ArrayList();
        arrScripts.add("https://code.jquery.com/jquery-1.12.4.js");
        arrScripts.add("https://code.jquery.com/ui/1.12.1/jquery-ui.js");
        arrScripts.add("resources/js/example.js");
        return arrScripts;
    }    
}
