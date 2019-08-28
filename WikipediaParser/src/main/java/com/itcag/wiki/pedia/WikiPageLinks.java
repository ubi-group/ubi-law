package com.itcag.wiki.pedia;

import com.itcag.wiki.lang.Page;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONObject;

/**
 * Retrieves all links contained in a Wikipedia page.
 */
public class WikiPageLinks {

    private final HashMap<String, Page> links = new HashMap<>();
    
    public void download(String page) throws Exception {
        
        String target = "https://en.wikipedia.org/w/api.php?action=query&pageids=" + page + "&generator=links&gplnamespace=0&format=json&gpllimit=max";

        JSONObject jsonObject = getLink(target);
        while (jsonObject.has("continue")) {
        
            extractLinks(jsonObject.getJSONObject("query"));
            
            JSONObject cont = jsonObject.getJSONObject("continue");
            String appendix = "&" + "gplcontinue" + "=" + cont.getString("gplcontinue");
            appendix += "&" + "continue" + "=" + cont.getString("continue");
            jsonObject = getLink(target + appendix);
        
        }
        
        extractLinks(jsonObject.getJSONObject("query"));

    }
    
    private JSONObject getLink(String target) throws Exception {

        URL url = new URL(target);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");

        if (conn.getResponseCode() != 200) {
            throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
        }

        BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

        StringBuilder retVal = new StringBuilder();
        String output;
        while ((output = br.readLine()) != null) {
            retVal.append(output);
//            System.out.println(output);
        }

        conn.disconnect();

        return new JSONObject(retVal.toString());

    }

    private void extractLinks(JSONObject jsonObject) throws Exception {
        
        JSONObject pages = jsonObject.getJSONObject("pages");
        Iterator pageIterator = pages.keys();
        while (pageIterator.hasNext()) {
            String key = (String) pageIterator.next();
            JSONObject linkedPage = pages.getJSONObject(key);
            if (linkedPage.has("missing")) continue;
            if (!linkedPage.has("pageid")) continue;
            if (!linkedPage.has("title")) continue;
            String pageId = linkedPage.getString("pageid");
            String title = linkedPage.getString("title");
            links.put(pageId, new Page(pageId, title, Page.Type.PAGE));
        }
        
    }

    public HashMap<String, Page> getLinks() {
        return links;
    }

}
