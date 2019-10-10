package com.itcag.legalyzer;

import com.itcag.dlutil.Categories;
import com.itcag.dlutil.lang.Category;
import com.itcag.legalyzer.util.Anchor;
import com.itcag.util.Converter;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class Anchors {

    /**
     * Key = category index,
     * Value = anchor.
     */
    private final HashMap<Integer, Anchor> anchors =  new HashMap<>();
        
    public Anchors(Categories categories, String filePath) throws Exception {

        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            
            String[] elts = line.split("\t");
        
            Integer anchorIndex = Converter.convertStringToInteger(elts[0].trim());
            if (anchorIndex == null) throw new NullPointerException("Invalid anchor index (not an integer): " + line);
            
            Category anchorCategory = categories.get().get(anchorIndex);
            
            Anchor anchor;
            if (this.anchors.containsKey(anchorCategory.getIndex())) {
                anchor = this.anchors.get(anchorCategory.getIndex());
            } else {
                anchor = new Anchor(anchorCategory);
                this.anchors.put(anchorCategory.getIndex(), anchor);
            }
            
            Integer tagIndex = Converter.convertStringToInteger(elts[2].trim());
            if (tagIndex == null) throw new NullPointerException("Invalid tag index (not an integer): " + line);
            
            Category tagCategory = categories.get().get(tagIndex);
            
            Double weight = Converter.convertStringToDouble(elts[1].trim());
            if (weight == null) throw new NullPointerException("Invalid weight (not a double): " + line);
            
            anchor.addTag(tagCategory, weight);
        
        }
        
    }
    
    public HashMap<Integer, Anchor> getAnchors() {
        return this.anchors;
    }

    public boolean containsIndex(int index) {
        return this.anchors.containsKey(index);
    }

    public TreeMap<Double, Category> getAnchorTags(int index) {
        return this.anchors.get(index).getTags();
    }
    
}
