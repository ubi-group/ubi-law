package com.sa.util.rss;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.HashSet;
import java.util.List;

public class RSSReader {

    private static final String DIRECTORY = "/home/nahum/Desktop/hebrew_news/";
    
    private static final String CATEGORY_0_FILE = "0.txt";
    private static final String CATEGORY_1_FILE = "1.txt";
    private static final String CATEGORY_2_FILE = "2.txt";
    private static final String CATEGORY_3_FILE = "3.txt";
    
    private static final String CATEGORY_0_URL = "http://rss.walla.co.il/feed/2689"; //צבא וביטחון
    private static final String CATEGORY_1_URL = "http://rss.walla.co.il/feed/156"; //כדורגל ישראלי
    private static final String CATEGORY_2_URL = "http://rss.walla.co.il/feed/111"; //עסקים בארץ
    private static final String CATEGORY_3_URL = "http://rss.walla.co.il/feed/272"; //מוזיקה
    
    public static void main(String[] args) throws Exception {
        
        update(DIRECTORY + CATEGORY_0_FILE, CATEGORY_0_URL);
        update(DIRECTORY + CATEGORY_1_FILE, CATEGORY_1_URL);
        update(DIRECTORY + CATEGORY_2_FILE, CATEGORY_2_URL);
        update(DIRECTORY + CATEGORY_2_FILE, "http://rss.walla.co.il/feed/112");
        update(DIRECTORY + CATEGORY_2_FILE, "http://rss.walla.co.il/feed/557");
        update(DIRECTORY + CATEGORY_3_FILE, CATEGORY_3_URL);
        update(DIRECTORY + CATEGORY_3_FILE, "http://rss.walla.co.il/feed/270");
        update(DIRECTORY + CATEGORY_3_FILE, "http://rss.walla.co.il/feed/3601");
        update(DIRECTORY + CATEGORY_3_FILE, "http://rss.walla.co.il/feed/271");

    }
    
    private static void update(String path, String url) throws Exception {
        
        HashSet<String> oldData = getData(path);
        HashSet<String> newData = new HashSet<>();
        
        RSSFeedParser parser = new RSSFeedParser(url);
        
        Feed feed = parser.readFeed();

        for (FeedMessage message : feed.getMessages()) {
            String title = message.getTitle();
            title = title.trim();
            if (title.isEmpty()) continue;
            if (!oldData.contains(title)) newData.add(title);
        }
        
        appendData(path, newData);
        
    }
    
    private static HashSet<String> getData(String pathToFile) throws Exception {
        
        Path path = Paths.get(pathToFile);
        if (!path.toFile().exists()) path.toFile().createNewFile();
            
        List<String> allLines = Files.readAllLines(Paths.get(pathToFile));
        return new HashSet<>(allLines);
    
    }
    
    private static void appendData(String path, HashSet<String> newData) throws Exception {
        
        StringBuilder text = new StringBuilder();
        for (String datum : newData) {
            text.append(datum).append("\n");
        }
        
        Files.write(Paths.get(path), text.toString().getBytes(), StandardOpenOption.APPEND);
    
    }
    
}
