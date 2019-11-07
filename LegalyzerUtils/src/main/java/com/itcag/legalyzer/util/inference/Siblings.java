package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Siblings {

    /**
     * Key = category index,
     * Value = indices of the sibling categories (sharing the same parent).
     */
    private final HashMap<Integer, ArrayList<Integer>> siblings = new HashMap<>();

    public Siblings(Categories categories) {
        
        HashMap<String, ArrayList<Integer>> tmp = new HashMap<>();
        
        for (Map.Entry<Integer, Category> entry : categories.get().entrySet()) {
            
            String label = entry.getValue().getLabel();
            String[] elts = label.split("-");
            String parent = elts[0].trim();
            
            if (tmp.containsKey(parent)) {
                tmp.get(parent).add(entry.getKey());
            } else {
                tmp.put(parent, new ArrayList<>(Arrays.asList(entry.getKey())));
            }
            
        }
        
        for (Map.Entry<String, ArrayList<Integer>> entry : tmp.entrySet()) {
            for (int index : entry.getValue()) {
                ArrayList<Integer> siblings = new ArrayList<>();
                for (int sibling : entry.getValue()) {
                    if (sibling != index) siblings.add(sibling);
                }
                this.siblings.put(index, siblings);
            }
        }

    }

    public boolean isThematicSibling(int index1, int index2) {
        return this.siblings.get(index1).contains(index2);
    }
    
    public ArrayList<Integer> getThematicSiblings(int index) {
        return this.siblings.get(index);
    }
    
}
