package com.itcag.legalyzer.util;

import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashSet;

public class NewsCleaner {
    
    private final static HashSet<String> NEW_LINES = new HashSet<>();

    private static final String SOURCE_FOLDER = "/home/nahum/Desktop/hebrew/news/";
    private static final String TARGET_FOLDER = "/home/nahum/Desktop/hebrew/news/clean/";
    
    private static int fileNum = 0;

    private static int count = 0;
    
    public static void main(String[] args) throws Exception {
        
        Files.createDirectories(Paths.get(TARGET_FOLDER));
        
        readFolder();

        System.out.println("COUNT: " + count);
        
    }

    private static void readFolder() throws Exception {
        
        File folder = new File(SOURCE_FOLDER);
        for (File file : folder.listFiles()) {
            if (!file.isFile()) continue;
            System.out.println("Reading: " + file.getName());
            process(readFile(file.getAbsolutePath()));
        }
        
        if (!NEW_LINES.isEmpty()) recordNewLines();

    }
    
    private static ArrayList<String> readFile(String path) throws Exception {
        return TextFileReader.read(path);
    }
    
    private static void process(ArrayList<String> lines) throws Exception {
        
        for (String line : lines) {
            
            if (TextToolbox.isReallyEmpty(line)) continue;
            
            while (line.contains("\n")) line = TextToolbox.replace("line", "\n", " ");
            while (line.contains("\r")) line = TextToolbox.replace("line", "\r", " ");
            while (line.contains("  ")) line = TextToolbox.replace("line", "  ", " ");
            
            line = line.trim();
            if (line.isEmpty()) continue;

            if (containsIllegitimateCharacters(line)) continue;
            if (line.split(" ").length < 4) continue;
            
            NEW_LINES.add(line);
            count++;
            
            if (NEW_LINES.size() >= 100000) recordNewLines();
            
        }
        
    }

    private static boolean containsIllegitimateCharacters(String line) throws Exception {

        for (char c : line.toCharArray()) {
            if (!isLegitimateCharacter(c)) return true;
        }

        return false;
        
    }
    
    private static boolean isLegitimateCharacter(char c) {
        
        if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HEBREW) return true;
        
        switch (Character.getType(c)) {
            case Character.DASH_PUNCTUATION:
            case Character.DIRECTIONALITY_BOUNDARY_NEUTRAL:
            case Character.DIRECTIONALITY_WHITESPACE:
            case Character.END_PUNCTUATION:
            case Character.LOWERCASE_LETTER:
            case Character.MATH_SYMBOL:
            case Character.OTHER_PUNCTUATION:
            case Character.START_PUNCTUATION:
            case Character.UPPERCASE_LETTER:
                return true;
        }
        
        return false;
    }
    
    private static void recordNewLines() throws Exception {
        
        StringBuilder buffer = new StringBuilder();
        
        for (String newLine : NEW_LINES) {
            if (buffer.length() > 0) buffer.append("\n");
            buffer.append(newLine);
        }
        
        String fileName = Integer.toString(fileNum) + ".txt";
        Files.write(Paths.get(TARGET_FOLDER + fileName), buffer.toString().getBytes());

        fileNum++;
        NEW_LINES.clear();

    }
    
}
