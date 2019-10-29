package com.itcag.legalyzer.util.parse;

import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.util.Converter;

import java.util.ArrayList;
import java.util.Properties;

public class Parser {
    
    protected final Integer maxLineLength;
    protected final Integer maxNumParagraphs;
    
    protected final boolean stripOffBullets;
    protected final boolean removeParentheses;
    protected final boolean removeQuotes;
    
    protected final ArrayList<String> triggers = new ArrayList<>();
    protected final ArrayList<String> skippers = new ArrayList<>();
    protected final ArrayList<String> stoppers = new ArrayList<>();
    
    public Parser(Properties config) {

        this.maxLineLength = Converter.convertStringToInteger(config.getProperty(ParserFields.MAX_LINE_LENGTH.getName()));
        this.maxNumParagraphs = Converter.convertStringToInteger(config.getProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName()));

        this.stripOffBullets = Boolean.valueOf(config.getProperty(ParserFields.STRIP_OFF_BULLETS.getName()));
        this.removeParentheses = Boolean.valueOf(config.getProperty(ParserFields.REMOVE_PARENTHESES.getName()));
        this.removeQuotes = Boolean.valueOf(config.getProperty(ParserFields.REMOVE_QUOTES.getName()));

    }
    
    public ArrayList<Paragraph> parse(ArrayList<String> lines) throws Exception {
        
        ArrayList<Paragraph> retVal = new ArrayList<>();
        
        boolean trigger = false;
        
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);

            if (this.stripOffBullets) line = BulletStripper.stripOffBullet(line);

            if (this.removeQuotes) line = QuoteRemover.removeQuotes(line);

            if (this.removeParentheses) line = ParanthesesRemover.removeParantheses(line);

            if (!trigger) {
                if (isTrigger(line)) {
                    trigger = true;
                    continue;
                } else {
                    continue;
                }
            }
            
            if (isStopper(line)) break;
            
            if (!isValid(line)) {
                continue;
            }
            
            if (this.maxLineLength != null && line.length() > this.maxLineLength) line = line.substring(0, this.maxLineLength);
            
            retVal.add(new Paragraph(line));
            if (this.maxNumParagraphs != null && retVal.size() >= this.maxNumParagraphs) break;
            
        }

        return retVal;
        
    }

    private boolean isTrigger(String line) {
        return this.triggers.stream().anyMatch((trigger) -> (line.startsWith(trigger)));
    } 
    
    private boolean isValid(String line) {
        return this.skippers.stream().noneMatch((skipper) -> (line.startsWith(skipper)));
    } 
    
    private boolean isStopper(String line) {
        return this.stoppers.stream().anyMatch((stopper) -> (line.startsWith(stopper)));
    } 
    
}
