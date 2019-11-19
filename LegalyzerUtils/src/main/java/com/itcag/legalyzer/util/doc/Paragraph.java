package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.eval.Result;
import com.itcag.split.Splitter;
import com.itcag.split.SplitterException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.json.JSONArray;
import org.json.JSONObject;

public class Paragraph extends Aggregator implements Text {

    private enum Fields {
        
        INDEX("index"),
        TEXT("text"),
        SENTENCES("sentences"),
        
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

    private final String text;
    
    private final ArrayList<Sentence> sentences = new ArrayList<>();
    
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
    
    public Paragraph(String text, int index, AtomicInteger sentenceIndex) throws Exception {
        this.text = text;
        this.index = index;
        Splitter splitter = new Splitter();
        try {
            for (StringBuilder sentence : splitter.split(new StringBuilder(text))) {
                this.sentences.add(new Sentence(sentence.toString(), sentenceIndex.addAndGet(1), index));
            }
        } catch (Exception ex) {
            if (!(ex instanceof SplitterException)) throw ex;
        }
    }
    
    public Paragraph(JSONObject jsonObject) throws Exception {
        this.text = jsonObject.getString(Fields.TEXT.getName());
        this.index = jsonObject.getInt(Fields.INDEX.getName());
        JSONArray jsonArray = jsonObject.getJSONArray(Fields.SENTENCES.getName());
        for (int i = 0; i < jsonArray.length(); i++) {
            Sentence sentence = new Sentence(jsonArray.getJSONObject(i));
            this.sentences.add(sentence);
        }
    }
    
    @Override
    public int getIndex() {
        return this.index;
    }
    
    @Override
    public String getText() {
        return this.text;
    }
    
    public ArrayList<Sentence> getSentences() {
        return this.sentences;
    }
    
    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence);
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
        return this.aggregate(new ArrayList<>(this.sentences), categories);
    }
    
    @Override
    public JSONObject getJSON() {
        
        JSONObject retVal = new JSONObject();
        
        retVal.put(Fields.INDEX.getName(), this.index);
        retVal.put(Fields.TEXT.getName(), this.text);
        
        JSONArray jsonArray = new JSONArray();
        for (Sentence sentence : this.sentences) {
            jsonArray.put(sentence.getJSON());
        }
        retVal.put(Fields.SENTENCES.getName(), jsonArray);
        
        return retVal;
        
    }
    
}
