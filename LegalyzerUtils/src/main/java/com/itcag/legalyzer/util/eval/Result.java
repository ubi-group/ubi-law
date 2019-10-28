package com.itcag.legalyzer.util.eval;

import com.itcag.legalyzer.util.cat.Category;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

import java.util.TreeMap;

public class Result {

    private final double SCORE_THRESHOLD = 0.5;

    protected final TreeMap<Integer, Category> categories;
    
    public Result(TreeMap<Integer, Category> categories) {
        this.categories = categories;
    }
    
    public TreeMap<Integer, Category> getCategories() {
        return this.categories;
    }
    
    public Category getCategory(int index) {
        return this.categories.get(index);
    }
    
    public void setCategoryScore(int index, double score) {
        this.categories.get(index).setScore(score);
    }
    
    public LinkedHashMap<Integer, Category> getCategoriesSortedByScore() {
        
        TreeMap<Double, Category> sorted = new TreeMap<>(Collections.reverseOrder());
        
        this.categories.entrySet().forEach((entry) -> {
            sorted.put(entry.getValue().getScore(), entry.getValue());
        });

        ArrayList<Category> tmp = new ArrayList<>(sorted.values());
        /**
         * If the highest scoring category is generic,
         * remove it and check the next category.
         */
        if (tmp.get(0).getIndex() == 0) {
            tmp.remove(0);
            /**
             * If the next category is below the threshold,
             * all other categories will be also below it,
             * and the resulting recommendation should be
             * considered generic (no recommendations).
             */
            if (tmp.get(0).getScore() < SCORE_THRESHOLD) tmp.clear();
        }
        
        LinkedHashMap<Integer, Category> retVal = new LinkedHashMap<>();
        tmp.forEach((category) -> {
            retVal.put(category.getIndex(), category);
        });
        
        return retVal;
        
    }
    
}
