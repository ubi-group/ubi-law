package com.itcag.util.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;

public final class TextFileReader {

    public final static ArrayList<String> read(String filePath) throws Exception {
        
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException(filePath);

        ArrayList<String> retVal = new ArrayList<>();
        
        try (InputStream input = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                if (!line.isEmpty()) retVal.add(line);
                line = reader.readLine();
            }
        }
        
        return retVal;
    
    }

}
