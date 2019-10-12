package com.itcag.split;

public final class Dateline {

    public final static void split(StringBuilder input) {
        
        String delimiter = " - ";
        
        int test = input.indexOf(delimiter);
        if (test == -1) return;
        if (test > 50) return;
        
        input.replace(test, test + delimiter.length(), "\n");

    }
    
}
