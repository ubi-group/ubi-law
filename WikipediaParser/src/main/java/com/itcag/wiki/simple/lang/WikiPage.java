package com.itcag.wiki.simple.lang;

import com.itcag.util.txt.TextToolbox;
import com.itcag.wiki.simple.Split;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Retrieves a single Wikipedia page.
 */
public class WikiPage {

    public ArrayList<String> download(String pageid, String language) throws Exception {
        
        String location = "https://" + language + ".wikipedia.org/?curid=" + pageid;

        Document doc = getHTML(location);
        return getText(doc);
        
    }
    
    private Document getHTML(String location) throws Exception {

        StringBuilder retVal = new StringBuilder();
        
        URL url = new URL(location);
        HttpURLConnection con = (HttpURLConnection)(url.openConnection());
        con.setInstanceFollowRedirects( false );
        con.connect();
        int responseCode = con.getResponseCode();

        if (responseCode != 200) return null; 
        
        InputStream is = url.openStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            retVal.append(line).append("\n");
        }        

        return Jsoup.parse(retVal.toString());
        
    }
    
    private ArrayList<String> getText(Document doc) {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        retVal.addAll(getSentences("h1", doc));
        retVal.addAll(getSentences("h2", doc));
        retVal.addAll(getSentences("h3", doc));
        retVal.addAll(getSentences("h4", doc));
        retVal.addAll(getSentences("p", doc));
        retVal.addAll(getSentences("ul", doc));
//        retVal.addAll(getSentences("", doc));
        
        return retVal;
        
    }
    
    private ArrayList<String> getSentences(String tag, Document doc) {
        
        ArrayList<String> retVal = new ArrayList<>();
        
        Split split = new Split();

        Elements elts = doc.getElementsByTag(tag);
        for (Element elt : elts) {
            
            String text = elt.wholeText();
            if (TextToolbox.isReallyEmpty(text)) continue;
            
            text = TextToolbox.removeParentheses(text, "[", "]");
            
            text = TextToolbox.replace(text, "\t", " ");
            while (text.contains("  ")) text = TextToolbox.replace(text, "  ", " ");
            
            ArrayList<String> sentences = split.split(text);
            retVal.addAll(sentences);
            
            
        }

        return retVal;
        
    }
    
}
