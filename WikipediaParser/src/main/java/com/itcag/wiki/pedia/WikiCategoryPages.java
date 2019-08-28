package com.itcag.wiki.pedia;

import com.itcag.wiki.lang.Page;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Extracts both the sub-categories and the pages belonging to a category. 
 */
public class WikiCategoryPages {

    private final HashMap<String, Page> pages = new HashMap<>();
    
    public void download(String category, boolean subcategories) throws Exception {
        
        String target;
        if (subcategories) {
            target = "https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&cmpageid=" + category + "&cmlimit=500&format=json&cmtype=subcat";
        } else {
            target = "https://en.wikipedia.org/w/api.php?action=query&list=categorymembers&cmpageid=" + category + "&cmlimit=500&format=json&cmtype=page";
        }

        JSONObject jsonObject = getPage(target);
        while (jsonObject.has("continue")) {
        
            extractPages(jsonObject.getJSONObject("query"));
            
            JSONObject cont = jsonObject.getJSONObject("continue");
            String appendix = "&" + "apcontinue" + "=" + cont.getString("apcontinue");
            appendix += "&" + "continue" + "=" + cont.getString("continue");
            jsonObject = getPage(target + appendix);
        
        }
        
        extractPages(jsonObject.getJSONObject("query"));

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

    private void extractPages(JSONObject jsonObject) throws Exception {
        
        JSONArray jsonArray = jsonObject.getJSONArray("categorymembers");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject category = jsonArray.getJSONObject(i);
            String pageid = category.getString("pageid");
            String title = category.getString("title");
            Page page = createPage(pageid, title);
            if (page == null) continue;
            getPages().put(page.getId(), page);
        }
        
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
    
    public HashMap<String, Page> getPages() {
        return pages;
    }

}
