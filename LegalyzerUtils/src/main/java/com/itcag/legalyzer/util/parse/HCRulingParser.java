package com.itcag.legalyzer.util.parse;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.Properties;

public class HCRulingParser extends Parser {
    
    public HCRulingParser(Properties config) throws Exception {
        
        super(config);
    
        setTriggers();
        setSkippers();
        setStoppers();
        
    }
    
    private void setTriggers() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("parse/hc_ruling_triggers");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null){
            line = line.trim();
            if (!line.isEmpty()) super.triggers.add(line);
            line = reader.readLine();
        }
    }
    
    private void setSkippers() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("parse/hc_ruling_skippers");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null){
            line = line.trim();
            if (!line.isEmpty()) super.skippers.add(line);
            line = reader.readLine();
        }
    }
    
    private void setStoppers() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("parse/hc_ruling_stoppers");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line = reader.readLine();
        while (line != null){
            line = line.trim();
            if (!line.isEmpty()) super.stoppers.add(line);
            line = reader.readLine();
        }
    }
    
}
