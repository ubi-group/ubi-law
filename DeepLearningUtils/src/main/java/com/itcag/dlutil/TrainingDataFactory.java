package com.itcag.dlutil;

import com.itcag.util.MathToolbox;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;

import java.io.File;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Splits the original data set into training and test data.
 * The original data set is expected to be contained in a single file,
 * where each data point is a single line. This could be a single
 * sentence or a paragraph. Paragraphs are not split into sentences.
 * The name of the resulting file must be set, so that it corresponds
 * to the index of the related category in the categories.txt file.
 */
public class TrainingDataFactory {
    
    public static void main(String[] args) throws Exception {
    
        TrainingDataFactory factory = new TrainingDataFactory();
        factory.processFile();
        
    }

    private final ArrayList<String> trainSource = new ArrayList<>();
    private final ArrayList<String> testSource = new ArrayList<>();

    private final ArrayList<String> trainTarget = new ArrayList<>();
    private final ArrayList<String> testTarget = new ArrayList<>();

    private final HashSet<String> uniqueDataPoints = new HashSet<>();
    
    public TrainingDataFactory() throws Exception {
        extractUniqueDataPoints(Config.TEST_DATA_PATH);
        extractUniqueDataPoints(Config.TRAINING_DATA_PATH);
    }
    
    private void extractUniqueDataPoints(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        if (!folder.exists()) return;
        
        for (File file : folder.listFiles()) {
            ArrayList<String> lines = TextFileReader.read(file.getPath());
            uniqueDataPoints.addAll(lines);
        }
        
    }
    
    public void processFile() throws Exception {
        
        ArrayList<String> docs = TextFileReader.read(Config.TRAINING_SOURCE_FILE_NAME);
        splitDocs(docs);
        
        while (trainTarget.size() < Config.TRAINING_DATA_SIZE) {
            createData(new ArrayList<>(trainSource), trainTarget, Config.TRAINING_DATA_SIZE);
        }
        
        while (testTarget.size() < Config.TEST_DATA_SIZE) {
            createData(new ArrayList<>(testSource), testTarget, Config.TEST_DATA_SIZE);
        }
        
        record(trainTarget, Config.TRAINING_DATA_PATH);
        record(testTarget, Config.TEST_DATA_PATH);
        
    }
    
    private void splitDocs(ArrayList<String> docs) {

        /**
         * Splits the file content into two arrays:
         * one array from which the training data will be extracted,
         * and another array from which the test data will be extracted.
         */
        
        int count = 0;
        for (String doc : docs) {
            
            /**
             * Ensure no overlapping training (test) data points between categories.
             */
            if (this.uniqueDataPoints.contains(doc)) continue;
            
            if (Config.APPLY_FILTER && !isValid(doc)) continue;
            
            if (Config.REMOVE_PUNCTUATION) doc = removePunctuation(doc);
            
            count++;
            if (count % 2 == 0) {
                /**
                 * Assign the minimum required for testing,
                 * and save most documents for training.
                 */
                if (testSource.size() < Config.TEST_DATA_SIZE) {
                    testSource.add(doc);
                } else {
                    trainSource.add(doc);
                }
            } else {
                trainSource.add(doc);
            }
            
        }
        
    }
    
    private boolean isValid(String doc) {
        for (String filter : Config.FILTER) {
            if (doc.contains(filter)) return false;
        }
        return true;
    }
    
    private String removePunctuation(String doc) {
        
        String retVal = doc.replace(".", "");
        retVal = retVal.replace("!", "");
        retVal = retVal.replace("?", "");
        retVal = retVal.replace(",", "");
        retVal = retVal.replace(";", "");
        retVal = retVal.replace(":", "");
        
        retVal = retVal.replace(" - ", " ");

        while (retVal.contains("  ")) retVal = retVal.replace("  ", " ");
        
        return retVal;
        
    }
    
    private void createData(ArrayList<String> source, ArrayList<String> target, int limit) {
        
        while (source.size() > 1 && target.size() < limit) {
            int index = MathToolbox.getRandomNumber(0, source.size() - 1);
            String doc = source.get(index);
            target.add(doc);
            source.remove(index);
        }
        
        if (source.size() > 0 && target.size() < limit) {
            while (source.size() > 0) {
                String doc = source.get(0);
                target.add(doc);
                source.remove(0);
            }
        }
        
    }
    
    private void record(ArrayList<String> target, String targetFolder) throws Exception {
        
        if (!Files.exists(Paths.get(targetFolder))) Files.createDirectories(Paths.get(targetFolder));
        
        targetFolder += Config.TRAINING_TARGET_FILE_NAME;
        if (Files.exists(Paths.get(targetFolder))) Files.delete(Paths.get(targetFolder));
        
        TextFileWriter writer = new TextFileWriter(targetFolder);
        for (String doc : target) {
            writer.write(doc);
        }
        writer.close();
        
    }
    
}
