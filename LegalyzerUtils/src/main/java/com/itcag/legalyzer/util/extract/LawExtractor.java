package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.extr.Law;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.util.txt.TextToolbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

/**
 * Extracts reference to the specific clauses in the Israeli laws.
 * The reference is extracted also from the aliases
 * (shorter references defined in brackets when a law is first mentioned).
 * Validates laws against a complete list of Israeli laws,
 * which implies that the list of laws must be kept updated.
 */
public class LawExtractor {

    private final HashMap<String, String> laws = new HashMap<>();
    private final HashSet<String> triggers = new HashSet<>();
    private final String[] clauseTriggers = {"סעיף", "סעיפי"};

    private final LinkedList<Law> extractedLaws = new LinkedList<>();

    public LawExtractor() throws Exception {

        /**
         * Load the complete list of Israeli laws.
         */
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("extr/laws")) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.charAt(0) == 8235) line = line.substring(1);
                    if (line.charAt(line.length() - 1) == 8236) line = line.substring(0, line.length() - 1);
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
        
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                insertLawReferences(sentence, document);
                insertClausesByAlias(sentence, document.getLaws());
                insertInformalLawReferences(sentence, document);
            }
        }
    
    }
    
    private void insertLawReferences(Sentence sentence, Document document) {
        
        /**
         * This the safe method to extract a reference to a law,
         * since it extracts the complete name, and then
         * validates it against a list of Israeli laws.
         */
        
        String text = sentence.getText();
        
        for (String clauseTrigger : this.clauseTriggers) {
            
            for (String lawTrigger : this.triggers) {
                
                String pattern = clauseTrigger + ".{1,30}" + " ל" + lawTrigger  + "[^\\d]*?\\d{4}";
                Matcher matcher = Pattern.compile(pattern).matcher(text);
                while (matcher.find()) {
                    
                    String reference = matcher.group();
                    Map.Entry<String, String> entry = parseReference(reference, lawTrigger);
                    String name = entry.getKey();
                    String clause = entry.getValue();
                    
                    String normalized = normalize(name);
                    if (this.laws.containsKey(normalized)) {
                        
                        String officialName = this.laws.get(normalized);
                        if (document.getLaws().containsKey(officialName)) {
                            Law law = document.getLaws().get(officialName);
                            law.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                            law.addClause(clause);
                            if (law.getAlias() == null) insertAlias(sentence, matcher.end(), law);
                            this.extractedLaws.add(law);
                        } else {
                            Law law = new Law(name);
                            if (!officialName.equals(reference)) law.setOfficialName(officialName);
                            law.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                            law.addClause(clause);
                            insertAlias(sentence, matcher.end(), law);
                            document.addLaw(law);
                            this.extractedLaws.add(law);
                        }
                    
                    } else {
                        
                        if (document.getUnknownLaws().containsKey(name)) {
                            Law law = document.getUnknownLaws().get(name);
                            law.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                            law.addClause(clause);
                        } else {
                            Law law = new Law(name);
                            lastResort(name, law);
                            law.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                            law.addClause(clause);
                            document.addLaw(law);
                        }

                    }
                }

            }
        
        }

    }
    
    private void lastResort(String name, Law law) {
        
        if (!name.contains(" התש")) return;
        
        int pos = name.indexOf(" התש");
        name = name.substring(0, pos);
        String normalized = normalize(name);
        if (this.laws.containsKey(normalized)) {
            String officialName = this.laws.get(normalized);
            law.setOfficialName(officialName);
        }
        
    }
    
    private void insertAlias(Sentence sentence, int end, Law law) {
        
        String text = sentence.getText();
        /**
         * Extract only the section following the law.
         */
        text = text.substring(end).trim();
        /**
         * If the law is not immediately followed by an opening parenthesis, there is no alias.
         */
        if (!text.startsWith("(") && !text.startsWith("[")) return;
        
        ArrayList<String> parentheses = TextToolbox.extractParentheses(text, "(", ")");
        if (parentheses.isEmpty()) parentheses = TextToolbox.extractParentheses(text, "[", "]");
        if (parentheses.isEmpty()) return;
        
        /**
         * Only the first parenthesis immediately following the law is of interest.
         */
        String alias = parentheses.get(0);
        
        alias = alias.replace("להלן:", "").trim();
        if (alias.startsWith("ה")) alias = alias.substring(1);

        if (law.getOfficialName() != null) {
            if (!law.getOfficialName().contains(alias)) return;
        } else {
            if (!law.getName().contains(alias)) return;
        }
        
        law.setAlias(alias, sentence.getIndex());
        
    }
    
    private void insertClausesByAlias(Sentence sentence, HashMap<String, Law> laws) {

        String text = sentence.getText();
        
        for (Map.Entry<String, Law> entry : laws.entrySet()) {
            
            String alias = entry.getValue().getAlias();
            if (alias == null) continue;
            
            if (entry.getValue().getAliasIndex() == sentence.getIndex()) continue;
            
            String normalized = alias;
            if (normalized.startsWith("ה")) normalized = normalized.substring(1);
            /**
             * If alias contains parentheses insert escape characters.
             */
            normalized = normalized.replace("(", "\\(").replace(")", "\\)");
            normalized = normalized.replace("[", "\\[").replace("]", "\\]");
            
            for (String trigger : clauseTriggers) {

                String pattern = trigger + ".{1,30}" + normalized;

                Matcher matcher = Pattern.compile(pattern).matcher(text);
                while (matcher.find()) {
                    
                    String clause = matcher.group();
                    /**
                     * Remove the escape characters if any were inserted.
                     */
                    normalized = normalized.replace("\\", "");
                    
                    clause = clause.replace(normalized, "");
                    if (clause.endsWith(" ל")) clause = clause.substring(0, clause.length() - 2);
                    clause = clause.replace("הנ\"ל", "");
                    clause = clause.trim();
                    entry.getValue().addClause(clause);
                    entry.getValue().addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                    this.extractedLaws.add(entry.getValue());
                
                }

            }

        }

    }
    
    private void insertInformalLawReferences(Sentence sentence, Document document) {
        
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
                
                String pattern = clauseTrigger + ".{1,30}" + " ל" + lawTrigger + ".*?" + "[\\.,:;\\)]";
                Matcher matcher = Pattern.compile(pattern).matcher(text);
                while (matcher.find()) {

                    String reference = matcher.group();
                    Map.Entry<String, String> entry = parseReference(reference, lawTrigger);
                    String name = entry.getKey();
                    String clause = entry.getValue();

                    Law law = new Law(name);
                    law.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                    if (isOverlapping(law)) continue;
                    law.addClause(clause);
                    validate(law);
                    if (law.getGuessedOfficialName() == null) infer(law);
                    document.addLaw(law);
                    this.extractedLaws.add(law);

                }

            }
            
        }

    }

    private boolean isOverlapping(Law law) {
        /**
         * This method is necessary, since the a sentence is inspected
         * by multiple methods, and the methods executed later may
         * identify laws that were extracted by previous methods.
         */
        for (Law extracted : this.extractedLaws) {
            for (Law.Position extractedPosition : extracted.getPositions()) {
                Law.Position position = law.getLastPosition();
                if (extractedPosition.getSentenceIndex() == position.getSentenceIndex()) {
                    if (position.getStart() >= extractedPosition.getStart() && position.getStart() <= extractedPosition.getEnd()) return true;
                }
            }
        }
        return false;
    }
    
    private void validate(Law guess) {

        if (!guess.getName().contains(" ")) return;

        for (Map.Entry<String, String> entry : this.laws.entrySet()) {
            if (entry.getKey().contains(guess.getName())) {
                guess.setGuessedOfficialName(entry.getValue());
                break;
            }
        }

    }
    
    private void infer(Law guess) {
        
        if (extractedLaws.isEmpty()) return;
        
        if (extractedLaws.getLast().getOfficialName() != null) {
            guess.setInferredOfficialName(extractedLaws.getLast().getOfficialName());
        } else if (extractedLaws.getLast().getGuessedOfficialName() != null) {
            guess.setInferredOfficialName(extractedLaws.getLast().getGuessedOfficialName());
        } else if (extractedLaws.getLast().getInferredOfficialName() != null) {
            guess.setInferredOfficialName(extractedLaws.getLast().getInferredOfficialName());
        }
        
    }
    
    private Map.Entry<String, String> parseReference(String reference, String lawTrigger) {
        
        int start = reference.indexOf(lawTrigger);

        String law = reference.substring(start, reference.length());
        char c = law.charAt(law.length() - 1);
        if (!Character.isLetterOrDigit(c)) law = law.substring(0, law.length() - 1);
        law = law.trim();

        String clause = reference.substring(0, start - 1);
        clause = clause.trim();
        
        return new AbstractMap.SimpleEntry<>(law, clause);
        
    }
    
    public static String normalize(String name) {
        
        String retVal = name;
        
        retVal = retVal.replace(",", " ");
        retVal = retVal.replace("-", " ");
        retVal = retVal.replace("(", " ");
        retVal = retVal.replace(")", " ");
        retVal = retVal.replace("[", " ");
        retVal = retVal.replace("]", " ");
//        retVal = retVal.replace("", "");
        
        while (retVal.contains("  ")) retVal = retVal.replace("  ", " ");
        
        retVal = retVal.trim();
        
        return retVal;
        
    }
    
}
