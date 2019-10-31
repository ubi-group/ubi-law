package com.itcag.docanalyzer;

import com.itcag.legalyzer.util.doc.CourtRuling;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Law;
import com.itcag.legalyzer.util.extract.LawExtractor;
import com.itcag.legalyzer.util.extract.RulingExtractor;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Tester {

    public static void main(String[] args) throws Exception {
        
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין 2.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/הכרעת דין פשוטה.docx";
        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/הכרעת דין פשוטה 2.docx";
//        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/פסד מזכה.docx";
        
        testMSWordDocument(filePath);
        
    }
    
    private static void testMSWordDocument(String filePath) throws Exception {

        ArrayList<String> lines = MSWord.parse(filePath);

        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());

        Document document = new Document(lines, new SimpleParser(config));
        
        extractLaws(document);
        extractRulings(document);
        
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
    
}
