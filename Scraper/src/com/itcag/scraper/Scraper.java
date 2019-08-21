package com.itcag.scraper;

import com.itcag.util.txt.TextToolbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;
import java.util.HashSet;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static final Split split = new Split();
    
    private static final HashSet<String> SENTENCES = new HashSet<>();
    
    public static void main(String[] args) throws Exception {

        String path = "/home/nahum/Desktop/hebrew/news/";

        ArrayList<String> links = extractLinks("https://www.walla.co.il/archive");
        
        for (String link : links) {
            scrapArchive(link);
        }
        
        StringBuilder text = new StringBuilder();
        
        int count = 0;
        int fileNum = 0;
        
        for (String sentence : SENTENCES) {
            if (text.length() > 0) text.append("\n");
            text.append(sentence);
            count++;
            if (count%100000 == 0) {
                Files.write(Paths.get(path + Integer.toString(fileNum) + ".txt"), text.toString().getBytes(), StandardOpenOption.APPEND);
                fileNum++;
                text = new StringBuilder();
            }
        }
        
        if (text.length() > 0) Files.write(Paths.get(path + Integer.toString(fileNum) + ".txt"), text.toString().getBytes(), StandardOpenOption.APPEND);
        
    }
    
    private static ArrayList<String> extractLinks(String location) throws Exception {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        String page = getPage(location);
        if (page == null || page.isEmpty()) return retVal;
        Document doc = Jsoup.parse(page);
        
        Elements elts = doc.select("#container > section > section > section > section > ul > li > a");
        for (Element link : elts) {
            String href = link.attr("href");
            if (TextToolbox.isReallyEmpty(href)) continue;
            if (href.contains("?")) {
                int pos = href.indexOf("?");
                href = href.substring(0, pos);
            }
            href = href.trim();
            retVal.add(href);
            System.out.println(href);
        }
        
        return retVal;
        
    }
    
    private static void scrapArchive(String baseUrl) throws Exception {
        
        int year = 2006;
        int month = 1;
        int page = 1;
        
        for (int i = year; i < 2020; i++) {
            for (int j = month; j <= 12; j++) {
                for (int k = page; k < 11; k++) {
                    String location = baseUrl + "?year=" + i + "&month=" + j + "&page=" + k;
System.out.println(location);
                    String text = getPage(location);
                    if (text == null || text.isEmpty()) break;
                    Document doc = Jsoup.parse(text);
                    extractHeadlines(doc);
                    extractSummaries(doc);
                }
            }
        }
        
    }
    
    private static String getPage(String location) throws Exception {

        StringBuilder retVal = new StringBuilder();
        
        URL url = new URL(location);
        HttpURLConnection con = (HttpURLConnection)(url.openConnection());
        con.setInstanceFollowRedirects( false );
        con.connect();
        int responseCode = con.getResponseCode();

        if (responseCode != 200) return null; 
        
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        try {
            String line;
            while ((line = br.readLine()) != null) {
                retVal.append(line).append("\n");
            }        
        } catch(Exception ex) {
            //do nothing
        }

        return retVal.toString();
        
    }
    
    private static void extractHeadlines(Document doc) {
        
        Elements elts = doc.select("#container > section.fc.common-section.grid-1-1 > section > div > section > ul > li > article > a > h3 > div > span");
        for (Element title : elts) {
            String text = title.text();
            if (TextToolbox.isReallyEmpty(text)) continue;
            text = text.trim();
            ArrayList<String> sentences = split.split(text);
            SENTENCES.addAll(sentences);
        }
        
    }
    
    private static void extractSummaries(Document doc) {
        
        Elements elts = doc.select("#container > section.fc.common-section.grid-1-1 > section > div > section > ul > li > article > a > p");
        for (Element title : elts) {
            String text = title.text();
            if (TextToolbox.isReallyEmpty(text)) continue;
            text = text.trim();
            ArrayList<String> sentences = split.split(text);
            SENTENCES.addAll(sentences);
        }
        
    }
    
}
