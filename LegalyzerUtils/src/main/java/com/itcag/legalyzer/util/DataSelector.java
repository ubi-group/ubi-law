package com.itcag.legalyzer.util;

import com.itcag.util.MathToolbox;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Selects up to a specified amount of data from a data file.
 * It assumes that each line is a datum.
 * Data are randomly selected until the specified number is reached.
 */
public class DataSelector {
    
    private final int limit;
    private final int size;
    
    private final String sourcePath;
    private final String targetPath;
    
    private final ArrayList<String> all = new ArrayList<>();
    
    public DataSelector(int limit, String sourcePath, String targetPath) throws Exception {
        
        this.limit = limit;
        
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        
        List<String> lines = Files.readAllLines(Paths.get(sourcePath));
        for (String line : lines) {
            all.add(line);
        }
        
        this.size = lines.size();
        
    }

    public void selectAndSave() throws Exception {
        
        Double test = (double) size / limit;
        test = MathToolbox.roundDouble(test, 0);
        int factor = test.intValue();
        
        ArrayList<String> selected = new ArrayList<>();
        
        for (int i = 0; i < all.size(); i++) {
            if (i%factor == 0) {
                selected.add(all.get(i));
                if (selected.size() == limit) break;
            }
        }
        
        saveSelected(selected);
        
    }
    
    private void saveSelected(ArrayList<String> selected) throws Exception {
        
        StringBuilder buffer = new StringBuilder();
        
        for (String newLine : selected) {
            if (buffer.length() > 0) buffer.append("\n");
            buffer.append(newLine);
        }
        
        Files.write(Paths.get(this.targetPath), buffer.toString().getBytes());

    }
    
    public static void main(String[] args) throws Exception {
        
        DataSelector selector = new DataSelector(5000, "/home/nahum/code/ubi-law/hebrew_news/news/celebs/test/many.txt", "/home/nahum/code/ubi-law/hebrew_news/news/celebs/test/0.txt");
        selector.selectAndSave();
        
    }
    
}
