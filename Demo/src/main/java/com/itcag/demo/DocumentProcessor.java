package com.itcag.demo;

import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.itcag.scraper.court_rulings.Scraper;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class DocumentProcessor {
    
    public static void classify(String url) throws Exception {
        
        try {

            String strDoc = Scraper.getPage(url);
            Document doc = Jsoup.parse(strDoc);
            String txt = Scraper.extractText(doc).toString();

            if(txt == null) throw new Exception("Nothing to process");
            
            List<String> lines = new BufferedReader(new StringReader(txt)).lines().collect(Collectors.toList());

            Properties config = new Properties();
            config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "300");
            config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
            config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
            com.itcag.legalyzer.util.doc.Document document = new com.itcag.legalyzer.util.doc.Document(new ArrayList(lines), new HCRulingParser(config));           
            
            TesterFactory testerFactory = TesterFactory.getInstance();
            Tester tester = testerFactory.getTester();
            tester.testSentences(document);
            
            Categories categories = new Categories(Config.CATEGORIES_PATH); 
            
            for (Paragraph paragraph : document.getParagraphs()) {             
                paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
            }

            ArrayList<Paragraph> paras = document.getParagraphs();
            for (Paragraph paragraph : paras) {
                tester.test(paragraph.getText(), paragraph.getResult());
                for (Map.Entry<Integer, Category> entry : paragraph.getResult().getCategoriesSortedByScore().entrySet()) {
                    System.out.println(paragraph.getText());
                    System.out.println("\t" + entry.getKey() + "\t" + entry.getValue().getIndex() + "\t" + entry.getValue().getLabel());
                    System.out.println();
                }

            }            
            
            
        } catch(Exception ex) {
            
        }
    }
    
}
