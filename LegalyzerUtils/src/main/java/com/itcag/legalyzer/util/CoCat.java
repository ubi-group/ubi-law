package com.itcag.legalyzer.util;

import com.itcag.dlutil.lang.Category;

/**
 * Holds data about the categories with the highest score.
 */
public class CoCat {
    
    private final Category category;
    
    private double totalScore = 0.00;
    private Double minScore = null;
    private Double maxScore = null;
    
    private int foo = 0;
    
    public CoCat(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public double getAvgScore() {
        return totalScore / foo;
    }

    public double getMinScore() {
        return minScore;
    }

    public double getMaxScore() {
        return maxScore;
    }

    public int getFoo() {
        return foo;
    }

    public void setScore(double score) {
        totalScore += score;
        foo++;
        if (minScore == null || minScore > score) minScore = score;
        if (maxScore == null || maxScore < score) maxScore = score;
    }
    
}
