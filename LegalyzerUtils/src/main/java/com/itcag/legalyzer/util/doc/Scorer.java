package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.eval.Result;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 */
public class Scorer {

    protected LinkedHashMap<Integer, Category> getEvaluation(Result result, TreeMap<Integer, Category> categories) {
        
        LinkedHashMap<Integer, Category> retVal = new LinkedHashMap<>();
        
        TreeMap<Double, ArrayList<Category>> sorted = sort(result, categories);
        for (Map.Entry<Double, ArrayList<Category>> entry : sorted.entrySet()) {
            for (Category category : entry.getValue()) {
                retVal.put(category.getIndex(), category);
            }
        }
        
        return retVal;
        
    }

    private TreeMap<Double, ArrayList<Category>> sort(Result result, TreeMap<Integer, Category> categories) {
        
        TreeMap<Double, ArrayList<Category>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<Integer, Category> entry : result.getCategories().entrySet()) {
            Double score = entry.getValue().getScore();
            Category category = categories.get(entry.getKey());
            if (retVal.containsKey(score)) {
                retVal.get(score).add(category);
            } else {
                retVal.put(score, new ArrayList<>(Arrays.asList(category)));
            }
        }

        return retVal;
        
    }
    
}
