package com.itcag.legalyzer;

import com.itcag.legalyzer.util.inference.Inference;
import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.extract.LawExtractor;
import com.itcag.legalyzer.util.extract.PenaltyExtractor;
import com.itcag.legalyzer.util.extract.PersonnelExtractor;
import com.itcag.legalyzer.util.extract.RulingExtractor;
import com.itcag.util.Printer;
import java.util.Properties;

public class Legalyzer {

    public enum ExtractionOptions {
        
        LAW("law"),
        RULINGS("rulings"),
        PERSONNEL("personnel"),
        PENALTY("penalty"),
        
        ;
        
        private final String name;
        
        private ExtractionOptions(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
    
    }
    
    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    public Legalyzer(Categories categories) throws Exception {
        
        this.tester = new Tester();
        this.categories = categories;

        this.inference = new Inference(categories);
        
    }
    
    public void evaluate(Document document) throws Exception {
        this.tester.testSentences(document);
    }
    
    public void recommend(Document document) throws Exception {
        this.tester.testSentences(document);
        this.inference.getRecommendations(document, 0);
    }

    public void extract(Document document, Properties config) throws Exception {
        
        if (config.containsKey(ExtractionOptions.LAW.getName())) {
            LawExtractor extractor = new LawExtractor();
            extractor.extract(document);
        }
        
        if (config.containsKey(ExtractionOptions.RULINGS.getName())) {
            RulingExtractor extractor = new RulingExtractor();
            extractor.extract(document);
        }
        
        if (config.containsKey(ExtractionOptions.PERSONNEL.getName())) {
            PersonnelExtractor extractor = new PersonnelExtractor(Document.Type.HIGH_COURT_RULING);
            extractor.extract(document);
        }
        
        if (config.containsKey(ExtractionOptions.PENALTY.getName())) {
            PenaltyExtractor extractor = new PenaltyExtractor();
            extractor.extract(document);
        }
        
    }
    
    public Categories getCategories() {
        return this.categories;
    }
    
}
