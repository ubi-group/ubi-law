package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.cat.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Tag is a manually selected category assigned to a document.
 * Anchor is a category that received the highest score, but is not the tag of that document. 
 * The current class holds all anchors of a tag.
 */
public class Tag {

    private final Category category;
    
    private final HashMap<Integer, TopCategory> anchors = new HashMap<>();
    
    public Tag(Category category) {
        this.category = category;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public int getindex() {
        return this.category.getIndex();
    }
    
    public String getLabel() {
        return this.category.getLabel();
    }

    public HashMap<Integer, TopCategory> getAnchors() {
        return this.anchors;
    }
    
    public void addAnchor(Category category, double score) {
        if (this.anchors.containsKey(category.getIndex())) {
            this.anchors.get(category.getIndex()).setScore(score);
        } else {
            TopCategory topCategory = new TopCategory(category);
            topCategory.setScore(score);
            this.anchors.put(category.getIndex(), topCategory);
        }
    }
    
    public TreeMap<Integer, TopCategory> getAnchors(int n, boolean excludeTag) {
        
        /**
         * Key = frequency of occurrence (how many times was this the top category).
         */
        TreeMap<Integer, TopCategory> retVal = new TreeMap<>(Collections.reverseOrder());
        
        /**
         * Select anchors descending by the frequency of occurrence.
         */
        TreeMap<Integer, ArrayList<TopCategory>> tmp = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<Integer, TopCategory> entry : this.anchors.entrySet()) {
            if (tmp.containsKey(entry.getValue().getFoo())) {
                tmp.get(entry.getValue().getFoo()).add(entry.getValue());
            } else {
                tmp.put(entry.getValue().getFoo(), new ArrayList<>(Arrays.asList(entry.getValue())));
            }
        }
        
        /**
         * Select only the top n categories and ignore the rest.
         * If required, exclude the tag.
         */
        for (Map.Entry<Integer, ArrayList<TopCategory>> entry : tmp.entrySet()) {
            for (TopCategory topCategory : entry.getValue()) {
                if (excludeTag && topCategory.getCategory().getIndex() == this.category.getIndex()) continue;
                retVal.put(entry.getKey(), topCategory);
            }
            if (retVal.size() >= n) break;
        }
        
        return retVal;
        
    }
    
    public int getTotal() {
        int retVal = 0;
        for (Map.Entry<Integer, TopCategory> entry : this.anchors.entrySet()) {
            retVal += entry.getValue().getFoo();
        }
        return retVal;
    }
    
}
