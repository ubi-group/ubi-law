package com.itcag.wiki.pedia;

import com.itcag.wiki.lang.Page;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import org.json.JSONObject;

/**
 * Retrieves a single Wikipedia page.
 */
public class WikiPage {

    public Page download(String pageid) throws Exception {
        
        String target = "https://en.wikipedia.org/w/api.php?action=query&format=json&pageids=" + pageid;

        JSONObject jsonObject = getPage(target);
        return extractPage(jsonObject.getJSONObject("query"));

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

    private Page extractPage(JSONObject jsonObject) throws Exception {
        
        JSONObject pages = jsonObject.getJSONObject("pages");
        Iterator pageIterator = pages.keys();
        String first = (String) pageIterator.next();
        JSONObject embedded = pages.getJSONObject(first);
        
        if (embedded.has("missing")) return null;
        if (!embedded.has("pageid")) return null;
        if (!embedded.has("title")) return null;
        
        String pageid = embedded.getString("pageid");
        String title = embedded.getString("title");
        return createPage(pageid, title);
        
    }

    private Page createPage(String id, String title) {
        
        if (title.startsWith("User:")) return null;
        
        if (title.startsWith("Category:")) {
            title = title.replace("Category:", "");
            return new Page(id, title, Page.Type.CATEGORY);
        } else if (title.startsWith("Portal:")) {
            return null;
        } else {
            return new Page(id, title, Page.Type.PAGE);
        }

    }
    
}
