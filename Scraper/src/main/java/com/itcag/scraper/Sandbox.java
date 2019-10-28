package com.itcag.scraper;

import com.itcag.split.Splitter;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import com.itcag.util.txt.TextToolbox;

import java.io.File;

import java.util.ArrayList;
import java.util.HashSet;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        
//        processFolder("/home/nahum/Desktop/legaltech/experiments/original");
        processFile("/home/nahum/Desktop/legaltech/experiments/original/Criminal - Prisioner's petitions.txt");
        
//        removeConfusedSentences("/home/nahum/Desktop/legaltech/experiments/confused_sentences", "/home/nahum/Desktop/legaltech/experiments/original/Administrative - Commercial and Economic Regulation.txt");

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
        System.out.println("To be removed: " + filter.size());
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
    
    private static void processFolder(String folderPath) throws Exception {
        
        int total = 0;
        int count = 0;
        
        File folder = new File(folderPath);
        total = folder.listFiles().length;
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            System.out.println(count++ + "/" + total);
            processFile(file.getPath());
        }
        
    }
    
    private static void processFile(String filePath) throws Exception {
    
        String filter = "עתירת";
        
        ArrayList<String> lines = TextFileReader.read(filePath);

        HashSet<String> outputLines = new HashSet<>();
        
        for (String line : lines) {
            
            if (line.contains(filter)) {
                System.out.println(line);
            } else {
                outputLines.add(line);
            }
            
        }
        
        TextFileWriter writer = new TextFileWriter(filePath);
        for (String outputLine : outputLines) {
            writer.write(outputLine);
        }
        writer.close();
    
    }
    
}
