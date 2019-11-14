package com.itcag.dl.util.train;

import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Compares two files with training data,
 * and removes all instances from one of the files,
 * if they also occur in the other file.
 */
public class TrainingDataCleaner {
    
    public static void main(String[] args) throws Exception {
        
        String fileToBeCleaned = "/home/nahum/Desktop/legaltech/experiments/original/Administrative - Licenses, planning and construction.txt";
        String fileWithDuplicates = "/home/nahum/Desktop/legaltech/experiments/generic.txt";
        removeConfusedSentences(fileWithDuplicates, fileToBeCleaned);

    }
    
    private static void removeConfusedSentences(String sourcePath, String targetPath) throws Exception {
        
        HashSet<String> filter = new HashSet<>();
        
        ArrayList<String> lines = TextFileReader.read(sourcePath);
        lines.forEach((line) -> {
            filter.add(line);
        });
        
        HashSet<String> outputLines = new HashSet<>();
        
        lines = TextFileReader.read(targetPath);
        System.out.println("Before removal: " + lines.size());
        System.out.println("Potentially to be removed: " + filter.size());
        for (String line : lines) {
            if (!filter.contains(line)) outputLines.add(line);
        }
        
        TextFileWriter writer = new TextFileWriter(targetPath);
        for (String outputLine : outputLines) {
            writer.write(outputLine);
        }
        writer.close();
        
        System.out.println("After removal: " + outputLines.size());
        System.out.println("Removed: " + (lines.size() - outputLines.size()));
        
    }
    
}
