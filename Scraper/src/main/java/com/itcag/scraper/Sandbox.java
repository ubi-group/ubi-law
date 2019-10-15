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
        
        processFolder("/home/nahum/Desktop/legaltech/experiments/original");
        
    }

    private static void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            processFile(file.getPath());
        }
        
    }
    
    private static void processFile(String filePath) throws Exception {
    
        ArrayList<String> lines = TextFileReader.read(filePath);

        Splitter splitter = new Splitter();

        HashSet<String> outputLines = new HashSet<>();
        
        for (String line : lines) {
            
            ArrayList<StringBuilder> sentences = splitter.split(new StringBuilder(line));
            for (StringBuilder sentence : sentences) {
                
                if (!isValid(sentence.toString())) {
                    System.out.println(sentence.toString());
                    continue;
                }
                
                String cleanedSentence = clean(sentence.toString());
                outputLines.add(cleanedSentence);
                
            }
            
        }
        
        TextFileWriter writer = new TextFileWriter(filePath);
        for (String outputLine : outputLines) {
            writer.write(outputLine);
        }
        writer.close();
    
    }
    
    private static boolean isValid(String sentence) throws Exception {
        
        if (sentence.contains("ערעור")) return false;
        if (sentence.contains("עתירה")) return false;
        if (sentence.contains("נמחק")) return false;
        if (sentence.contains("מוצתה")) return false;
        if (sentence.contains("הוצאות")) return false;
        if (sentence.contains("המלצת")) return false;
        if (sentence.contains("פשרה")) return false;
        if (sentence.contains("גישור")) return false;
        if (sentence.contains("מיצתה עצמה")) return false;
        if (sentence.contains("להידחות")) return false;
        if (sentence.contains("נדחית")) return false;
        if (sentence.contains("(")) return false;
        if (sentence.contains(")")) return false;
        
        if (sentence.length() < 100) return false;

        return true;
        
    }
    
    private static String clean(String sentence) throws Exception {
        
        while (sentence.contains("  ")) sentence = TextToolbox.replace(sentence, "  ", " ");
        sentence = sentence.replace(" .", ".");
        sentence = sentence.replace(" ,", ",");
        
        return sentence;
        
    }
    
}
