package com.itcag.legalyzer.util.eval;

import com.itcag.legalyzer.util.cat.Category;

import java.util.TreeMap;

public class Result {

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
    
}
