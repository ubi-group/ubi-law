package com.itcag.wiki.pedia;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Retrieves all Wikipedia categories.
 */
public class WikiCategories {

    public static void download() throws Exception {
        
        String target = "https://en.wikipedia.org/w/api.php?action=query&list=allpages&aplimit=500&apnamespace=14&format=json";

        JSONObject jsonObject = getPage(target);
        while (jsonObject.has("continue")) {
        
            extractCategories(jsonObject.getJSONObject("query"));
            
            JSONObject cont = jsonObject.getJSONObject("continue");
            String appendix = "&" + "apcontinue" + "=" + cont.getString("apcontinue");
            appendix += "&" + "continue" + "=" + cont.getString("continue");
            jsonObject = getPage(target + appendix);
        
        }
        
        extractCategories(jsonObject.getJSONObject("query"));
            
    }
    
    private static JSONObject getPage(String target) throws Exception {

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

    private static void extractCategories(JSONObject jsonObject) throws Exception {
        
        JSONArray jsonArray = jsonObject.getJSONArray("allpages");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject category = jsonArray.getJSONObject(i);
            String pageid = category.getString("pageid");
            String title = category.getString("title");
            title = cleanTitle(title);
            System.out.println(pageid + "\t" + title);
        }
        
    }

    private static String cleanTitle(String title) {
        title = title.replace("Category:", "");
        return title;
    }
    
}
