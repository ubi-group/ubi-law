package com.itcag.legalyzer;

import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Recommendation;
import com.itcag.legalyzer.util.doc.Sentence;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

public class Legalyzer {

    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    public Legalyzer(Categories categories, Inference inference) throws Exception {
        
        this.tester = new Tester();
        this.categories = categories;

        this.inference = inference;
        
    }
    
    public void evaluate(Document document) throws Exception {
        
        for (Paragraph paragraph : document.getParagraphs()) {
            
            for (Sentence sentence : paragraph.getSentences()) {
                sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                paragraph.addSentence(sentence);
                tester.test(sentence.getText(), sentence.getResult());
            }

        }

    }
    
    public void recommend(Document document) throws Exception {
        
        for (Paragraph paragraph : document.getParagraphs()) {
            
            for (Sentence sentence : paragraph.getSentences()) {
                sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                paragraph.addSentence(sentence);
                tester.test(sentence.getText(), sentence.getResult());
                TreeMap<Integer, ArrayList<Category>> recommendations = this.inference.getRecommendations(sentence.getEvaluation(categories.get()));
                for (Map.Entry<Integer, ArrayList<Category>> entry : recommendations.entrySet()) {
                    for (Category category : entry.getValue()) {
                        Recommendation recommendation = new Recommendation(category, entry.getKey());
                        sentence.addRecommendation(recommendation);
                    }
                }
            }

        }

    }
    
}
