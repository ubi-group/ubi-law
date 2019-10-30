package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Law;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Extracts reference to the Israeli laws (and the specific clauses, if any are given).
 * The reference is extracted also from the aliases
 * (shorter references defined in brackets when a law is first mentioned).
 * Assumes a standard reference format.
 * Extracts only recognized laws, which implies that the list of laws must be kept updated.
 */
public class LawExtractor {

    private final HashMap<String, String> laws = new HashMap<>();
    private final HashSet<String> triggers = new HashSet<>();
    private final String[] clauseTriggers = {"סעיף", "סעיפי"};
        

    public LawExtractor() throws Exception {

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("extr/laws")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                if (!line.isEmpty()) {
                    String[] elts = line.split("\t");
                    String normalized = normalize(elts[0]);
                    laws.put(normalized, elts[1].trim());
                    String[] words = elts[0].split(" ");
                    this.triggers.add(words[0]);
                }
                line = reader.readLine();
            }
        }

    }

    public void extract(Document document) {
        
        /**
         * Identify laws in the text.
         * Extract aliases, if any are given.
         */
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                insertLawReferences(sentence, document);
            }
        }
    
        /**
         * Identify clauses for the identified laws, if any given.
         */
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                insertClauses(sentence, document.getLaws());
            }
        }

        /**
         * Identify clauses using aliases.
         */
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                insertClausesByAlias(sentence, document.getLaws());
            }
        }
        
for (Paragraph paragraph : document.getParagraphs()) {
    for (Sentence sentence : paragraph.getSentences()) {
        insertClausesOfInformalLawReferences(sentence, document);
    }
}

    }
    
    private void insertLawReferences(Sentence sentence, Document document) {
        
        /**
         * This the safe method to extract a reference to a law,
         * since it extracts the complete name, and then
         * validates it against a list of Israeli laws.
         */
        
        for (String trigger : this.triggers) {
            
            String pattern = trigger + "[^\\d]*\\d{4}";
            Matcher matcher = Pattern.compile(pattern).matcher(sentence.getText());
            while (matcher.find()) {
                String name = matcher.group();
                String normalized = normalize(name);
                if (this.laws.containsKey(normalized)) {
                    String officialName = this.laws.get(normalized);
                    if (document.getLaws().containsKey(officialName)) continue;
                    Law law;
                    if (officialName.equals(name)) {
                        law = new Law(name);
                    } else {
                        law = new Law(name, officialName);
                    }
                    document.addLaw(law);
                    insertAlias(sentence, matcher.end(), law);
                } else {
                    System.out.println("UNKNOWN LAW: " + name);
                    System.out.println();
                }
            }

        }

    }
    
    private void insertAlias(Sentence sentence, int end, Law law) {
        
        String text = sentence.getText();
        
        int start = text.indexOf("(", end);
        if (start == -1) return;
        
        end = text.indexOf(")", start);
        if (end == -1) return;
        
        String alias = text.substring(start + 1, end);
        alias = alias.replace("להלן:", "").trim();
        
        law.setAlias(alias, sentence.getIndex());
        
    }
    
    private void insertClauses(Sentence sentence, HashMap<String, Law> laws) {
        
        String text = sentence.getText();
        
        for (String trigger : this.clauseTriggers) {
            
            String pattern = trigger + ".*?\\d{4}";
            Matcher matcher = Pattern.compile(pattern).matcher(text);
            while (matcher.find()) {
                String clause = matcher.group();
                for (Map.Entry<String, Law> entry : laws.entrySet()) {
                    String name = entry.getValue().getName();
                    if (clause.contains(name)) {
                        clause = clause.replace(name, "");
                        if (clause.endsWith(" ל")) clause = clause.substring(0, clause.length() - 2);
                        clause = clause.trim();
                        entry.getValue().addClause(clause);
                    }
                }
            }
            
        }

    }
    
    private void insertClausesOfInformalLawReferences(Sentence sentence, Document document) {
        
        /**
         * This is a guessing method to extract a law reference.
         * It extract mention of a clause followed by a possible
         * mention of a law, and the extracts the segment until
         * a punctuation mark.
         * The part of the segment from the possible mention of
         * a law until the end is considered to be the name of the law,
         * and is checked against the list of the laws - hoping to match it
         * to one of them.
         */
        
        String text = sentence.getText();
        
        for (String clauseTrigger : this.clauseTriggers) {
            
            for (String lawTrigger : this.triggers) {
                
                String pattern = clauseTrigger + ".*?" + " ל" + lawTrigger + ".*?" + "[\\.,:;\\)]";
                Matcher matcher = Pattern.compile(pattern).matcher(text);
                while (matcher.find()) {
                    String clause = matcher.group();
                    int start = clause.indexOf(lawTrigger);
                    String law = clause.substring(start, clause.length() - 1);
                    law = law.trim();
                    clause = clause.substring(0, start - 1);
//                    if (clause.endsWith(" ל")) clause = clause.substring(0, clause.length() - 2);
                    clause = clause.trim();
                    String officialName = null;
                    for (Map.Entry<String, String> entry : this.laws.entrySet()) {
                        if (entry.getKey().contains(law)) {
                            officialName = entry.getValue();
                            break;
                        }
                    }
System.out.println(clause);
if (officialName != null) {
    System.out.println(law + " (probably: " + officialName + ")");
} else {
    System.out.println(law);
}
System.out.println();

                }

            }
            
        }

    }
    
    private void insertClausesByAlias(Sentence sentence, HashMap<String, Law> laws) {

        String text = sentence.getText();
        
        for (Map.Entry<String, Law> entry : laws.entrySet()) {
            
            String alias = entry.getValue().getAlias();
            if (alias == null) continue;
            
            if (entry.getValue().getAliasIndex() == sentence.getIndex()) continue;
            
            String normalized = alias;
            if (normalized.startsWith("ה")) normalized = normalized.substring(1);
            
            for (String trigger : clauseTriggers) {

                String pattern = trigger + ".*?" + normalized;

                Matcher matcher = Pattern.compile(pattern).matcher(text);
                while (matcher.find()) {
                    String clause = matcher.group();
                    clause = clause.replace(normalized, "");
                    if (clause.endsWith(" ל")) clause = clause.substring(0, clause.length() - 2);
                    clause = clause.trim();
                    entry.getValue().addClause(clause);
                }

            }

        }

    }
    
    private String normalize(String name) {
        
        String retVal = name;
        
        retVal = retVal.replace(",", " ");
        retVal = retVal.replace("-", " ");
        retVal = retVal.replace("(", " ");
        retVal = retVal.replace(")", " ");
        retVal = retVal.replace("[", " ");
        retVal = retVal.replace("]", " ");
        
        while (retVal.contains("  ")) retVal = retVal.replace("  ", " ");
        
        retVal = retVal.trim();
        
        return retVal;
        
    }
    
}
