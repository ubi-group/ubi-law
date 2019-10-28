package com.itcag.legalyzer.util.cat;

import com.itcag.util.Converter;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.TreeMap;

public class Categories {

    TreeMap<Integer, Category> categories = new TreeMap<>();

    public Categories(String filePath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            
            String[] elts = line.split(",");
            
            Integer index = Converter.convertStringToInteger(elts[0].trim());
            if (index == null) throw new NullPointerException("Invalid category (" + line + ") in file: " + filePath);
            
            this.categories.put(index, new Category(index, elts[1].trim()));
            
        }
        
    }

    public TreeMap<Integer, Category> get() {
        return this.categories;
    }
    
}
