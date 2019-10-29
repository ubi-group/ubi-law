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

        SCORE_THRESHOLD("scoreThreshold"),

        MAXIMUM_ANCHORS("maxAnchors"),
        EXCLUDE_TAG_FROM_ANCHORS("excludeTagsFromAnchors"),

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
    
    private final double scoreThreshold;
    
    private final int maximumAnchors;
    private final boolean excludeTagFromAnchors;
    
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
            
            this.scoreThreshold = Double.parseDouble(prop.getProperty(Fields.SCORE_THRESHOLD.getName()));
            
            this.maximumAnchors = Integer.parseInt(prop.getProperty(Fields.MAXIMUM_ANCHORS.getName()));
            this.excludeTagFromAnchors = Boolean.valueOf(prop.getProperty(Fields.EXCLUDE_TAG_FROM_ANCHORS.getName()));
        
        } catch (IOException ex) {
            throw new IllegalArgumentException(ex);
        }
    
    }

    public String getWord2vecDataPath() {
        return word2vecDataPath;
    }

    public String getWord2vecPath() {
        return word2vecPath;
    }

    public String getCategoriesPath() {
        return categoriesPath;
    }

    public String getDataPath() {
        return dataPath;
    }

    public String getTrainingDataPath() {
        return trainingDataPath;
    }

    public String getTestDataPath() {
        return testDataPath;
    }

    public String getModelPath() {
        return modelPath;
    }

    public double getScoreThreshold() {
        return scoreThreshold;
    }

    public int getMaximumAnchors() {
        return maximumAnchors;
    }

    public boolean isExcludeTagFromAnchors() {
        return excludeTagFromAnchors;
    }

}
