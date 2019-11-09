package com.itcag.datatier;

import java.io.IOException;

import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;

/**
 * ProxyManager provides connection to Elasticsearch via proxy server
 * @author IT Consulting Alicja Grużdź
 */
public class ProxyManager {
    
    protected static HttpURLConnection getConnection(URL serverAddress) throws Exception {
        HttpURLConnection httpUrlConn;
        
        try {

                Authenticator.setDefault(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication("legal", "Th88mffyQg".toCharArray());
                    }
                });               
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("18.184.3.224", 56662));
                httpUrlConn = (HttpURLConnection)serverAddress.openConnection(proxy);
   
        } catch(IOException ex) {
            throw new Exception("IOException while attempting connection via proxy. " + ex.getLocalizedMessage());
        }

        return httpUrlConn;
    }
     
}