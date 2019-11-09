package com.itcag.demo;

import com.itcag.datatier.IndexDocument;
import com.itcag.datatier.SearchIndex;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.itcag.scraper.court_rulings.Scraper;

import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.DocumentFields;
import com.itcag.legalyzer.Legalyzer;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.json.JSONObject;

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
            
            com.itcag.legalyzer.util.doc.Document document;           

            ArrayList<JSONObject> results = SearchIndex.searchIndex(Indices.COURT_RULINGS.getFieldName(), 0, 1, DocumentFields.id.getFieldName(), url);
            if(results.size() > 0) {
                System.out.println("DOC FOUND...");
                JSONObject jsonDoc = results.get(0);
                document = new com.itcag.legalyzer.util.doc.Document(jsonDoc);    
            } else {
                document = new com.itcag.legalyzer.util.doc.Document(url, new ArrayList(lines), new HCRulingParser(config));                 
                IndexDocument.indexDocument(Indices.COURT_RULINGS.getFieldName(), document.getJSON().toString());
            }
            
System.out.println("Starting Legalyzer...");
long startTime = System.nanoTime();

            LegalyzerFactory legalyzerFactory = LegalyzerFactory.getInstance(); 
long endTime = System.nanoTime();
long durationInNano = (endTime - startTime);  //Total execution time in nano seconds
System.out.println("duration in milis: " + TimeUnit.NANOSECONDS.toSeconds(durationInNano));
            Legalyzer legalyzer = legalyzerFactory.getLegalyzer();
            legalyzer.evaluate(document);
long endTime2 = System.nanoTime();            
long durationInNano2 = (endTime2 - endTime);  //Total execution time in nano seconds     
System.out.println("duration of evaluation in milis: " + TimeUnit.NANOSECONDS.toSeconds(durationInNano2));
/*            
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
                    paragraph.getSentences();
                    System.out.println("\t" + entry.getKey() + "\t" + entry.getValue().getIndex() + "\t" + entry.getValue().getLabel());
                    System.out.println();
                }

            }            
*/            
            
        } catch(Exception ex) {
            
        }
    }
    
}
