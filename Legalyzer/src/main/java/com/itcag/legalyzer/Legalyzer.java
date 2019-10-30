package com.itcag.legalyzer;

import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.split.Splitter;

public class Legalyzer {

    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    public Legalyzer(Categories categories, Inference inference) throws Exception {
        
        this.tester = new Tester();

        this.inference = inference;
        this.categories = categories;
        
    }
    
    public void insertRecommendations(Document document) throws Exception {
        
        Evaluator evaluator = new Evaluator(inference);

        for (Paragraph paragraph : document.getParagraphs()) {
            
            for (Sentence sentence : paragraph.getSentences()) {
                sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                paragraph.addSentence(sentence);
                tester.test(sentence.getText(), sentence.getResult());
                evaluator.run(sentence);
            }

        }

    }
    
}
