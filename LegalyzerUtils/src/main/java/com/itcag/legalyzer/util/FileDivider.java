package com.itcag.legalyzer.util;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class FileDivider {

    private final static String SOURCE = "/home/nahum/Desktop/hebrew_news_processed/";
    private final static String TARGET_TRAIN = "/home/nahum/Desktop/hebrew_news_processed/train/";
    private final static String TARGET_TEST = "/home/nahum/Desktop/hebrew_news_processed/test/";

    public static void main(String[] args) throws Exception {
        
        for (int i = 0; i < 4; i++) {
            
            String fileName = Integer.toString(i) + ".txt";
            
            TreeMap<Integer, ArrayList<String>> index = new TreeMap<>(Collections.reverseOrder());
            
            List<String> lines = Files.readAllLines(Paths.get(SOURCE + fileName));
            for (String line : lines) {
                String[] tmp = line.split(" ");
                int len = tmp.length;
                if (index.containsKey(len)) {
                    index.get(len).add(line);
                } else {
                    index.put(len, new ArrayList<>(Arrays.asList(line)));
                }
            }
            
            ArrayList<String> train = new ArrayList<>();
            ArrayList<String> test = new ArrayList<>();
            
            int count = 0;
            boolean done = false;
            for (Map.Entry<Integer, ArrayList<String>> entry : index.entrySet()) {

                for (String line : entry.getValue()) {
                    
                    count++;

                    if (count%2 == 0) {
                        test.add(line);
                    } else {
                        train.add(line);
                    }

                    if (count == 10000) {
                        done = true;
                        break;
                    }
                    
                }
                
                if (done) break;
            
            }
            
            StringBuilder buffer = new StringBuilder();
            
            for (String line : train) {
                if (buffer.length() > 0) buffer.append("\n");
                buffer.append(line);
            }
            
            Files.createDirectories(Paths.get(TARGET_TRAIN));
            Files.write(Paths.get(TARGET_TRAIN + fileName), buffer.toString().getBytes());
            
            buffer = new StringBuilder();
            
            for (String line : test) {
                if (buffer.length() > 0) buffer.append("\n");
                buffer.append(line);
            }
            
            Files.createDirectories(Paths.get(TARGET_TEST));
            Files.write(Paths.get(TARGET_TEST + fileName), buffer.toString().getBytes());
            
        }
    
    }
    
    
}
