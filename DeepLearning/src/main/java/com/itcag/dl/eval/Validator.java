package com.itcag.dl.eval;

import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

/**
 * Validates neural net tags against the manual tags.
 */
public class Validator {

    public static void main(String[] args) throws Exception {
        
        String verdictsFilePath = "/home/nahum/Desktop/legaltech/verdicts";
        String folderPath = "/home/nahum/Desktop/hebrew/high court rulings/";
        
        Validator validator = new Validator(verdictsFilePath, folderPath);
        validator.validate();
        
    }
    
    private class Verdict {
        
        private final String id;
        private final String url;
        private final String primary;
        private final String secondary;
    
        private Verdict(String line) {
            
            if (line.isEmpty()) throw new IllegalArgumentException("Empty line.");

            String[] elts = line.split("\t");
            if (elts.length != 4) throw new IllegalArgumentException("Invalid line: " + line);
            
            this.id = elts[0].trim();
            this.url = elts[1].trim();
            this.primary = elts[2].trim();
            
            if (!elts[3].trim().isEmpty()) {
                this.secondary = elts[3].trim();
            } else {
                this.secondary = null;
            }
            
        }

        private String getId() {
            return id;
        }

        private String getUrl() {
            return url;
        }

        private String getPrimary() {
            return primary;
        }

        private String getSecondary() {
            return secondary;
        }
        
    }
    
    private final String folderPath;
    
    private final ArrayList<Verdict> verdicts = new ArrayList<>();
    
    public Validator(String verdictsFilePath, String folderPath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(verdictsFilePath);
        for (String line : lines) {
            try {
                Verdict verdict = new Verdict(line);
                verdicts.add(verdict);
            } catch (Exception ex) {
                Printer.print(ex.getMessage());
            }
        }
        
        this.folderPath = folderPath;
    
    }
    
    private void validate() throws Exception {

        Tester tester = new Tester();
        
        for (Verdict verdict : this.verdicts) {
            
            String filePath = this.folderPath + verdict.id + ".txt";
            ArrayList<String> lines = TextFileReader.read(filePath);

            Properties config = new Properties();
            config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "1000");
            config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
            config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
            
            Document document = new Document(lines, new HCRulingParser(config));
            
            tester.testSentences(document);
        
            LinkedHashMap<Integer, Category> aggregation = new LinkedHashMap<>();
            
            for (Paragraph paragraph : document.getParagraphs()) {
                accumulate(paragraph.getEvaluation(tester.getCategories().get()), aggregation);
            }

            if (aggregation.isEmpty()) continue;
            
            TreeMap<Double, Category> sorted = new TreeMap<>(Collections.reverseOrder());
            
            for (Map.Entry<Integer, Category> entry : aggregation.entrySet()) {
                sorted.put(entry.getValue().getScore(), entry.getValue());
            }
            
            Category top = sorted.firstEntry().getValue();
            String left  = Category.normalizeLabel(top.getLabel());
            String right = Category.normalizeLabel(verdict.primary);
            if (left.equals(right)) Printer.print(verdict.primary + "\t" + verdict.url);
            
        }
        
    }
    
    private void accumulate(LinkedHashMap<Integer, Category> evaluation, LinkedHashMap<Integer, Category> aggregation) {
        
        for (Map.Entry<Integer, Category> entry : evaluation.entrySet()) {
            
            if (aggregation.containsKey(entry.getKey())) {
                double score = aggregation.get(entry.getKey()).getScore();
                score += entry.getValue().getScore();
                aggregation.get(entry.getKey()).setScore(score);
            } else {
                aggregation.put(entry.getValue().getIndex(), entry.getValue());
            }
            
        }
        
    }
    
}
