package com.itcag.scraper;

import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import com.itcag.util.txt.TextToolbox;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Sandbox {

    public static void main(String[] args) throws Exception {
        
        
        processFolder("/home/nahum/Desktop/hebrew/high court rulings/");
//        processFile("/home/nahum/Desktop/legaltech/experiments/tmp");
        
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
    
        String filter = "מעמד";
        
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
