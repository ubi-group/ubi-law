package com.itcag.docanalyzer;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;
import java.util.ArrayList;
import java.util.Properties;

public class Tester {

    public static void main(String[] args) throws Exception {
        
        String filePath = "/home/nahum/Desktop/legaltech/test cases for Gai/גזר דין.docx";
        
        testMSWordDocument(filePath);
        
    }
    
    private static void testMSWordDocument(String filePath) throws Exception {

        ArrayList<String> lines = MSWord.parse(filePath);

        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());

        Document document = new Document(lines, new SimpleParser(config));
        for (Paragraph paragraph : document.getParagraphs()) {
            System.out.println(paragraph.getText());
        }

    }
    
}
