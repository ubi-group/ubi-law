package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.Configuration;
import com.itcag.legalyzer.util.MyConfiguration;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Recommendation;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.util.MathToolbox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;

public class Inference {

    private final Ontology ontology;

    private final Configuration config = Configuration.getInstance(MyConfiguration.FILE_NAME);
    
    public Inference(Categories categories) throws Exception {
        this.ontology = new Ontology(categories);
    }
    
    public LinkedHashMap<Integer, Category> getRecommendations(Document document, double threshold) {
        
        LinkedHashMap<Integer, Category> retVal = new LinkedHashMap<>();
        
        TreeMap<Double, Category> tmp = new TreeMap<>();
        
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                
                
                Category highestRanking = sentence.getResult().getHighestRanking();
                if (highestRanking == null) continue;
                
                if (highestRanking.getIndex() <= config.getLastGenericIndex()) {
                    highestRanking = sentence.getResult().getHighestRankingNotGeneric();
                    if (highestRanking == null) continue;
                    if (highestRanking.getScore() < threshold) return retVal;
                }
                
                ArrayList<Ontology.Relation> related = this.ontology.getRelations(highestRanking.getIndex());
                for (Ontology.Relation relation : related) {
                    if (relation.getIndex() == -1) {
                        /**
                         * This is a category that was not included in the net evaluation.
                         */
                        Category category = new Category(relation.getIndex(), relation.getLabel());
                        Recommendation recommendation = new Recommendation(category, relation.getWeight());
                        sentence.addRecommendation(recommendation);
                    } else {
                        Category category = sentence.getResult().getCategory(relation.getIndex());
                        double score = category.getScore() + relation.getWeight();
                        score = MathToolbox.sigmoid(score);
                        Recommendation recommendation = new Recommendation(category, score);
                        sentence.addRecommendation(recommendation);
                    }
                    
                }
                
            }
        }
        
        return retVal;
        
    }
    
}
