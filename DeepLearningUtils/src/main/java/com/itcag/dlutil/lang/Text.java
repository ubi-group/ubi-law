package com.itcag.dlutil.lang;

import com.itcag.dlutil.eval.Result;

import java.util.ArrayList;

public interface Text {

    public String getText();

    public Result getResult();
    public void setResult(Result result);

    public ArrayList<Recommendation> getRecommendations();
    public void addRecommendation(Recommendation recommendation);

    
}
