package com.itcag.legalyzer.util.eval;

import com.itcag.legalyzer.util.cat.Category;

import java.util.Collections;
import java.util.TreeMap;

public class SigmoidResult extends Result {

    private final Double threshold;

    private final TreeMap<Double, Category> positive = new TreeMap<>(Collections.reverseOrder());
    
    public SigmoidResult(TreeMap<Integer, Category> categories, Double threshold) {
        super(categories);
        this.threshold = threshold;
    }
    
    public Double getThreshold() {
        return this.threshold;
    }
    
    @Override
    public void setCategoryScore(int index, double score) {
        super.setCategoryScore(index, score);
        if (score > this.threshold) {
            this.positive.put(score, super.categories.get(index));
        }
    }
    
    public final TreeMap<Double, Category> getPositive() {
        return this.positive;
    }
    
}
