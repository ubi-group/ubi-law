package com.itcag.wiki.simple;

import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;
import com.itcag.wiki.simple.wiki.WikiPage;
import com.itcag.wiki.simple.wiki.WikiTitles;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;

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

        HashSet<String> filter = new HashSet<>(TextFileReader.read("/home/nahum/Desktop/log"));
        downloader.filter(filter);
        
        int totalPages = downloader.getPageIds().size();
        
        Printer.print("Total: " + totalPages + " articles");
        
        Printer.print("Processing downloaded articles...");
        
        int pageCount = 0;
        int sentenceCount = 0;
        int fileNum = 159;
        StringBuilder text = new StringBuilder();

        WikiPage pager = new WikiPage();
        for (String pageId : downloader.getPageIds()) {
            
            pageCount++;
            
            try {
                
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

            } catch (Exception ex) {
                Printer.print("\t" + pageCount + "/" + totalPages + "\tcanot be processed " + pageId + " (" + ex.getMessage() + ")");
            }

        }
        
        if (text.length() > 0) {
            String fileName = Integer.toString(fileNum) + ".txt";
            Files.write(Paths.get(path + fileName), text.toString().getBytes());
        }

        Printer.print("Total: " + sentenceCount + " sentences from " + totalPages + " articles");

    }
    
    
}
