package com.itcag.util.io;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public final class TextFileWriter {

    private final PrintWriter out;
    
    public TextFileWriter(String filePath) throws Exception {

        File file = new File(filePath);
        if (file.exists()) file.delete();
        file.createNewFile();

        FileWriter fileWriter = new FileWriter(file, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        out = new PrintWriter(bufferedWriter);

    }
    
    public final void write(String str) throws IOException {
        
        out.println(str);
        out.flush();

    }
    
    public final void close() {
        
        out.flush();
        out.close();

    }

}
