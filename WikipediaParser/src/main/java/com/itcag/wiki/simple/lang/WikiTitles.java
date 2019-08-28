package com.itcag.wiki.simple.lang;

import com.itcag.util.Printer;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashSet;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Retrieves all Wikipedia titles in the specified language.
 */
public class WikiTitles {

    private final HashSet<String> pageIds = new HashSet<>();
    
    public void download(String language) throws Exception {
        
        String target = "https://" + language + ".wikipedia.org/w/api.php?action=query&list=allpages&aplimit=500&apnamespace=0&format=json";

        JSONObject jsonObject = getPage(target);
        while (jsonObject.has("continue")) {
        
            extractPageIds(jsonObject.getJSONObject("query"));
            
            JSONObject cont = jsonObject.getJSONObject("continue");
            String appendix = "&" + "apcontinue" + "=" + cont.getString("apcontinue");
            appendix += "&" + "continue" + "=" + cont.getString("continue");
            jsonObject = getPage(target + appendix);
        
        }
        
        extractPageIds(jsonObject.getJSONObject("query"));
            
    }
    
    private JSONObject getPage(String target) throws Exception {

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

    private void extractPageIds(JSONObject jsonObject) throws Exception {
        
        JSONArray jsonArray = jsonObject.getJSONArray("allpages");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject category = jsonArray.getJSONObject(i);
            long pageid = category.getLong("pageid");
            String title = category.getString("title");
            Printer.print(pageid + "\t" + title);
            pageIds.add(Long.toString(pageid));
        }
        
    }

    public final void filter(HashSet<String> filter) {
        
        Iterator<String> pageIterator = this.pageIds.iterator();
        while (pageIterator.hasNext()) {
            String pageId = pageIterator.next();
            if (filter.contains(pageId)) pageIterator.remove();
        }
        
    }
    
    public final HashSet<String> getPageIds() {
        return pageIds;
    }
    
}
