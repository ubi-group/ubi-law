package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.eval.Result;
import com.itcag.split.Splitter;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Paragraph implements Text {

    private final String text;
    private final int index;
    
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
    
    public Paragraph(String text, int index, AtomicInteger sentenceIndex) throws Exception {
        this.text = text;
        this.index = index;
        Splitter splitter = new Splitter();
        for (StringBuilder sentence : splitter.split(new StringBuilder(text))) {
            this.sentences.add(new Sentence(sentence.toString(), sentenceIndex.addAndGet(1)));
        }
    }
    
    public int getIndex() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public String getText() {
        return this.text;
    }
    
    public ArrayList<Sentence> getSentences() {
        return this.sentences;
    }
    
    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence);
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
    
}
