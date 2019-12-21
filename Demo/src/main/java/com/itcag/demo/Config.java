package com.itcag.demo;

import com.itcag.legalyzer.util.Configuration;
import com.itcag.legalyzer.util.MyConfiguration;

public class Config {

    public final static String WORD_2_VEC_DATA_PATH;
    public final static String WORD_2_VEC_PATH;
    
    public final static String CATEGORIES_PATH;

    public final static String DATA_PATH;
    public final static String TRAINING_DATA_PATH;
    public final static String TEST_DATA_PATH;
    
    public final static String MODEL_PATH;
    
    public final static String ALL_CATEGORIES;
    
    public final static int HIGHEST_GENERIC_INDEX;
    
    public final static double HIGHEST_RANKING = 0.95;

    static {
        
        Configuration tmp = Configuration.getInstance(MyConfiguration.FILE_NAME);

        WORD_2_VEC_DATA_PATH = tmp.getWord2vecDataPath();
        WORD_2_VEC_PATH = tmp.getWord2vecPath();
        
        CATEGORIES_PATH = tmp.getCategoriesPath();
        
        DATA_PATH = tmp.getDataPath();
        TRAINING_DATA_PATH = tmp.getTrainingDataPath();
        TEST_DATA_PATH = tmp.getTestDataPath();
        
        MODEL_PATH = tmp.getModelPath();
        
        ALL_CATEGORIES = tmp.getAllCategories();
        
        HIGHEST_GENERIC_INDEX = tmp.getLastGenericIndex();

        System.out.println("WORD_2_VEC_DATA_PATH=" + WORD_2_VEC_DATA_PATH);
        System.out.println("WORD_2_VEC_PATH=" + WORD_2_VEC_PATH);
        System.out.println("CATEGORIES_PATH=" + CATEGORIES_PATH);
        System.out.println("DATA_PATH=" + DATA_PATH);
        System.out.println("TRAINING_DATA_PATH=" + TRAINING_DATA_PATH);
        System.out.println("TEST_DATA_PATH=" + TEST_DATA_PATH);
        System.out.println("MODEL_PATH=" + MODEL_PATH);
        System.out.println("ALL_CATEGORIES=" + ALL_CATEGORIES);
        System.out.println("HIGHEST_GENERIC_INDEX=" + HIGHEST_GENERIC_INDEX);
        
    }
    
}
