package com.itcag.scraper.court_rulings;

import com.itcag.util.MathToolbox;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Reads files in a folder created by the Extractor.
 * Every file contains sentences extracted from the original documents.
 * Every file is named by the corresponding category.
 * A list of categories is created and recorded in a format that is used by the DL4J.
 * For each category a training set and a test set are created and recorded in the specified folder.
 */
public class TrainingDataFactory {

    public static class InsufficientDataException extends Exception {
        public InsufficientDataException(String msg) {
            super(msg);
        }
    }
    
    private final static String SOURCE_DATA_FOLDER = "/home/nahum/Desktop/legaltech/experiments/original/";
    private final static String GENERIC_DATA_FILE = "/home/nahum/Desktop/legaltech/experiments/generic.txt";
    
    private final static String TARGET_DATA_FOLDER = "/home/nahum/Desktop/legaltech/experiments/";
    private final static String TRAIN_DATA_FOLDER = "train/";
    private final static String TEST_DATA_FOLDER = "test/";
    
    private final static String CATEGORIES_FILE = "/home/nahum/Desktop/legaltech/experiments/categories.txt";

    private final static int TRAIN_DATA_SIZE = 100;
    private final static int TEST_DATA_SIZE = 50;
    
    private final static ArrayList<String> CATEGORIES = new ArrayList<>();
    
    private final static HashMap<String, String> UNIQUE = new HashMap<>();
    private final static HashSet<String> REJECTS = new HashSet<>();
    
    private final static HashSet<String> GENERIC = new HashSet<>();
    
    private final static HashMap<String, HashSet<String>> DATA = new HashMap<>();
    
    public static void main(String[] args) throws Exception {
        
        createDestinationFolders();

        GENERIC.addAll(TextFileReader.read(GENERIC_DATA_FILE));
        
        createUniqueData();
        invertUnique();

        processCategories();
        processGeneric();
        
        recordCategoryFile();
        
    }
    
    private static void createDestinationFolders () throws Exception {
        
        String targetFolder = TARGET_DATA_FOLDER + TRAIN_DATA_FOLDER;
        if (Files.exists(Paths.get(targetFolder))) {
            Files.walk(Paths.get(targetFolder)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);        
        }
        Files.createDirectories(Paths.get(targetFolder));

        targetFolder = TARGET_DATA_FOLDER + TEST_DATA_FOLDER;
        if (Files.exists(Paths.get(targetFolder))) {
            Files.walk(Paths.get(targetFolder)).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);        
        }
        Files.createDirectories(Paths.get(targetFolder));

    }
    
    private static void createUniqueData() throws Exception {
        
        File folder = new File(SOURCE_DATA_FOLDER);
        
        for (File file : folder.listFiles()) {
            
            if (file.isDirectory()) continue;

            String category = file.getName().replace(".txt", "").replace(",", "").replace(" ", "_");
            
            ArrayList<String> lines = TextFileReader.read(file.getPath());
            for (String line : lines) {
                if (REJECTS.contains(line)) continue;
                if (UNIQUE.containsKey(line)) {
                    /**
                     * Files can potentially contain duplicate lines.
                     */
                    if (UNIQUE.get(line).equals(category)) continue;
/*
System.out.println(file.getName());
System.out.println(UNIQUE.get(line));
System.out.println(line);
System.out.println();
*/
                    UNIQUE.remove(line);
                    REJECTS.add(line);
                } else {
                    UNIQUE.put(line, category);
                }
            }
            
        }

    }

    private static void invertUnique() {
     
        for (Map.Entry<String, String> entry : UNIQUE.entrySet()) {
            
            if (DATA.containsKey(entry.getValue())) {
                DATA.get(entry.getValue()).add(entry.getKey());
            } else {
                DATA.put(entry.getValue(), new HashSet<>(Arrays.asList(entry.getKey())));
            }
            
        }
        
    }

    private static void processCategories() throws Exception {
        
        CATEGORIES.add("0,Generic");
        
        int count = 1;
        
        for (Map.Entry<String, HashSet<String>>entry : DATA.entrySet()) {

            ArrayList<ArrayList<String>> data = splitData(new ArrayList<>(entry.getValue()));
            if (data.get(0).size() < TRAIN_DATA_SIZE) continue;
            if (data.get(0).size() < TEST_DATA_SIZE) continue;
            
            ArrayList<String> train = new ArrayList<>();
            while (train.size() < TRAIN_DATA_SIZE) {
                createData(data.get(0), train, TRAIN_DATA_SIZE);
            }

            ArrayList<String> test = new ArrayList<>();
            while (test.size() < TEST_DATA_SIZE) {
                createData(data.get(1), test, TEST_DATA_SIZE);
            }

            recordData(train, TARGET_DATA_FOLDER + TRAIN_DATA_FOLDER + Integer.toString(count) + ".txt");
            recordData(test, TARGET_DATA_FOLDER + TEST_DATA_FOLDER + Integer.toString(count) + ".txt");

            String category = Integer.toString(count) + "," + entry.getKey();
            CATEGORIES.add(category);
            
            count++;
            
        }
        
    }
    
    private static void processGeneric() throws Exception {
        
        ArrayList<ArrayList<String>> data = splitData(new ArrayList<>(GENERIC));
        if (data.get(0).size() < TRAIN_DATA_SIZE) throw new InsufficientDataException("Insuffcient train data for generic.");
        if (data.get(0).size() < TEST_DATA_SIZE) throw new InsufficientDataException("Insuffcient test data for generic.");

        ArrayList<String> train = new ArrayList<>();
        while (train.size() < TRAIN_DATA_SIZE) {
            createData(data.get(0), train, TRAIN_DATA_SIZE);
        }

        ArrayList<String> test = new ArrayList<>();
        while (test.size() < TEST_DATA_SIZE) {
            createData(data.get(1), test, TEST_DATA_SIZE);
        }

        recordData(train, TARGET_DATA_FOLDER + TRAIN_DATA_FOLDER + "0.txt");
        recordData(test, TARGET_DATA_FOLDER + TEST_DATA_FOLDER + "0.txt");
        
    }
    
    private static ArrayList<ArrayList<String>> splitData(ArrayList<String> data) {
        
        ArrayList<ArrayList<String>> retVal = new ArrayList<>();
        
        ArrayList<String> train = new ArrayList<>();
        ArrayList<String> test = new ArrayList<>();

        int count = 0;
        
        for (String datum : data) {
            if (count%2 == 0) {
                
                if (test.size() < TEST_DATA_SIZE) {
                    test.add(datum);
                } else {
                    train.add(datum);
                }
                
            } else {
                train.add(datum);
            }
        }

        retVal.add(train);
        retVal.add(test);
        
        return retVal;
        
    }
    
    private static void createData(ArrayList<String> source, ArrayList<String> target, int limit) {
        
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

    private static void recordCategoryFile() throws Exception {
        
        TextFileWriter writer = new TextFileWriter(CATEGORIES_FILE);
        for (String category : CATEGORIES) {
            writer.write(category);
        }
        writer.close();

    }
    
    private static void recordData(ArrayList<String> data, String filePath) throws Exception {
        
        TextFileWriter writer = new TextFileWriter(filePath);
        for (String doc : data) {
            writer.write(doc);
        }
        writer.close();
        
    }
    
}
