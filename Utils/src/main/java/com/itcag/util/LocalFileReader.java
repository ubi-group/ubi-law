package com.itcag.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public class LocalFileReader {
    
    public static ArrayList<String> readTextFile(String filePath) throws UtilException {
        try {
            ArrayList<String> retVal = new ArrayList<>();
            File file = new File(filePath);
            try (FileInputStream fileInputStream = new FileInputStream(file); InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8"); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    if (!line.isEmpty()) {
                        retVal.add(line);
                    }
                }
            }
            return retVal;
        } catch (Exception ex) {
            throw new UtilException("File " + filePath + " cannot be loaded. " + ex.getClass().getName() + " " + ex.getMessage());
        }
    }  

    public static String readHTMLFile(String filePath) throws UtilException {
        try {
            String retVal = "";
            File file = new File(filePath);
            try (FileInputStream fileInputStream = new FileInputStream(file); InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF8"); BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    line = line.trim();
                    retVal += line;
                }
            }
            return retVal;
        } catch (Exception ex) {
            throw new UtilException("File " + filePath + " cannot be loaded. " + ex.getClass().getName() + " " + ex.getMessage());
        }
    }  

}

