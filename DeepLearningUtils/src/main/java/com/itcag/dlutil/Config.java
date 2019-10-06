package com.itcag.dlutil;

public class Config {

    public final static int MIN_PHRASE_SIZE = 1;
    public final static int MAX_PHRASE_SIZE = 6;
    
    public final static int MAX_THREADS = 30;
    public final static int THREADS_TIMEOUT = 60000;
    
    public final static int MAIN_THRESHOLD = 10;
    public final static int CLEANING_THRESHOLD = 10;
    
    public final static int MAX_SAMPLE_SENTENCES = 10;
    
    public final static boolean APPLY_FILTER = true;
    /* Generic filter for the subject of purchase
    public final static String[] FILTER = {"buy", "bought", "purchas", "order", "pay", "spend", "price", "cost", "afford", "expens", "$", "dollar", "money", "worth", "value"};
    */
    public final static String[] FILTER = {"צו להוצאות"};
    
    public final static boolean APPLY_NEGATIVE_FILTER = false;
    public final static String[] NEGATIVE_FILTER = {""};

    /***** WORD2VEC *****/
    public final static String WORD_2_VEC_SOURCE_DATA_PATH = "/home/nahum/Desktop/hebrew/high court rulings/";

    public final static String WORD_2_VEC_DATA_PATH = "/home/nahum/Desktop/hebrew/combined/";
    public final static String WORD_2_VEC_PATH = "/home/nahum/Desktop/hebrew/wordvec.txt";
    
    public final static boolean SPLIT_TEXT_INTO_SENTENCES = true;
    
    /***** TRAINING *****/
    public final static int TRAINING_DATA_SIZE = 100;
    public final static int TEST_DATA_SIZE = 100;

//    public final static String TRAINING_SOURCE_FILE_NAME = "/home/nahum/Desktop/legaltech/experiments/original/Criminal - Violence.txt";
//    public final static String TRAINING_SOURCE_FILE_NAME = "/home/nahum/Desktop/legaltech/experiments/original/National security, military, and the territories.txt";
    public final static String TRAINING_SOURCE_FILE_NAME = "/home/nahum/Desktop/legaltech/experiments/original/Labor and Employment.txt";
//    public final static String TRAINING_SOURCE_FILE_NAME = "/home/nahum/Desktop/legaltech/experiments/original/Civil - Land.txt";

    public final static String TRAINING_TARGET_FOLDER_PATH = "/home/nahum/Desktop/legaltech/experiments/";
    public final static String TRAINING_DATA_PATH = TRAINING_TARGET_FOLDER_PATH + "train/";
    public final static String TEST_DATA_PATH = TRAINING_TARGET_FOLDER_PATH + "test/";
    public final static String TRAINING_TARGET_FILE_NAME = "2.txt";
    
    public final static boolean REMOVE_PUNCTUATION = false;
    
    /***** TEXT MANIPULATION *****/
    public final static String TEXT_SOURCE_FOLDER = "/home/nahum/Desktop/hebrew/wikipedia/";
    
    public final static int TEXT_FOO_THRESHOLD = 5;
    
}
