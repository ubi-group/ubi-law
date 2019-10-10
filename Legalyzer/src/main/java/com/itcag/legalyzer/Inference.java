package com.itcag.legalyzer;

import com.itcag.dlutil.lang.Category;
import com.itcag.util.MathToolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
    
    public TreeMap<Integer, ArrayList<Category>> getRecommendations(Category category) {
        
        /**
         * Key = significance (weight),
         * Value =  list of recommended categories.
         */
        TreeMap<Integer, ArrayList<Category>> retVal = new TreeMap<>(Collections.reverseOrder());
        {
            Double weight = category.getScore();
            weight *= 100;
            weight = MathToolbox.roundDouble(weight, 2);
            int significance = weight.intValue();
            retVal.put(significance, new ArrayList<>(Arrays.asList(category)));
        }
        
        if (isAnchor(category.getIndex())) {
            
            for (Map.Entry<Double, Category> entry : getAnchorTags(category.getIndex()).entrySet()) {
                
                Double weight = entry.getKey();
                
                if (isThematicSibling(category.getIndex(), entry.getValue().getIndex())) {
                    weight *= Config.THEMATIC_SIBLING_FACTOR;
                }
                
                weight *= 100;
                weight = MathToolbox.roundDouble(weight, 2);
                int significance = weight.intValue();

                if (retVal.containsKey(significance)) {
                    retVal.get(significance).add(entry.getValue());
                } else {
                    retVal.put(significance, new ArrayList<>(Arrays.asList(entry.getValue())));
                }
                
            }
            
        }
        
        return retVal;
        
    }

    public TreeMap<Integer, ArrayList<Category>> getRecommendations(ArrayList<Integer> indices) {
        
        return null;
        
    }
    
}
