package com.itcag.legalyzer.util.doc;

import java.util.ArrayList;

public class Paragraph extends Sentence implements Text {

    private final ArrayList<Sentence> sentences = new ArrayList<>();
    
    public Paragraph(String text) {
        super(text);
    }
    
    public ArrayList<Sentence> getSentences() {
        return this.sentences;
    }
    
    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence);
    }
    
}
