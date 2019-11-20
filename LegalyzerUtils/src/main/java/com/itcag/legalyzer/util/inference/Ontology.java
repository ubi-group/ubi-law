package com.itcag.legalyzer.util.inference;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 */
public class Ontology {

    public class Relation {

        private final String label;
        private final int index;
        private final double weight;

        public Relation(String label, int index, double weight) {
            this.label = label;
            this.index = index;
            this.weight = weight;
        }

        public String getLabel() {
            return label;
        }

        public int getIndex() {
            return index;
        }

        public double getWeight() {
            return weight;
        }

    }
    
    private final Categories categories;
    
    /**
     * Key = label,
     * Value = list of relations read from a file.
     */
    private final HashMap<String, ArrayList<Relation>> relations = new HashMap<>();
    
    public Ontology(Categories categories) throws Exception {
        
        this.categories = categories;
    
        loadRelations();
        
    }
    
    private void loadRelations() throws Exception {
        
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("inf/ontology")) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    
                    if (line.charAt(0) == 8235) line = line.substring(1);
                    if (line.charAt(line.length() - 1) == 8236) line = line.substring(0, line.length() - 1);

                    /**
                     * Format:
                     *      category (label)
                     *      relation (label)
                     *      weight
                     */
                    String[] elts = line.split("\t");
                    
                    if (elts.length == 3) {
                        
                        String label = Category.normalizeLabel(elts[0].trim());
                        if (isValidCategory(label)) {

                            String related = Category.normalizeLabel(elts[1].trim());
                            int index = getIndex(related);

                            double weight = Double.parseDouble(elts[2].trim());

                            Relation relation = new Relation(related, index, weight);
                            if (this.relations.containsKey(label)) {
                                this.relations.get(label).add(relation);
                            } else {
                                this.relations.put(label, new ArrayList<>(Arrays.asList(relation)));
                            }

                        }

                    }
                     
                }

                line = reader.readLine();

            }
        
        }

    }

    private boolean isValidCategory(String label) {
        return this.categories.get().values().stream().anyMatch((category) -> (category.getLabel().equals(label)));
    }
    
    private int getIndex(String label) {
        for (Category category : this.categories.get().values()) {
            if (category.getLabel().equals(label)) return category.getIndex();
        }
        return -1;
    } 
    
    public ArrayList<Relation> getRelations(int index) {
        Category category = categories.get().get(index);
        return relations.get(category.getLabel());
    }
    
}
