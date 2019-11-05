package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class PenaltyExtractor {

    private final HashSet<String> penalties = new HashSet<>();
    
    public PenaltyExtractor() {
        
        penalties.add("מאסר");
        penalties.add("עבודות שירות");
        penalties.add("קנס");
        penalties.add("פיצוי");
        penalties.add("צו מבחן");
        penalties.add("פיקוח שרות המבחן");
        penalties.add("צו של\"צ");
//        penalties.add("");
        
    }
    
    public void extract(Document document) {
        
        boolean trigger = false;
        boolean stopper = false;
        
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                
                if (!trigger) {
                    trigger = isTrigger(sentence);
                    break;
                }
                
                if (!stopper) stopper = isStopper(sentence);

                if (stopper) return;

                if (isPenalty(sentence)) document.addPenalty(sentence.getText());
                
                break;
                
            }
        }
    
    }
    
    private boolean isTrigger(Sentence sentence) {
        
        String pattern = "העונשים הבאים:$";
        Matcher matcher = Pattern.compile(pattern).matcher(sentence.getText());
        while (matcher.find()) {
            return true;
        }
        
        return false;
        
    }

    private boolean isStopper(Sentence sentence) {
        
        String pattern = "ניתן (ה|ב)יום";
        Matcher matcher = Pattern.compile(pattern).matcher(sentence.getText());
        while (matcher.find()) {
            return true;
        }
        
        return false;
        
    }

    private boolean isPenalty(Sentence sentence) {
        
        for (String penalty : penalties) {
            if (sentence.getText().contains(penalty)) return true;
        }
        
        return false;
        
    }
    
}
