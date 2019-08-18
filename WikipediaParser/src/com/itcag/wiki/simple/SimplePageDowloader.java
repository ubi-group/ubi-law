package com.itcag.wiki.simple;

import com.itcag.util.Printer;
import com.itcag.wiki.simple.wiki.WikiPage;
import com.itcag.wiki.simple.wiki.WikiTitles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SimplePageDowloader {

    public static void main(String[] args) throws Exception {
        
        String language = "he";
        String path = "/home/nahum/Desktop/hebrew/wikipedia/";
        Files.createDirectories(Paths.get(path));

        Printer.print();
        Printer.print();
        Printer.print("------------------------------------------------------------------------------");
        Printer.print("Starting download...");
        
        WikiTitles downloader = new WikiTitles();
        downloader.download(language);

        int totalPages = downloader.getPageIds().size();
        
        Printer.print("Total: " + totalPages + " articles");
        
        Printer.print("Processing downloaded articles...");
        
        int pageCount = 0;
        int sentenceCount = 0;
        int fileNum = 0;
        StringBuilder text = new StringBuilder();

        WikiPage pager = new WikiPage();
        for (String pageId : downloader.getPageIds()) {
            
            pageCount++;
            
            ArrayList<String> sentences = pager.download(pageId, language);
            
            Printer.print("\t" + pageCount + "/" + totalPages + "\tprocessing " + pageId + " (" + sentences.size() + " sentences)");
            
            for (String sentence : sentences) {
                
                sentenceCount++;
                
                if (sentenceCount%100000 == 0) {
                    String fileName = Integer.toString(fileNum) + ".txt";
                    Files.write(Paths.get(path + fileName), text.toString().getBytes());
                    text = new StringBuilder();
                    fileNum++;
                }
                
                text.append(sentence).append("\n");
            
            }

        }
        
        if (text.length() > 0) {
            String fileName = Integer.toString(fileNum) + ".txt";
            Files.write(Paths.get(path + fileName), text.toString().getBytes());
        }

        Printer.print("Total: " + sentenceCount + " sentences from " + totalPages + " articles");

    }
    
    
}
