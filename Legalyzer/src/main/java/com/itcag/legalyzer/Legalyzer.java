package com.itcag.legalyzer;

import com.itcag.legalyzer.util.inference.Inference;
import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.util.Printer;

public class Legalyzer {

    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    public Legalyzer(Categories categories) throws Exception {
        
        this.tester = new Tester();
        this.categories = categories;

        this.inference = new Inference(categories);
        
    }
    
    public void evaluate(Document document) throws Exception {
        
        for (Paragraph paragraph : document.getParagraphs()) {
            
            for (Sentence sentence : paragraph.getSentences()) {
                
                try {
                    
                    sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                    tester.test(sentence.getText(), sentence.getResult());

                } catch (Exception ex) {
                    Printer.print(ex.getMessage());
                }
            }

        }

    }
    
    public void recommend(Document document) throws Exception {
        
        for (Paragraph paragraph : document.getParagraphs()) {
            
            for (Sentence sentence : paragraph.getSentences()) {
                
                try {
                    
                    sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                    tester.test(sentence.getText(), sentence.getResult());

                } catch (Exception ex) {
                    Printer.print(ex.getMessage());
                }
            
            }

        }

        this.inference.getRecommendations(document, 0);
            
    }
    
}
