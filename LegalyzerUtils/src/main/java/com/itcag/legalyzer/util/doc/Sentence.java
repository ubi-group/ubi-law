package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.eval.Result;
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
    
    @Override
    public Result getResult() {
        return this.result;
    }
    
    @Override
    public void setResult(Result result) {
        this.result = result;
    }
    
    @Override
    public ArrayList<Recommendation> getRecommendations() {
        return this.recommendations;
    }
    
    @Override
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
