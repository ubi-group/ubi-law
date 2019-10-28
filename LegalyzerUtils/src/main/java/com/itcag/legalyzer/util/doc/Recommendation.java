package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;

public class Recommendation {

    private final static int STRONG_UPPER_THRESHOLD = 75;
    private final static int STRONG_LOWER_THRESHOLD = 25;

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
    private final int value;
    
    public Recommendation(Category category, int value) {
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

    public int getValue() {
        return value;
    }
    
}
