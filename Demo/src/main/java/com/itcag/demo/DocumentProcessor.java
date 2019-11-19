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
import java.util.stream.Collectors;
import org.json.JSONObject;

public class DocumentProcessor {
    
    public static com.itcag.legalyzer.util.doc.Document classify(String url) throws Exception {
        
        com.itcag.legalyzer.util.doc.Document document = null;     
        
        try {

            String strDoc = Scraper.getPage(url);
            Document doc = Jsoup.parse(strDoc);
            
            String txt = Scraper.extractText(doc).toString();

            if(txt == null) throw new Exception("Nothing to process");
            
            List<String> lines = new BufferedReader(new StringReader(txt)).lines().collect(Collectors.toList());
          
            Properties config = new Properties();
            config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "1000");
            config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
            config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
            config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());      

            ArrayList<JSONObject> results = SearchIndex.searchIndex(Indices.COURT_RULINGS.getFieldName(), 0, 1, DocumentFields.id.getFieldName(), url);

            if(results.size() > 0) {
                JSONObject jsonDoc = results.get(0);
                document = new com.itcag.legalyzer.util.doc.Document(jsonDoc);    
            } else {
                document = new com.itcag.legalyzer.util.doc.Document(url, new ArrayList(lines), new HCRulingParser(config));                 
                IndexDocument.indexDocument(Indices.COURT_RULINGS.getFieldName(), document.getJSON().toString());
            }

            LegalyzerFactory legalyzerFactory = LegalyzerFactory.getInstance(); 

            Legalyzer legalyzer = legalyzerFactory.getLegalyzer();
            
            legalyzer.evaluate(document);
       
            
        } catch(Exception ex) {
            

        }
        
        return document;
    }
    
}
