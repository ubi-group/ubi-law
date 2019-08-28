package com.itcag.legalyzer.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class Result {

    public class Category {
        
        private final int index;
        private final String label;
        
        private Double score = null;
        
        public Category(int index, String label) {
            this.index = index;
            this.label = label;
        }

        public int getIndex() {
            return index;
        }

        public String getLabel() {
            return label;
        }

        public Double getScore() {
            return score;
        }

        private void setScore(Double score) {
            this.score = score;
        }
        
        @Override
        public String toString() {
            return this.index + "\t" + this.label;
        }
        
    }
    
    private final String categoryFilePath;
    
    private Category topCategory = null;
    
    private final TreeMap<Integer, Category> categories;

    private Result(Result result) {
        this.categoryFilePath = result.categoryFilePath;
        this.categories = result.getCategories();
    }
    
    public Result (String categoryFilePath) throws Exception {
        
        if (categoryFilePath == null) throw new IllegalArgumentException("Invalid category file path: [null]");

        categoryFilePath = categoryFilePath.trim();
        if (categoryFilePath.isEmpty()) throw new IllegalArgumentException("Invalid category file path: [empty string]");

        this.categoryFilePath = categoryFilePath;
        this.categories = new TreeMap<>();
        
        loadCategories();

    }

    private void loadCategories() throws Exception {
        
        
        File file = new File(this.categoryFilePath);
        if (!file.exists()) throw new IllegalArgumentException("File not found: " + this.categoryFilePath);

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(this.categoryFilePath), StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                
                String[] elts = line.split(",");
                if (elts.length != 2) throw new IllegalArgumentException("Invalid category specification: " + line);
                
                elts[0] = elts[0].trim();
                elts[1] = elts[1].trim();
                
                if (elts[1].isEmpty()) throw new IllegalArgumentException("Invalid category specification: " + line);
                
                try {
                    
                    Integer test = Integer.parseInt(elts[0]);
                    
                    Category category = new Category(test, elts[1]);
                    categories.put(category.getIndex(), category);

                } catch (Exception ex) {
                    throw new IllegalArgumentException("The first element in the category specification must be an integer: " + line);
                }
                
            
            }
            reader.close();
        }
    }
    
    public String getCategoryFilePath() {
        return categoryFilePath;
    }

    public Category getTopCategory() {
        return topCategory;
    }

    public TreeMap<Integer, Category> getCategories() {
        return categories;
    }
    
    public void insertScore(int index, double score) throws Exception {
        
        if (!this.categories.containsKey(index)) throw new IllegalArgumentException("Unknown categgory: " + index);
        
        Category category = this.categories.get(index);
        category.setScore(score);
        
        if (this.topCategory == null) {
            this.topCategory = category;
        } else if (topCategory.getScore() < score) {
            this.topCategory = category;
        }
        
    }

    public Result copy() {
        return new Result(this);
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal =  new StringBuilder();
        
        for (Map.Entry<Integer, Category> entry : this.categories.entrySet()) {
            if (retVal.length() > 0) retVal.append(System.lineSeparator());
            retVal.append(entry.getValue().toString()).append("\t").append(Double.toString(entry.getValue().getScore()));
        }
        
        return retVal.toString();
        
    }
    
}
