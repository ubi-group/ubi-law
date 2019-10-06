package com.itcag.dlutil.text;

import com.itcag.dlutil.Config;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.Split;
import com.itcag.util.txt.TextToolbox;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.deeplearning4j.text.tokenization.tokenizer.Tokenizer;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Creates an inverted index of tokens sorted descending by the frequency of occurrence.
 */
public class VocabularyExtractor {

    public static void main(String[] args) throws Exception {
        
        VocabularyExtractor extractor = new VocabularyExtractor();
        extractor.processFile("/home/nahum/Desktop/legaltech/experiments/generic.txt");
        
        for (Map.Entry<Integer, TreeSet<String>> entry : extractor.getInvertedIndex(true).entrySet()) {
            for (String token : entry.getValue()) {
                System.out.println(entry.getKey() + "\t" + token);
            }
        }
        
    }
    
    private final Split split = new Split();
    private final TokenizerFactory tokenizerFactory;

    private final HashMap<String, Integer> index = new HashMap<>();
    
    public VocabularyExtractor() {
        this.tokenizerFactory = new DefaultTokenizerFactory();
        this.tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
    }
    
    public void processFolder() throws Exception {
        
        File folder = new File(Config.TEXT_SOURCE_FOLDER);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) continue;
            System.out.println(file.getName());
            processFile(file.getPath());
        }
        
    }
    
    public void processFile(String filePath) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        for (String line : lines) {
            processLine(line);
        }
        
    }
    
    private void processLine(String line) {
        
        ArrayList<String> sentences = split.split(line);
        for (String sentence : sentences) {
            processSentence(sentence);
        }
        
    }
    
    private void processSentence(String sentence) {
        
        Tokenizer tokenizer = this.tokenizerFactory.create(sentence);
        while (tokenizer.hasMoreTokens()) {
            
            String token = tokenizer.nextToken();
            if (TextToolbox.isReallyEmpty(token)) continue;
            
            if (index.containsKey(token)) {
                index.put(token, index.get(token) + 1);
            } else {
                index.put(token, 1);
            }
        
        }
    
    }
   
    public TreeMap<Integer, TreeSet<String>> getInvertedIndex(boolean useThreshold) {
        
        TreeMap<Integer, TreeSet<String>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            
            if (useThreshold && entry.getValue() <= Config.TEXT_FOO_THRESHOLD) continue;
            
            if (retVal.containsKey(entry.getValue())) {
                retVal.get(entry.getValue()).add(entry.getKey());
            } else {
                retVal.put(entry.getValue(), new TreeSet<>(Arrays.asList(entry.getKey())));
            }
        
        }
        
        return retVal;
        
    }
    
}
