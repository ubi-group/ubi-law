package com.itcag.demo;

import java.util.HashMap;

public class WebConstants {
    

    public final static String VERSION = "0.0.1";
    
    public final static String CONTEXT_PATH = "/Demo";
    public final static String GENERAL_TITLE = "Demo";

    public final static String WORD_VECTOR_PATH = "/home/alis/Desktop/legaltech/wordvec.txt";
    public final static String CATEGORIES_PATH = "/home/alis/Desktop/legaltech/experiments/categories.txt";
    
    public final static HashMap<String, String> MODEL_PATHS = new HashMap<>();
    static {
        MODEL_PATHS.put("צבא וביטחון", "/home/alis/Desktop/hebrew/demo models/SecurityModel.net");
        MODEL_PATHS.put("כדורגל", "/home/alis/Desktop/hebrew/demo models/FootballModel.net");
        MODEL_PATHS.put("סלבס", "/home/alis/Desktop/hebrew/demo models/CelebsModel.net");
        MODEL_PATHS.put("טכנולוגיה", "//home/alis/Desktop/hebrew/demo models/TechModel.net");
    };
    
    public final static int UPDATE_DELAY = 2000;
    
}
