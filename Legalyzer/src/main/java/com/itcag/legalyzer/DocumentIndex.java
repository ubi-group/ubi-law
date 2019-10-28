package com.itcag.legalyzer;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Loads a list of documents from a text file, and enables easy access to the list items.
 * A list item consists of a document ID (also used as the document file name),
 * the original URL from which the document was scraped, and the tags
 * that were manually assigned to the document.
 */
public class DocumentIndex {
    
    /**
     * Key = document ID,
     * Value = list of category indices.
     */
    private final HashMap<String, ArrayList<Integer>> docs = new HashMap<>();

    public DocumentIndex(String documentIndexFilePath, Categories categories) throws Exception {
        load(documentIndexFilePath, categories);
    }
    
    private void load(String documentIndexFilePath, Categories categories) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(documentIndexFilePath);
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            
            String id = elts[0].trim();
            
            Integer tag1 = null;
            Integer tag2 = null;

            String tagName1 = null;
            String tagName2 = null;

            if (elts.length > 2 && !elts[2].trim().isEmpty()) {
                tagName1 = elts[2].trim().replace(",", "").replace(" ", "_");
                for (Map.Entry<Integer, Category> entry : categories.get().entrySet()) {
                    if (entry.getValue().getLabel().equalsIgnoreCase(tagName1)) tag1 = entry.getKey();
                }
            }

            if (elts.length > 3 && !elts[3].trim().isEmpty()) {
                tagName2 = elts[3].trim().replace(",", "").replace(" ", "_");
                for (Map.Entry<Integer, Category> entry : categories.get().entrySet()) {
                    if (entry.getValue().getLabel().equalsIgnoreCase(tagName2)) tag2 = entry.getKey();
                }
            }
            
            ArrayList<Integer> tmp = new ArrayList<>();
            if (tag1 != null) tmp.add(tag1);
            if (tag2 != null) tmp.add(tag2);
            if (!tmp.isEmpty()) docs.put(id, tmp);
            
        }
        
    }
    
    public HashMap<String, ArrayList<Integer>> get() {
        return this.docs;
    }
    
}
