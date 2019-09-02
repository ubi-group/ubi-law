package com.itcag.demo.util;

import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.test.Result;
import com.itcag.legalyzer.test.Tester;
import com.itcag.util.txt.TextToolbox;

public class ClassificationCategory {

    private final String label;
    private final String modelPath;
    
    private Tester tester = null;
    
    public ClassificationCategory(String label, String modelPath) {
        this.label = label;
        this.modelPath = modelPath;
    }

    public String getLabel() {
        return label;
    }

    public String getModelPath() {
        return modelPath;
    }

    public Tester getTester() {
        return tester;
    }

    public void setTester(Tester tester) {
        this.tester = tester;
    }

    public boolean test(String sentence) throws Exception {
        
        if (this.tester == null) throw new IllegalStateException("Tester for this category was not loaded.");
        
        if (TextToolbox.isReallyEmpty(sentence)) throw new IllegalArgumentException("Sentence is empty.");
        
        Result result = new Result(WebConstants.CATEGORIES_PATH);
        this.tester.test(sentence, result);
        
        /**
         * 0 category is always "other",
         * 1 category is the classification category.
         */
        return (result.getTopCategory().getIndex() == 1);
    
    }
    
}
