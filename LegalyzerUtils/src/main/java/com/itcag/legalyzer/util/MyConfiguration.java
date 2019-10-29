package com.itcag.legalyzer.util;

import java.io.InputStream;

import java.util.Properties;

public class MyConfiguration {

    public final static String FILE_NAME;
    static {
        try (InputStream inputStream = MyConfiguration.class.getClassLoader().getResourceAsStream("my.properties")) {
	    Properties prop = new Properties();
            prop.load(inputStream);
            FILE_NAME = prop.getProperty("fileName");

        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
    
}
