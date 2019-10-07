package com.itcag.dlutil.lang;

public class Category {
        
    private final int index;
    private final String label;

    private Double score = null;

    public Category(int index, String label) {
        this.index = index;
        this.label = label;
    }

    public int getIndex() {
        return index;
    }

    public String getLabel() {
        return label;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
    
        retVal.append(this.index).append("\t").append(this.label);
        if (this.score != null) retVal.append("\t").append(this.score);
        
        return retVal.toString();
    
    }
        
}
