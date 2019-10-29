package com.itcag.legalyzer.util.doc;

import com.itcag.split.Splitter;
import java.util.ArrayList;

public class Paragraph extends Sentence implements Text {

    private final ArrayList<Sentence> sentences = new ArrayList<>();
    
    public Paragraph(String text) throws Exception {
        super(text);
        Splitter splitter = new Splitter();
        splitter.split(new StringBuilder(text)).stream().forEach((sentence) -> {this.sentences.add(new Sentence(sentence.toString()));});
    }
    
    public ArrayList<Sentence> getSentences() {
        return this.sentences;
    }
    
    public void addSentence(Sentence sentence) {
        this.sentences.add(sentence);
    }
    
}
