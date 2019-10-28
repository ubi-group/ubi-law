package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.eval.Result;

import java.util.ArrayList;

public interface Text {

    public String getText();

    public Result getResult();
    public void setResult(Result result);

    public ArrayList<Recommendation> getRecommendations();
    public void addRecommendation(Recommendation recommendation);

    
}
