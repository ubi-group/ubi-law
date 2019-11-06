package com.itcag.docanalyzer;

import com.itcag.legalyzer.util.doc.CourtRuling;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Law;
import com.itcag.legalyzer.util.doc.Person;
import com.itcag.legalyzer.util.doc.penalty.Penalty;
import com.itcag.legalyzer.util.extract.LawExtractor;
import com.itcag.legalyzer.util.extract.PenaltyExtractor;
import com.itcag.legalyzer.util.extract.PersonnelExtractor;
import com.itcag.legalyzer.util.extract.RulingExtractor;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;

import com.itcag.util.io.TextFileReader;
import java.io.File;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Tester {

    public static void main(String[] args) throws Exception {
        
        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין.docx";

//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין 2.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/הכרעת דין פשוטה.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/הכרעת דין פשוטה 2.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/פסד מזכה.docx";

//        testPlainTextDocument(filePath);
        testMSWordDocument(filePath);

//        processFolder("/home/nahum/Desktop/hebrew/high court rulings/");
//        processFolder("/home/nahum/Desktop/legaltech/test cases for Gai/");

    }
    
    private static void processFolder(String folderPath) throws Exception {
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            if (file.getName().startsWith(".~")) continue;
            System.out.println(file.getName());
//            testPlainTextDocument(file.getPath());
            testMSWordDocument(file.getPath());
            System.out.println();
        }

    }
    
    private static void testPlainTextDocument(String filePath) throws Exception {

        ArrayList<String> lines = TextFileReader.read(filePath);
        
        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());

        Document document = new Document(lines, new SimpleParser(config));
        
//        extractPersonnel(document, Document.Type.CRIMINAL_RULING);
        extractLaws(document);
//        extractRulings(document);
//        extractPenalties(document);
        
    }
    
    private static void testMSWordDocument(String filePath) throws Exception {

        ArrayList<String> lines = MSWord.parse(filePath);

        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());

        Document document = new Document(lines, new SimpleParser(config));
        
//        extractPersonnel(document, Document.Type.CRIMINAL_RULING);
//        extractLaws(document);
//        extractRulings(document);
        extractPenalties(document);
        
    }
    
    private static void extractPersonnel(Document document, Document.Type type) throws Exception {
        
        PersonnelExtractor extractor = new PersonnelExtractor(type);
        
        extractor.extract(document);

        for (Person person : document.getJudges()) {
            System.out.println(person.toString());
        }
        
        for (Person person : document.getPlaintiffAttorneys()) {
            System.out.println(person.toString());
        }
        
        for (Person person : document.getDefendantAttorneys()) {
            System.out.println(person.toString());
        }
        
    }
    
    private static void extractLaws(Document document) throws Exception {
        
        LawExtractor extractor = new LawExtractor();
        
        extractor.extract(document);

        for (Map.Entry<String, Law> entry : document.getLaws().entrySet()) {
            System.out.println(entry.getValue().toString());
        }
        
    }
    
    private static void extractRulings(Document document) throws Exception {
        
        RulingExtractor extractor = new RulingExtractor();
        
        extractor.extract(document);

        for (Map.Entry<String, CourtRuling> entry : document.getRulings().entrySet()) {
            System.out.println(entry.getValue().toString());
        }
        
    }
    
    private static void extractPenalties(Document document) throws Exception {
        
        PenaltyExtractor extractor = new PenaltyExtractor();
        
        extractor.extract(document);
        
        for (Penalty penalty : document.getPenalties()) {
            System.out.println(penalty.toString());
        }

    }
    
}
