package com.itcag.legalyzer.util;

import com.itcag.legalyzer.util.cat.Category;

/**
 * Holds data about the category that received the highest score.
 * This top category is referred to as an "anchor",
 * if it is different from the manually selected category used to tag the same document.
 * This manually selected category is referred to as the "tag".
 * Anchors are linked with their tags for the purpose of inference:
 * if an anchor receives the highest score when a new (untagged) document is evaluated,
 * the associated tags are also considered as potential categories significant for that document.
 * The data in this class are statistics collected from multiple cases while processing a corpus.
 */
public class TopCategory {
    
    private final Category category;
    
    /**
     * Scores assigned by the neural net model.
     */
    private double totalScore = 0.00;
    private Double minScore = null;
    private Double maxScore = null;
    
    /**
     * Frequency of occurrence of this category as the top category in the processed corpus.
     */
    private int foo = 0;
    
    public TopCategory(Category category) {
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
