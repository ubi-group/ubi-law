package com.itcag.dl.util.word2vec;

import com.itcag.dl.Config;
import com.itcag.util.Converter;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import com.itcag.util.txt.Split;

import java.io.File;

import java.util.ArrayList;

/**
 * Converts text files into single sentence files of the size 10,000 lines.
 * It iterates over a directory containing text files. Reads line by line
 * and if instructed splits them into sentences. Each sentences is then
 * recorded in a text file in a separate line. The resulting file name
 * is expected to be a number continuing the number of the last file
 * in the destination folder.
 */
public class Word2VecDataFactory {
    
    public static void main(String[] args) throws Exception {
        
        Word2VecDataFactory factory = new Word2VecDataFactory();
        factory.processFolder("/home/nahum/Desktop/hebrew/high court rulings/");
        
    }
    
    private final static int FILE_SIZE = 100000;

    private int currentFileIndex;
    private int currentRecordedLine = -1;

    private TextFileWriter writer;

    
    public Word2VecDataFactory() throws Exception {
    
        File folder = new File(Config.WORD_2_VEC_DATA_PATH);
        for (File file : folder.listFiles()) {
            String fileName = file.getName().replace(".txt", "");
            Integer test = Converter.convertStringToInteger(fileName);
            if (test == null) throw new NullPointerException("Bad file name: " + file.getPath());
            if (test > this.currentFileIndex) this.currentFileIndex = test;
        }
    
        this.currentFileIndex++;
        writer = new TextFileWriter(Config.WORD_2_VEC_DATA_PATH + Integer.toString(this.currentFileIndex) + ".txt");

    }
    
    public final void processFolder(String folderName) throws Exception {
        
        File folder = new File(folderName);
        for (File file : folder.listFiles()) {
            processFile(file);
        }
        
        this.writer.close();
        
    }

    private void processFile(File file) throws Exception {
        
        Split split = new Split();

        ArrayList<String> lines = TextFileReader.read(file.getPath());
        for (String line : lines) {
            ArrayList<String> sentences = split.split(line);
            for (String sentence : sentences) {
                recordLine(sentence);
            }
        }
        
    }
    
    private void recordLine(String line) throws Exception {
        
        this.currentRecordedLine++;
        
        if (this.currentRecordedLine >= FILE_SIZE) {
            this.currentRecordedLine = 0;
            this.currentFileIndex++;
            this.writer = new TextFileWriter(Config.WORD_2_VEC_DATA_PATH + Integer.toString(this.currentFileIndex) + ".txt");
        }
        
        this.writer.write(line);
        
    }
    
}
