package com.itcag.legalyzer;

import com.itcag.dlutil.lang.Category;
import com.itcag.util.MathToolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class Inference {

    private final Anchors anchors;
    private final Siblings siblings;
    
    public Inference(Anchors anchors, Siblings siblings) {
        this.anchors= anchors;
        this.siblings = siblings;
    }
    
    public boolean isAnchor(int index) {
        return this.anchors.containsIndex(index);
    }
    
    public TreeMap<Double, Category> getAnchorTags(int index) {
        return this.anchors.getAnchorTags(index);
    }
    
    public boolean isThematicSibling(int index1, int index2) {
        return this.siblings.isThematicSibling(index1, index2);
    }
    
    public ArrayList<Integer> getThematicSiblings(int index) {
        return this.siblings.getThematicSiblings(index);
    }
    
    public TreeMap<Integer, ArrayList<Category>> getRecommendations(LinkedHashMap<Integer, Category> categories) {
        HashMap<Integer, Category> index = getRecommendationIndex(categories);
        return transformIntoOutput(index);
    }
    
    private HashMap<Integer, Category> getRecommendationIndex(LinkedHashMap<Integer, Category> categories) {
        
        HashMap<Integer, Category> retVal = new HashMap<>();
        
        Iterator<Map.Entry<Integer, Category>> categoryIterator = categories.entrySet().iterator();
        while (categoryIterator.hasNext()) {
            
            Category category = categoryIterator.next().getValue();
            if (category.getScore() < Config.SCORE_THRESHOLD) break;
            
            HashMap<Integer, Category> index = getRecommendationIndex(category, categories);
            for (Map.Entry<Integer, Category> entry : index.entrySet()) {
                
                if (retVal.containsKey(entry.getKey())) {
                    double average = retVal.get(entry.getKey()).getSignificance();
                    average += entry.getValue().getSignificance();
                    average = average / 2;
                    average = MathToolbox.roundDouble(average, 2);
                    retVal.get(entry.getKey()).setSignificance(Double.valueOf(average).intValue());
                } else {
                    retVal.put(entry.getKey(), entry.getValue());
                }
                
            }

        }

        return retVal;
        
    }
    
    public TreeMap<Integer, ArrayList<Category>> getRecommendations(Category category, LinkedHashMap<Integer, Category> categories) {
        HashMap<Integer, Category> index = getRecommendationIndex(category, categories);
        return transformIntoOutput(index);
    }

    private HashMap<Integer, Category> getRecommendationIndex(Category category, LinkedHashMap<Integer, Category> categories) {
        
        HashMap<Integer, Category> retVal = new HashMap<>();

        category.setSignificance(convertScoreIntoSignificance(category.getScore()));
        retVal.put(category.getIndex(), category);
        
        if (isAnchor(category.getIndex())) {
            
            for (Map.Entry<Double, Category> entry : getAnchorTags(category.getIndex()).entrySet()) {
                
                Double weight = entry.getKey();
                
                Category tag = entry.getValue();
                /**
                 * Categories with score less than 0.01 have their anchor weight lowered,
                 * while categories with score greater than 0.01 have it increased.
                 */
                double factor = 100 * categories.get(tag.getIndex()).getScore();
                if (factor > 1) {
                    weight *= factor;
                    weight = MathToolbox.sigmoid(weight);
                } else {
                    weight *= factor;
                }
                
                if (isThematicSibling(category.getIndex(), entry.getValue().getIndex())) {
                    weight *= Config.THEMATIC_SIBLING_FACTOR;
                }
                
                entry.getValue().setSignificance(convertScoreIntoSignificance(weight));
                retVal.put(entry.getValue().getIndex(), entry.getValue());
                
            }
            
        }
        
        return retVal;
        
    }

    private int convertScoreIntoSignificance(Double weight) {

        weight *= 100;
        weight = MathToolbox.roundDouble(weight, 2);
        return weight.intValue();

    }
    
    private TreeMap<Integer, ArrayList<Category>> transformIntoOutput(HashMap<Integer, Category> index) {
        
        /**
         * Key = significance (weight),
         * Value =  list of recommended categories.
         */
        TreeMap<Integer, ArrayList<Category>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<Integer, Category> entry : index.entrySet()) {
            
            if (retVal.containsKey(entry.getValue().getSignificance())) {
                retVal.get(entry.getValue().getSignificance()).add(entry.getValue());
            } else {
                retVal.put(entry.getValue().getSignificance(), new ArrayList<>(Arrays.asList(entry.getValue())));
            }
            
        }
        
        return retVal;
        
    }
    
}
