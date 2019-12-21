package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Aggregates sentences scores at the paragraph level,
 * or paragraph scores at the document level.
 * WARNING: the evaluation must be carried accordingly!!!
 * If the paragraphs were evaluated, they will have scores.
 * But if the sentences were evaluated, sentences will have
 * scores, but NOT paragraphs.
 */
public class Aggregator {

    protected LinkedHashMap<Integer, Category> aggregate(ArrayList<Text> texts, TreeMap<Integer, Category> categories) {
        
        LinkedHashMap<Integer, Category> retVal = new LinkedHashMap<>();
        
        TreeMap<Double, ArrayList<Category>>  sorted = sort(texts, categories);
        for (Map.Entry<Double, ArrayList<Category>> entry : sorted.entrySet()) {
            for (Category category : entry.getValue()) {
                retVal.put(category.getIndex(), category);
            }
        }
        
        return retVal;
        
    }
    
    private TreeMap<Double, ArrayList<Category>> sort(ArrayList<Text> texts, TreeMap<Integer, Category> categories) {
        
        TreeMap<Double, ArrayList<Category>> retVal = new TreeMap<>(Collections.reverseOrder());

        /**
         * Key is the category index, and the value is the category score (incremented).
         */
        HashMap<Integer, Double> scores = combineScores(texts);
        TreeMap<Double, HashSet<Integer>> inverted = invertScores(scores);
        
        for (Map.Entry<Double, HashSet<Integer>> entry : inverted.entrySet()) {
            for (Integer index : entry.getValue()) {
                Category category = categories.get(index);
                category.setScore(entry.getKey());
                if (retVal.containsKey(entry.getKey())) {
                    retVal.get(entry.getKey()).add(category);
                } else {
                    retVal.put(entry.getKey(), new ArrayList<>(Arrays.asList(category)));
                }
            }
        }
        
        return retVal;
        
    }
    
    private HashMap<Integer, Double> combineScores(ArrayList<Text> texts) {
        
        HashMap<Integer, Double> retVal = new HashMap<>();
        
        for (Text text : texts) {
            
            for (Map.Entry<Integer, Category> entry : text.getResult().getCategories().entrySet()) {
        
                Category category = entry.getValue();
                
                if (category.getScore() != null) {
                    
                    if (retVal.containsKey(category.getIndex())) {
                        retVal.put(category.getIndex(), retVal.get(category.getIndex()) + category.getScore());
                    } else {
                        retVal.put(category.getIndex(), category.getScore());
                    }
                    
                }
                
            }
        
        }
        
        return retVal;

    }
    
    private TreeMap<Double, HashSet<Integer>> invertScores(HashMap<Integer, Double> scores) {
        
        TreeMap<Double, HashSet<Integer>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            
            if (retVal.containsKey(entry.getValue())) {
                retVal.get(entry.getValue()).add(entry.getKey());
            } else {
                retVal.put(entry.getValue(), new HashSet<>(Arrays.asList(entry.getKey())));
            }
            
        }
        
        return retVal;
        
    }

}
