package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.parse.Parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class Document {

    private final String id = UUID.randomUUID().toString().replace("-", "");
    
    private final ArrayList<String> lines;
    
    private ArrayList<Paragraph> paragraphs = new ArrayList<>();
    
    private int length = 0;
    
    public Document(ArrayList<String> lines, Parser parser) throws Exception {
        this.lines = lines;
        this.paragraphs = parser.parse(this.lines);
        this.length = this.paragraphs.stream().map((paragraph) -> paragraph.getText().length()).reduce(0, Integer::sum);
    }

    public String getId() {
        return this.id;
    }
    
    public ArrayList<Paragraph> getParagraphs() {
        return this.paragraphs;
    }
    
    public TreeMap<Double, ArrayList<Category>> evaluate(TreeMap<Integer, Category> categories) {
        
        TreeMap<Double, ArrayList<Category>> retVal = new TreeMap<>(Collections.reverseOrder());

        /**
         * Key is the category index, and the value is the category score (incremented).
         */
        HashMap<Integer, Double> scores = aggregateScores();
        TreeMap<Double, HashSet<Integer>> inverted = invertScores(scores);
        
        for (Map.Entry<Double, HashSet<Integer>> entry : inverted.entrySet()) {
            for (Integer index : entry.getValue()) {
                Category category = categories.get(index);
                if (retVal.containsKey(entry.getKey())) {
                    retVal.get(entry.getKey()).add(category);
                } else {
                    retVal.put(entry.getKey(), new ArrayList<>(Arrays.asList(category)));
                }
            }
        }
        
        return retVal;
        
    }
    
    private HashMap<Integer, Double> aggregateScores() {
        
        HashMap<Integer, Double> retVal = new HashMap<>();
        
        for (Paragraph paragraph : this.paragraphs) {
            
            for (Map.Entry<Integer, Category> entry : paragraph.getResult().getCategories().entrySet()) {
        
                Category category = entry.getValue();
                
                if (category.getScore() != null) {
                    
                    if (retVal.containsKey(category.getIndex())) {
                        retVal.put(category.getIndex(), retVal.get(category.getIndex()) + category.getScore());
                    } else {
                        retVal.put(category.getIndex(), category.getScore());
                    }
                    
                }
                
            }
        
        }
        
        return retVal;

    }
    
    private TreeMap<Double, HashSet<Integer>> invertScores(HashMap<Integer, Double> scores) {
        
        TreeMap<Double, HashSet<Integer>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<Integer, Double> entry : scores.entrySet()) {
            
            if (retVal.containsKey(entry.getValue())) {
                retVal.get(entry.getValue()).add(entry.getKey());
            } else {
                retVal.put(entry.getValue(), new HashSet<>(Arrays.asList(entry.getKey())));
            }
            
        }
        
        return retVal;
        
    }
    
    public int length() {
        return this.length;
    }
    
}
