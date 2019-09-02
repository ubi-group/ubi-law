package com.itcag.demo.util;

import java.util.ArrayList;
import java.util.HashMap;

public class Categories {

    private final static ArrayList<ClassificationCategory> CATEGORIES = new ArrayList<>();
    
    public static void addCategory(ClassificationCategory category) {
        CATEGORIES.add(category);
    }
    
    public static HashMap<String, String> test(String sentence) throws Exception {
        
        HashMap<String, String> retVal = new HashMap<>();
        
        for (ClassificationCategory category : CATEGORIES) {
            boolean test = category.test(sentence);
            retVal.put(category.getLabel(), Boolean.toString(test).toUpperCase());
        }
        
        return retVal;
        
    }
    
}
