package com.itcag.legalyzer;

import com.itcag.dlutil.lang.Recommendation;
import com.itcag.dlutil.lang.Category;
import com.itcag.dlutil.lang.Paragraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Analyzes scores returned by the neural net model, and decides which categories will be displayed.
 */
public class Evaluator {

    private final Inference inference;
    
    private final TreeMap<Double, Category> resultCategories = new TreeMap<>(Collections.reverseOrder());
    
    public Evaluator(Inference inference) {
        this.inference = inference;
    }
    
    public void run(Paragraph paragraph) {
        
        
        // if length < 150 display nothing
        // if length < 300 and single sentence display nothing

        // if frequency of occurrence of generic legal stop words > threshold display nothing

        for (Map.Entry<Integer, Category> entry : paragraph.getResult().getCategories().entrySet()) {
            Category category = entry.getValue();
            resultCategories.put(category.getScore(), category);
        }

        Iterator<Map.Entry<Double, Category>> resultCategoryIterator = this.resultCategories.entrySet().iterator();
        while (resultCategoryIterator.hasNext()) {

            Map.Entry<Double, Category> entry = resultCategoryIterator.next();
            Category category = entry.getValue();

            /**
             * If the first category encountered (the highest scoring) is 0 (generic),
             * check the next category.
             */
            if (category.getIndex() == 0) continue;

            /**
             * If the next category's score is less than the threshold, there are no recommendations.
             */
            if (category.getScore() < Config.SCORE_THRESHOLD) return;

            insertRecommendations(paragraph, category);
//For now we process only one category
//for later: the second category could be included in the inferred ones - boost its significance score
break;
        }

    }

    private void insertRecommendations(Paragraph paragraph, Category category) {
        
        TreeMap<Integer, ArrayList<Category>> recommendedCategories = this.inference.getRecommendations(category);
        for (Map.Entry<Integer, ArrayList<Category>> entry : recommendedCategories.entrySet()) {
            for (Category recommendedCategory : entry.getValue()) {
                Recommendation recommendation = new Recommendation(recommendedCategory, entry.getKey());
                paragraph.addRecommendation(recommendation);
            }
        }

    }
    
}
