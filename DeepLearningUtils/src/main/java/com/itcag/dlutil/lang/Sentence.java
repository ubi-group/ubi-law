package com.itcag.dlutil.lang;

import com.itcag.dlutil.eval.Result;

import java.util.ArrayList;

public class Sentence implements Text {

    private final String text;
    
    /**
     * Result is set by the neural net.
     * It is merely a complete list of classification categories, each with the corresponding weight.
     */
    private Result result = null;
    
    /**
     * Recommendations are the final outcome set by the inference.
     * They include categories that are not the highest ranking, but are inferred from the top classes.
     * 
     */
    private final ArrayList<Recommendation> recommendations = new ArrayList<>();
    
    public Sentence(String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public Result getResult() {
        return this.result;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
    public ArrayList<Recommendation> getRecommendations() {
        return this.recommendations;
    }
    
    public void addRecommendation(Recommendation recommendation) {
        this.recommendations.add(recommendation);
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        
        retVal.append(this.text);
        for (Recommendation recommendation : this.recommendations) {
            retVal.append("\n");
            retVal.append("\t").append(recommendation.getStrength());
            retVal.append(" (").append(Integer.toString(recommendation.getValue())).append(")");
            retVal.append("\t").append(recommendation.getCategory().getIndex()).append(" ").append(recommendation.getCategory().getLabel());
        }
        
        return retVal.toString();
        
    }
    
}
