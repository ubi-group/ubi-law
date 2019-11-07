package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.extr.penalty.CommunityService;
import com.itcag.legalyzer.util.doc.extr.penalty.Compensation;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.extr.penalty.Fine;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.extr.penalty.Penalty;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.doc.extr.penalty.Incarceration;
import com.itcag.legalyzer.util.doc.extr.penalty.Period;
import com.itcag.legalyzer.util.doc.extr.penalty.Probation;
import com.itcag.legalyzer.util.doc.extr.penalty.Term;

import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */
public class PenaltyExtractor {

    private final HashSet<String> finePatterns = new HashSet<>();
    private final HashSet<String> compensationPatterns = new HashSet<>();
    private final HashSet<String> incarcerationPatterns = new HashSet<>();
    private final HashSet<String> probationPatterns = new HashSet<>();
    private final HashSet<String> communityServicePatterns = new HashSet<>();
    private final HashSet<String> suspendedSentencePatterns = new HashSet<>();
    
    
    public PenaltyExtractor() {
        
        finePatterns.add("קנס");
        compensationPatterns.add("פיצוי");
        incarcerationPatterns.add("מאסר");
        probationPatterns.add("צו מבחן");
        probationPatterns.add("פיקוח שרות המבחן");

        communityServicePatterns.add("עבודות שירות");
        communityServicePatterns.add("צו של\"צ");
        communityServicePatterns.add("שירות לתועלת הציבור");
        
        suspendedSentencePatterns.add("לא יישא עונש");
        suspendedSentencePatterns.add("על תנאי");
//        suspendedSentencePatterns.add("");
        
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

                Penalty penalty = getPenalty(sentence); 
                if (penalty != null) {
                    document.addPenalty(penalty);
                }
                
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

    private Penalty getPenalty(Sentence sentence) {

        for (String pattern : finePatterns) {
            if (sentence.getText().contains(pattern)) {
                Penalty retVal = parseFine(sentence.getText());
                return retVal;
            }
        }
        
        for (String pattern : compensationPatterns) {
            if (sentence.getText().contains(pattern)) {
                Penalty retVal = parseCompensation(sentence.getText());
                return retVal;
            }
        }
        
        for (String pattern : incarcerationPatterns) {
            if (sentence.getText().contains(pattern)) {
                Penalty retVal = parseIncarceration(sentence.getText());
                return retVal;
            }
        }
        
        for (String pattern : probationPatterns) {
            if (sentence.getText().contains(pattern)) {
                Penalty retVal = parseProbation(sentence.getText());
                return retVal;
            }
        }
        
        return null;
        
    }

    private Penalty parseIncarceration(String text) {
        
        Term term = getTerm(text);
        if (term == null) return null;

        for (String pattern : communityServicePatterns) {
            if (text.contains(pattern)) {
                Penalty retVal = new CommunityService(term.getDuration(), term.getPeriod());
                return retVal;
            }
        }
        
        for (String pattern : suspendedSentencePatterns) {
            if (text.contains(pattern)) {
                Penalty retVal = new Incarceration(term.getDuration(), term.getPeriod(), true);
                return retVal;
            }
        }
        
        Penalty retVal = new Incarceration(term.getDuration(), term.getPeriod(), false);
        return retVal;
    }
    
    private Penalty parseProbation(String text) {
        
        Term term = getTerm(text);
        if (term == null) return null;
        
        Penalty retVal = new Probation(term.getDuration(), term.getPeriod());
        return retVal;
        
    }
    
    private Penalty parseFine(String text) {
        
        Integer amount = getAmount(text);
        if (amount == null) return null;
        
        Penalty retVal = new Fine(amount);
        return retVal;
        
    }
    
    private Penalty parseCompensation(String text) {
        
        Integer amount = getAmount(text);
        if (amount == null) return null;
        
        Penalty retVal = new Compensation(amount);
        return retVal;
        
    }

    private Term getTerm(String text) {
        
        int pos = text.indexOf(",");
        if (pos > -1) text = text.substring(0, pos);

        String pattern;
        Matcher matcher;
        
        pattern = "יומיים";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            return new Term(2.00, Period.DAY);
        }
        
        pattern = "\\d+ יום";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.DAY);
        }
        
        pattern = "\\d+ ימים?";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.DAY);
        }
        
        pattern = "חודשיים";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            return new Term(2.00, Period.MONTH);
        }
        
        pattern = "\\d+ חודשים?";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.MONTH);
        }
        
        pattern = "\\d+ חודש";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.MONTH);
        }
        
        pattern = "שנתיים";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            return new Term(2.00, Period.YEAR);
        }
        
        pattern = "\\d+ (שנים|שנות|שנה)";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.YEAR);
        }
    
        /*
        pattern = "";
        matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String[] elts = matcher.group().split(" ");
            double duration = Double.parseDouble(elts[0]);
            return new Term(duration, Period.YEAR);
        }
        */
        
        return null;
        
    }
    
    private Integer getAmount(String text) {
        
        int pos = text.indexOf("₪");
        if (pos == -1) pos = text.indexOf("שקל");
        if (pos > -1) text = text.substring(0, pos);

        String pattern = "(\\d|\\.|,)+";
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        while (matcher.find()) {
            String digits = matcher.group();
            digits = digits.replace(",", "");
            pos = digits.indexOf(".");
            if (pos > -1) digits = digits.substring(0,  pos);
            return Integer.parseInt(digits);
        }
        
        return null;
        
    }
    
}
