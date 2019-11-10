package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;

public class Recommendation {

    private final static double STRONG_UPPER_THRESHOLD = 0.95;
    private final static double STRONG_LOWER_THRESHOLD = 0.85;

    public enum Strength {
        
        VERY_STRONG("Very strong"),
        STRONG("Strong"),
        WEAK("Weak"),
        
        ;
    
        private final String label;
        
        private Strength(String label) {
            this.label = label;
        }
        
        public String getLabel() {
            return this.label;
        }
        
    }
    
    private final Category category;
    private final Strength strength;
    private final double value;
    
    public Recommendation(Category category, double value) {
        this.category = category;
        this.value = value;
        if (value >= STRONG_UPPER_THRESHOLD) {
            this.strength = Strength.VERY_STRONG;
        } else if (value <= STRONG_LOWER_THRESHOLD) {
            this.strength = Strength.WEAK;
        } else {
            this.strength = Strength.STRONG;
        }
    }

    public Category getCategory() {
        return category;
    }

    public Strength getStrength() {
        return strength;
    }

    public double getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return this.strength + " (" + this.value + ") " + this.category.getIndex() + " " + this.category.getLabel();
    }
    
}
