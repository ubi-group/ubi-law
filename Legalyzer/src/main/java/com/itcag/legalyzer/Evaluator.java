package com.itcag.legalyzer;

import com.itcag.legalyzer.util.doc.Recommendation;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Text;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/**
 * Analyzes scores returned by the neural net model, and decides which categories will be displayed.
 */
public class Evaluator {

    private final Inference inference;
    
    public Evaluator(Inference inference) {
        this.inference = inference;
    }
    
    public void run(Text text) {
        
        TreeMap<Integer, ArrayList<Category>> recommendedCategories = this.inference.getRecommendations(text.getResult().getCategoriesSortedByScore());
        for (Map.Entry<Integer, ArrayList<Category>> entry : recommendedCategories.entrySet()) {
            for (Category recommendedCategory : entry.getValue()) {
//                if (recommendedCategory.getSignificance() < 11) break;
                Recommendation recommendation = new Recommendation(recommendedCategory, entry.getKey());
                text.addRecommendation(recommendation);
            }
        }
        
    }

}
