package scraper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;

import org.jsoup.Jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static HashSet<String> titles = new HashSet<>();
    
    public static void main(String[] args) throws Exception {

        String path = "/home/nahum/Desktop/hebrew_news/raw.txt";
        
//        String baseUrl = "https://news.walla.co.il/archive/2689";
//        String baseUrl = "https://sports.walla.co.il/archive/156";
//        String baseUrl = "https://celebs.walla.co.il/archive/3601";
//        String baseUrl = "https://tech.walla.co.il/archive/4000";
        String baseUrl = "https://sports.walla.co.il/archive/175";
        int year = 2006;
        int month = 1;
        int page = 1;
        
        for (int i = year; i < 2020; i++) {
            for (int j = month; j <= 12; j++) {
                for (int k = page; k < 11; k++) {
                    String location = baseUrl + "?year=" + i + "&month=" + j + "&page=" + k;
System.out.println(location);
                    String text = getContent(location);
                    if (text == null || text.isEmpty()) break;
                    Document doc = Jsoup.parse(text);
                    extractHeadlines(doc);
                    if (titles.size() > 16000) break;
                }
                if (titles.size() > 16000) break;
            }
            if (titles.size() > 16000) break;
        }
        
        
        StringBuilder text = new StringBuilder();
        for (String title : titles) {
            text.append(title).append("\n");
        }
        
        Files.write(Paths.get(path), text.toString().getBytes(), StandardOpenOption.APPEND);
        
    }
    
    private static String getContent(String location) throws Exception {

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
            if (text == null) continue;
            text = text.trim();
            if (text.isEmpty()) continue;
            titles.add(text);
            System.out.println(text);
        }
        
    }
    
}
