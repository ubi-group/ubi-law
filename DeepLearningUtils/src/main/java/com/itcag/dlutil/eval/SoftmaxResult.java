package com.itcag.dlutil.eval;

import com.itcag.dlutil.lang.Category;
import com.itcag.dlutil.eval.Result;
import java.util.TreeMap;

public class SoftmaxResult extends Result {
    
    private Category topCategory = null;

    public SoftmaxResult(TreeMap<Integer, Category> categories) throws Exception {
        super(categories);
    }
    
    @Override
    public void setCategoryScore(int index, double score) {
        super.setCategoryScore(index, score);
        if (topCategory == null) {
            topCategory = super.categories.get(index);
        } else if (topCategory.getScore() < score) {
            topCategory = super.categories.get(index);
        }
    }
    
    public Category getTopCategory() {
        return topCategory;
    }

}
