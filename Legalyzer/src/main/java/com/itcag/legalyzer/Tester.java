package com.itcag.legalyzer;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Recommendation;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.doc.extr.CourtRuling;
import com.itcag.legalyzer.util.doc.extr.Law;
import com.itcag.legalyzer.util.doc.extr.Person;
import com.itcag.legalyzer.util.doc.extr.penalty.Penalty;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;
import java.io.File;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Tester {
    
    public static void main(String[] args) throws Exception {

        String folderPath = "/home/nahum/Desktop/hebrew/high court rulings/";

        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        
        Categories categories = new Categories(categoryFilePath);

        Legalyzer legalyzer = new Legalyzer(categories);

        processFolder(folderPath, legalyzer);

//        processDocument("", legalyzer);
        
    }

    private static void processFolder(String folderPath, Legalyzer legalyzer) throws Exception {

        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            processFile(file, legalyzer);
        }

    }
    
    private static void processFile(File file, Legalyzer legalyzer) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(file.getPath());
        
        String id = file.getName().replace(".txt", "");
        
        Properties config = new Properties();
        config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "1000");
        config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        Document document = new Document(id, lines, new HCRulingParser(config));
        
//        legalyzer.evaluate(document);
        legalyzer.recommend(document);
        
        for (Paragraph paragraph : document.getParagraphs()) {

            for (Sentence sentence : paragraph.getSentences()) {
                
                if (sentence.getResult() != null && sentence.getResult().getHighestRanking() != null) {
                    
                    if (sentence.getResult().getHighestRanking().getScore() > 0.5) {

                        if (sentence.getResult().getHighestRanking().getIndex() != 0) {
                            
                            Printer.print(sentence.getText());
                            Printer.print(document.getId());
                            Printer.print(sentence.getResult().getHighestRanking().toString());

                            for (Recommendation recommendation : sentence.getRecommendations()) {
                                if (recommendation.getValue() > 0.70) {
                                    Printer.print("\t" + recommendation.toString());
                                }
                            }

                            Printer.print();
                        
                        }

                    }

                }
                
            }
            
        }
        
        Properties extractionConfig = new Properties();
        extractionConfig.put(Legalyzer.ExtractionOptions.LAW.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.RULINGS.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.PERSONNEL.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.PENALTY.getName(), Boolean.TRUE);
        legalyzer.extract(document, extractionConfig);

        if (!document.getJudges().isEmpty()) {
            Printer.print("Judges:");
            for (Person person : document.getJudges()) {
                Printer.print(person.toString());
            }
            Printer.print();
        }
        
        if (!document.getPlaintiffAttorneys().isEmpty()) {
            Printer.print("Plaintiff attorneys:");
            for (Person person : document.getPlaintiffAttorneys()) {
                Printer.print(person.toString());
            }
            Printer.print();
        }
        
        if (!document.getDefendantAttorneys().isEmpty()) {
            Printer.print("Defendant attorneys:");
            for (Person person : document.getDefendantAttorneys()) {
                Printer.print(person.toString());
            }
            Printer.print();
        }
        
        if (!document.getLaws().isEmpty()) {
            Printer.print("Referenced laws:");
            for (Map.Entry<String, Law> entry : document.getLaws().entrySet()) {
                Printer.print(entry.getValue().toString());
            }
            Printer.print();
        }
        
        if (!document.getRulings().isEmpty()) {
            Printer.print("Referenced court rulings:");
            for (Map.Entry<String, CourtRuling> entry : document.getRulings().entrySet()) {
                Printer.print(entry.getValue().toString());
            }
            Printer.print();
        }
        
        if (!document.getPenalties().isEmpty()) {
            Printer.print("Referenced court rulings:");
            for (Penalty penalty : document.getPenalties()) {
                Printer.print(penalty.toString());
            }
            Printer.print();
        }
        
    }
    
}
