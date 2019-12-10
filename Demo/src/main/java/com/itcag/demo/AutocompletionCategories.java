package com.itcag.demo;

import com.itcag.util.io.TextFileReader;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class AutocompletionCategories {

    private static ArrayList<String> allCategories;
    
    private static AutocompletionCategories autocompletionCategories;
    
    private final static String NONE = "None";

    private AutocompletionCategories(String filePath) throws Exception { 
        
        allCategories = new ArrayList();

        InputStream input = getClass().getClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        allCategories.add(NONE);

        String line = reader.readLine();
        while (line != null) {
            line = line.trim();             
            if (!line.isEmpty()) allCategories.add(line);
            line = reader.readLine();
        }        
        
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
