package com.itcag.legalyzer;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class Tester {
    
    public static void main(String[] args) throws Exception {

        String corpusIndexPath = "/home/nahum/Desktop/legaltech/verdicts";
        String corpusFolder = "/home/nahum/Desktop/hebrew/high court rulings/";

        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        
        Categories categories = new Categories(categoryFilePath);

        Legalyzer legalyzer = new Legalyzer(categories);

        processFolder(corpusIndexPath, corpusFolder, categories, legalyzer);

//        processDocument("", legalyzer);
        
    }

    private static void processFolder(String corpusIndexPath, String corpusFolder, Categories categories, Legalyzer legalyzer) throws Exception {
        
        DocumentIndex documentIndex = new DocumentIndex(corpusIndexPath, categories);
        
        int count = 0;
        
        for (Map.Entry<String, ArrayList<Integer>> entry : documentIndex.get().entrySet()) {
            try {
                
                count++;
                if (count % 100 != 0) continue;
//                if (count > 10) break;
            
                String fileName = entry.getKey() + ".txt";
System.out.println("----------------------------------------------------------------");
System.out.println("----------------------------------------------------------------");
System.out.println();
//System.out.println(fileName);
for (int index : entry.getValue()) {
    Category category = categories.get().get(index);
    System.out.println("Manualy assigned: " + category.getIndex() + " " + category.getLabel());
}
System.out.println();

                processDocument(corpusFolder + fileName, legalyzer);
                
            } catch (Exception ex) {
                //DO NOTHING!
            }
        }

    }
    
    private static void processDocument(String filePath, Legalyzer legalyzer) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        Properties config = new Properties();
        config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "300");
        config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        Document document = new Document(lines, new HCRulingParser(config));
        
        legalyzer.evaluate(document);

        for (Paragraph paragraph : document.getParagraphs()) {

            for (Sentence sentence : paragraph.getSentences()) {
                System.out.println(sentence.toString());
                System.out.println();
            }
            
        }
        System.out.println();
        
    }
    
}
