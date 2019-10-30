package com.itcag.docanalyzer;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Law;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.extract.LawExtractor;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Tester {

    public static void main(String[] args) throws Exception {
        
        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/הכרעת דין פשוטה.docx";
        
        testMSWordDocument(filePath);
        
    }
    
    private static void testMSWordDocument(String filePath) throws Exception {

        LawExtractor extractor = new LawExtractor();
        
        ArrayList<String> lines = MSWord.parse(filePath);

        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());

        Document document = new Document(lines, new SimpleParser(config));
        extractor.extract(document);

        for (Map.Entry<String, Law> entry : document.getLaws().entrySet()) {
            System.out.println(entry.getValue().toString());
        }
        
    }
    
}
