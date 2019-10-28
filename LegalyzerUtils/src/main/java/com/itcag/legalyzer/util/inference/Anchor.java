package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.cat.Category;

import java.util.Collections;
import java.util.TreeMap;

/**
 * Tag is the category that was manually assigned to a document, and
 * anchor is the category that received the highest score, but it is not the tag.
 */
public class Anchor {
    
    private final Category category;
    
    /**
     * Key = weight (percentage of documents where this category was the anchor for a tag),
     * Value = tag for which this category was the anchor.
     */
    private final TreeMap<Double, Category> tags = new TreeMap<>(Collections.reverseOrder());
    
    public Anchor(Category category) {
        this.category = category;
    }
    
    public Category getCategory() {
        return this.category;
    }
    
    public TreeMap<Double, Category> getTags() {
        return this.tags;
    }
    
    public void addTag(Category category, double weight) {
        this.tags.put(weight, category);
    }
    
}
