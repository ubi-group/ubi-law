package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.CourtRuling;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts mentions of the previous court rulings.
 */
public class RulingExtractor {

    private final HashSet<String> files = new HashSet<>();
    private final HashMap<String, String> ambiguous = new HashMap<>();
    
    public RulingExtractor() throws Exception {
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("extr/files")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.charAt(0) == 8235) line = line.substring(1);
                if (line.charAt(line.length() - 1) == 8236) line = line.substring(0, line.length() - 1);
                if (!line.isEmpty()) files.add(line);
                line = reader.readLine();
            }
        }

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("extr/amb_files")) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (line.charAt(0) == 8235) line = line.substring(1);
                if (line.charAt(line.length() - 1) == 8236) line = line.substring(0, line.length() - 1);
                if (!line.isEmpty()) {
                    String[] elts = line.split("\t");
                    ambiguous.put(elts[0].trim(), elts[1].trim());
                }
                line = reader.readLine();
            }
        }

    }
    
    public void extract(Document document) throws Exception {
        
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                insertCourtRulingReferences(sentence, document);
            }
        }
    
    }
    
    private void insertCourtRulingReferences(Sentence sentence, Document document) throws Exception {
        
        String text = sentence.getText();
        
        String pattern = "\\w+\"\\w{1}" + "\\s" + "\\d+(\\/|-)\\d+" + "((\\/|-)?\\d+)?";
        Matcher matcher = Pattern.compile(pattern, Pattern.UNICODE_CHARACTER_CLASS).matcher(text);
        while (matcher.find()) {
            
            String reference = matcher.group();
            
            String[] elts = reference.split(" ");
            
            String file = elts[0].trim();
            
            if (this.files.contains(file)) {
            
                if (document.getRulings().containsKey(reference)) {
                    CourtRuling ruling = document.getRulings().get(reference);
                    ruling.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                } else {
                    if (this.ambiguous.containsKey(file)) {
                        String alternative = this.ambiguous.get(file);
                        alternative += reference.replace(file, "");
                        CourtRuling ruling = new CourtRuling(reference, alternative);
                        ruling.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                        document.addRuling(ruling);
                    } else {
                        CourtRuling ruling = new CourtRuling(reference);
                        ruling.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                        document.addRuling(ruling);
                    }
                }
                return;
            
            }

            /**
             * The reference could be inflected.
             */
            if (file.startsWith("ב") || file.startsWith("ל") || file.startsWith("מ")) {
            
                file = file.substring(1);
                
                if (this.files.contains(file)) {
                    reference = reference.substring(1);
                    if (document.getRulings().containsKey(reference)) {
                        CourtRuling ruling = document.getRulings().get(reference);
                        ruling.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                    } else {
                        CourtRuling ruling = new CourtRuling(reference);
                        ruling.addPosition(sentence.getParagraphIndex(), sentence.getIndex(), matcher.start(), matcher.end());
                        document.addRuling(ruling);
                    }
                    return;
                }
            
            }

        }
        
    }
    
}
