package com.itcag.scraper.court_rulings;

import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {
    
    public static void main(String[] args) throws Exception {
        
        Scraper scraper = new Scraper("/home/nahum/Desktop/legaltech/verdicts", "/home/nahum/Desktop/legaltech/high court rulings/");
        scraper.scrape();
        
    }
    
    private final String sourcePath;
    private final String destination;
    
    public Scraper(String sourcePath, String destination) {
        this.sourcePath = sourcePath;
        this.destination = destination;
    }
    
    public void scrape() throws Exception {
        
        int count = 0;
        
        ArrayList<String> lines = TextFileReader.read(this.sourcePath);
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            String id = elts[0].trim();
            String url = elts[1].trim();
            
            String text = getPage(url);
            if (text == null || text.isEmpty()) break;
            
            Document doc = Jsoup.parse(text);
            text = extractText(doc).toString();
            
            Files.write(Paths.get(this.destination + id + ".txt"), text.getBytes());
            
            System.out.println(count++ + "\t" + id);
            
        }
        
    }
    
    public static String getPage(String location) throws Exception {

        StringBuilder retVal = new StringBuilder();
        
        URL url = new URL(location);
        HttpURLConnection con = (HttpURLConnection)(url.openConnection());
        
        con.setInstanceFollowRedirects( false );
        con.connect();
        int responseCode = con.getResponseCode();

        if (responseCode != 200) return null; 

        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is, "windows-1255"));

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
    
    public static StringBuilder extractText(Document doc) throws Exception {

        StringBuilder retVal = new StringBuilder();
        
        Elements elts = doc.getAllElements();
        for (Element elt : elts) {
            switch (elt.tagName().toLowerCase()) {
                case "h1":
                case "h2":
                case "h3":
                case "h4":
                {
                    String text = elt.text();
                    if (TextToolbox.isReallyEmpty(text)) continue;
                    text = text.trim();
                    retVal.append(text).append("\n");
                    break;
                }
                case "p":
                {
                    /* IGNORE TABLE OF CONTENT */
                    if (elt.className().toLowerCase().startsWith("msotoc")) continue;
                    String text = elt.text();
                    if (TextToolbox.isReallyEmpty(text)) continue;
                    text = text.trim();
                    retVal.append(text).append("\n");
                    break;
                }
            }
        }

        return retVal;

    }
    
}
