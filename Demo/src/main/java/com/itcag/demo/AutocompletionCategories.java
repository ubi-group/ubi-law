package com.itcag.demo;

import com.itcag.util.io.TextFileReader;
import java.util.ArrayList;

public class AutocompletionCategories {

    private static ArrayList<String> allCategories;
    
    private static AutocompletionCategories autocompletionCategories;

    private AutocompletionCategories(String filePath) throws Exception { 
        
        allCategories = new ArrayList();
               
        ArrayList<String> lines = TextFileReader.read(filePath);
        
        lines.forEach((line) -> { 
            allCategories.add(line);
        });
        
    } 
    
    public static AutocompletionCategories getInstance(String filePath) throws Exception {
        
        if (autocompletionCategories == null) 
            autocompletionCategories = new AutocompletionCategories(filePath); 
  
        return autocompletionCategories; 
    }     

    public static ArrayList<String> getAllCategories() {
        return allCategories;
    }

    
}
