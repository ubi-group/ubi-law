package com.itcag.legalyzer.util.parse;

import com.itcag.legalyzer.util.doc.Paragraph;

import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

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
        
        int count = 0;
        AtomicInteger sentenceIndex = new AtomicInteger(0);
        
        for (String line : lines) {

            if (line.isEmpty()) continue;

            if (super.stripOffBullets) line = BulletStripper.stripOffBullet(line);

            if (super.removeQuotes) line = QuoteRemover.removeQuotes(line);

            if (super.removeParentheses) line = ParanthesesRemover.removeParantheses(line);

            retVal.add(new Paragraph(line, count++, sentenceIndex));
        }
        
        return retVal;
        
    }

}
