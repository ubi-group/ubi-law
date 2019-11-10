package com.itcag.legalyzer.util.eval;

import com.itcag.legalyzer.util.cat.Category;

import java.util.TreeMap;

public class Result {

    protected final TreeMap<Integer, Category> categories;
    
    private Category highestRanking = null; 
    private Category highestRankingNotGeneric = null; 
    
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
        if (this.highestRanking == null || this.highestRanking.getScore() < score) this.highestRanking = this.categories.get(index);
        if ((this.highestRankingNotGeneric == null || this.highestRankingNotGeneric.getScore() < score) && index > 0) this.highestRankingNotGeneric = this.categories.get(index);
    }
    
    public Category getHighestRanking() {
        return highestRanking;
    }

    public Category getHighestRankingNotGeneric() {
        return highestRankingNotGeneric;
    }

}
