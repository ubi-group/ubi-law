package com.itcag.legalyzer.util;

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;

public class Configuration {

    private enum Fields {

        WORD_2_VEC_DATA_PATH("word2vecDataPath"),
        WORD_2_VEC_PATH("word2vecPath"),

        CATEGORIES_PATH("categoriesPath"),

        DATA_PATH("dataPath"),
        TRAINING_DATA_FOLDER("trainigDataFolder"),
        TEST_DATA_FOLDER("testDataFolder"),

        MODEL_PATH("modelPath"),

        LAST_GENERIC_INDEX("lastGenericIndex"),
        SCORE_THRESHOLD("scoreThreshold"),
        ALL_CATEGORIES("allCategories"),

        ;

        private final String name;

        private Fields(String name) {
            this.name = name;
        }

        public final String getName() {
            return this.name;
        }

    }
    private static Configuration instance = null;
    
    public static Configuration getInstance(String fileName) {
        if (instance == null) instance = new Configuration(fileName);
        return instance;
    }
    
    private final String word2vecDataPath;
    private final String word2vecPath;
    
    private final String categoriesPath;
    
    private final String dataPath;
    private final String trainingDataPath;
    private final String testDataPath;
    
    private final String modelPath;
    
    private final String allCategories;
    
    private final int lastGenericIndex;
    private final double scoreThreshold;
    
    private Configuration(String fileName) {
        
        try {
            
            InputStream input = Configuration.class.getClassLoader().getResourceAsStream(fileName);
            
            Properties prop = new Properties();
            prop.load(input);
            
            this.word2vecDataPath = prop.getProperty(Fields.WORD_2_VEC_DATA_PATH.getName());
            this.word2vecPath = prop.getProperty(Fields.WORD_2_VEC_PATH.getName());
            
            this.categoriesPath = prop.getProperty(Fields.CATEGORIES_PATH.getName());
            
            this.dataPath = prop.getProperty(Fields.DATA_PATH.getName());
            this.trainingDataPath = this.dataPath + prop.getProperty(Fields.TRAINING_DATA_FOLDER.getName());
            this.testDataPath = this.dataPath + prop.getProperty(Fields.TEST_DATA_FOLDER.getName());
            
            this.modelPath = prop.getProperty(Fields.MODEL_PATH.getName());
            
            this.allCategories = getClass().getClassLoader().getResource(prop.getProperty(Fields.ALL_CATEGORIES.getName())).getPath(); 

            this.lastGenericIndex = Integer.parseInt(prop.getProperty(Fields.LAST_GENERIC_INDEX.getName()));
            this.scoreThreshold = Double.parseDouble(prop.getProperty(Fields.SCORE_THRESHOLD.getName()));
            
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    
    }

    public String getWord2vecDataPath() {
        return this.word2vecDataPath;
    }

    public String getWord2vecPath() {
        return this.word2vecPath;
    }

    public String getCategoriesPath() {
        return this.categoriesPath;
    }

    public String getDataPath() {
        return this.dataPath;
    }

    public String getTrainingDataPath() {
        return this.trainingDataPath;
    }

    public String getTestDataPath() {
        return this.testDataPath;
    }

    public String getModelPath() {
        return this.modelPath;
    }

    public double getScoreThreshold() {
        return this.scoreThreshold;
    }

    public int getLastGenericIndex() {
        return this.lastGenericIndex;
    }

    /**
     * @return the allCategories
     */
    public String getAllCategories() {
        return allCategories;
    }

}
