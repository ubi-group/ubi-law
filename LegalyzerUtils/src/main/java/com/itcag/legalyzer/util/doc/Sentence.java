package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.eval.Result;
import com.itcag.util.MathToolbox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

import org.json.JSONObject;

public class Sentence extends Scorer implements Text {

    private enum Fields {
        
        INDEX("index"),
        PARAGRAPH_INDEX("paragraphIndex"),
        TEXT("text"),
        
        ;
        
        private final String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        private String getName() {
            return this.name;
        }
        
    }
    
    private final int index;
    private final int paragraphIndex;
    
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
    private Recommendation highestRanking = null; 
    
    public Sentence(String text, int index, int paragraphIndex) {
        this.text = text;
        this.index = index;
        this.paragraphIndex = paragraphIndex;
    }
    
    public Sentence(JSONObject jsonObject) {
        this.text = jsonObject.getString(Fields.TEXT.getName());
        this.index = jsonObject.getInt(Fields.INDEX.getName());
        this.paragraphIndex = jsonObject.getInt(Fields.PARAGRAPH_INDEX.getName());
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    public int getParagraphIndex() {
        return this.paragraphIndex;
    }
    
    @Override
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
        if (this.highestRanking == null || this.highestRanking.getValue() < recommendation.getValue()) this.highestRanking = recommendation;
    }
    
    @Override
    public Recommendation getHighestRanking() {
        return this.highestRanking;
    }
    
    public LinkedHashMap<Integer, Category> getEvaluation(TreeMap<Integer, Category> categories) {
        return this.getEvaluation(this.result, categories);
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        
        retVal.append(this.text).append("\n");
        
        if (this.result.getHighestRanking() != null) {
            retVal.append("Top: ").append(this.result.getHighestRanking().toString()).append("\n");
            if (this.result.getHighestRanking().getIndex() == 0) {
                if (this.result.getHighestRankingNotGeneric() != null) {
                    retVal.append("Next: ").append(this.result.getHighestRankingNotGeneric().toString()).append("\n");
                }
            }
        }
        
        for (Recommendation recommendation : this.recommendations) {
            retVal.append("\n");
            retVal.append("\t").append(recommendation.getStrength());
            retVal.append(" (").append(Double.toString(MathToolbox.roundDouble(recommendation.getValue(), 2))).append(")");
            retVal.append("\t").append(Integer.toString(recommendation.getCategory().getIndex())).append(" ").append(recommendation.getCategory().getLabel());
        }
        
        return retVal.toString();
        
    }
    
    @Override
    public JSONObject getJSON() {
     
        JSONObject retVal = new JSONObject();
        
        retVal.put(Fields.INDEX.getName(), this.index);
        retVal.put(Fields.PARAGRAPH_INDEX.getName(), this.paragraphIndex);
        retVal.put(Fields.TEXT.getName(), this.text);
        
        return retVal;
        
    }
    
}
