package com.itcag.demo;

import java.util.HashMap;

public class WebConstants {
    

    public final static String VERSION = "0.0.1";
    
    public final static String CONTEXT_PATH = "/Demo";
    public final static String GENERAL_TITLE = "Demo";

    public final static String WORD_VECTOR_PATH = "/opt/tomcat/resources/wordvec.txt";
    public final static String CATEGORIES_PATH = "/opt/tomcat/resources/categories.txt";
    
    public final static HashMap<String, String> MODEL_PATHS = new HashMap<>();
    static {
        MODEL_PATHS.put("צבא וביטחון", "/opt/tomcat/resources/SecurityModel.net");
        MODEL_PATHS.put("כדורגל", "/opt/tomcat/resources/FootballModel.net");
        MODEL_PATHS.put("סלבס", "/opt/tomcat/resources/CelebsModel.net");
        MODEL_PATHS.put("טכנולוגיה", "/opt/tomcat/resources/TechModel.net");
    };
    
    public final static int UPDATE_DELAY = 2000;
    
}
