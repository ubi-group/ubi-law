package com.itcag.dlutil.parse;

import com.itcag.dlutil.lang.Paragraph;
import com.itcag.util.Converter;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;

public class Parser {
    
    protected final ArrayList<String> triggers = new ArrayList<>();
    protected final ArrayList<String> skippers = new ArrayList<>();
    protected final ArrayList<String> stoppers = new ArrayList<>();
    
    private final int maxNumberOfParagraphs;
    private final int maxTextSize;
    
    public Parser(int maxNumberOfParagraphs, int maxTextSize) {
        this.maxNumberOfParagraphs = maxNumberOfParagraphs;
        this.maxTextSize = maxTextSize;
    }
    
    public ArrayList<Paragraph> parse(ArrayList<String> lines) throws Exception {
        
        ArrayList<Paragraph> retVal = new ArrayList<>();
        
        boolean trigger = false;
        int lastLine = 0;

        long textSize = 0;
        
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            
            line = stripOffBullet(line).trim();

            if (!trigger) {
                if (isTrigger(line)) {
                    trigger = true;
                    lastLine = i + this.maxNumberOfParagraphs;
                    continue;
                } else {
                    continue;
                }
            }
            
            if (isStopper(line)) break;
            
            if (!isValid(line)) {
                lastLine++;
                continue;
            }
            
            line = removeQuotes(line);
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            line = TextToolbox.removeParentheses(line.trim(), "(", ")");
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            line = TextToolbox.removeParentheses(line.trim(), "[", "]");
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            retVal.add(new Paragraph(line));
            
            textSize += line.length();
            if (textSize > this.maxTextSize) break;
            
            if (i == lastLine) break;
            
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
    
    private String stripOffBullet(String line) {

        String bullet = ". ";
        
        int end = line.indexOf(bullet);
        if (end == -1) {
            bullet = ") ";
            end = line.indexOf(bullet);
            if (end == -1) return line;
        }
        
        if (end > 4) return line;
        
        String test = line.substring(0, end);
        if (Converter.convertStringToInteger(test) != null) {
            line = line.substring(end + 2);
            return line;
        }
        
        if (end > 3) return line;
        
        for (char c : test.toCharArray()) {
            if (!Character.isLetter(c)) return line;
        }
        
        line = line.substring(end + 2);
        return line;
        
    }
    
    private String removeQuotes(String sentence) {
        
        StringBuilder retVal = new StringBuilder();
        
        for (int i = 0; i < sentence.length(); i++) {
            
            Character pre = null;
            if (i > 0) pre = sentence.charAt(i - 1);
            
            Character post = null;
            if (i < sentence.length() - 2) post = sentence.charAt(i + 1);
            
            String curr = Character.toString(sentence.charAt(i));
            if (curr.equals("\"")) {
                if (pre == null) {
                    /* DO NOTHING */
                } else if (post == null) {
                    /* DO NOTHING */
                } else if (Character.isLetter(pre) && Character.isLetter(post)) {
                    retVal.append(curr);
                } else {
                    /* DO NOTHING */
                }
            } else {
                retVal.append(curr);
            }
        }
        
        return retVal.toString();
    }
    
}
