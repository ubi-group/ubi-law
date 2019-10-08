package com.itcag.legalyzer.util;

import com.itcag.dlutil.lang.Category;
import com.itcag.util.MathToolbox;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CatStat {

    private final Category category;
    
    private final HashMap<Integer, CoCat> cocategories = new HashMap<>();
    
    public CatStat(Category category) {
        this.category = category;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public int getindex() {
        return this.category.getIndex();
    }
    
    public String getLabel() {
        return this.category.getLabel();
    }

    public HashMap<Integer, CoCat> getCocategories() {
        return this.cocategories;
    }
    
    public void addCocategory(Category category, double score) {
        if (this.cocategories.containsKey(category.getIndex())) {
            this.cocategories.get(category.getIndex()).setScore(score);
        } else {
            CoCat cocat = new CoCat(category);
            cocat.setScore(score);
            this.cocategories.put(category.getIndex(), cocat);
        }
    }
    
    public TreeMap<Integer, CoCat> getTopCocategories(int n) {
        
        /**
         * Key = frequency of occurrence *how many times was this the top category).
         */
        TreeMap<Integer, CoCat> retVal = new TreeMap<>(Collections.reverseOrder());
        
        TreeMap<Integer, CoCat> tmp = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Integer, CoCat> entry : this.cocategories.entrySet()) {
            tmp.put(entry.getValue().getFoo(), entry.getValue());
        }
        
        for (Map.Entry<Integer, CoCat> entry : tmp.entrySet()) {
            retVal.put(entry.getKey(), entry.getValue());
            if (retVal.size() == n) break;
        }
        
        return retVal;
        
    }
    
    public int getTotal() {
        int retVal = 0;
        for (Map.Entry<Integer, CoCat> entry : this.cocategories.entrySet()) {
            retVal += entry.getValue().getFoo();
        }
        return retVal;
    }
    
}
