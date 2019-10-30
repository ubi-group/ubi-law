package com.itcag.datatier;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Transporter uses Elasticsearch RESTful API with JSON over HTTP to send requests and receive Elasticsearch responses.
 * @author IT Consulting Alicja Grużdź
 */
public class Transporter {
   
    public static HttpURLConnection getConnetion(String path, String method) throws Exception {
        if(path == null || path.isEmpty())
            throw new MalformedURLException("Elasticsearch URL address incorrect.");
        
        path = "http://" + "18.184.3.224" + ":" + 56662 + path;

        URL obj = new URL(path);
        HttpURLConnection con = ProxyManager.getConnection(obj);

        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestMethod(method);
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Keep-Alive", "header");
        con.setDoOutput(true);
        return con;
    }
    
    public static String sendJson(String str, String path, String method) throws Exception {

        HttpURLConnection con = getConnetion(path, method);  
                
                if(str != null && !str.isEmpty()) {
                    try {
                        OutputStream os = con.getOutputStream();
                        os.write(str.getBytes("UTF-8"));
                        os.close(); 
                    } catch(IOException ex) {
                        throw new Exception("Could not write HTTP request body in Elasticsearch HTTP request." + ex.getLocalizedMessage());
                    }
                } 
                              
                int responseCode;
                try {
                    responseCode = con.getResponseCode();
                } catch(IOException ex) {
                    throw new Exception("Could not retrieve a response code from HTTP response from Elasticsearch." + ex.getLocalizedMessage());
                }
                
                InputStream is;
                if (responseCode < 400) {
                    try {
                        is = con.getInputStream();
                    } catch(IOException ex) {
                        throw new Exception("Could not retrieve HTTP response from Elasticsearch." + ex.getLocalizedMessage());                   
                    } 
                    
                } else {

                    is = con.getErrorStream();
                }
                

                BufferedReader in;
                StringBuilder response;
                try {
                    in = new BufferedReader(new InputStreamReader(is));
                    String inputLine;
                    response = new StringBuilder();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    
                    in.close();
                    con.disconnect();
                    
                } catch(IOException ex) {
                    throw new Exception("Unable to read HTTP response from Elasticsearch " + ex.getLocalizedMessage());                   
                } 
    
                return response.toString();
                

    }  
    
}