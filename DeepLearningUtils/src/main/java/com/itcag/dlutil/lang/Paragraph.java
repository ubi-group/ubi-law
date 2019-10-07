package com.itcag.dlutil.lang;

import com.itcag.dlutil.eval.Result;

public class Paragraph {

    private final String text;
    
    private Result result = null;
    
    public Paragraph(String text) {
        this.text = text;
    }
    
    public String getText() {
        return this.text;
    }
    
    public Result getResult() {
        return this.result;
    }
    
    public void setResult(Result result) {
        this.result = result;
    }
    
}
