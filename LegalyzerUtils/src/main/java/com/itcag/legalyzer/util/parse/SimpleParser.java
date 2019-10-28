package com.itcag.legalyzer.util.parse;

import com.itcag.legalyzer.util.doc.Paragraph;

import java.util.ArrayList;
import java.util.Properties;

/**
 * Accepts all sentences without inspecting them.
 */
public class SimpleParser extends Parser {
    
    public SimpleParser(Properties config) {
        super(config);
    }
    
    @Override
    public ArrayList<Paragraph> parse(ArrayList<String> lines) throws Exception {
        
        ArrayList<Paragraph> retVal = new ArrayList<>();
        
        for (String line : lines) {
            if (line.isEmpty()) continue;
            retVal.add(new Paragraph(line));
        }
        
        return retVal;
        
    }

}
