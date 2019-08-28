package com.itcag.legalyzer;

public enum ConfigurationFields {

    WORD_VECTOR_PATH("wordVectorPath"),
    MODEL_PATH("modelPath"),
    CATEGORIES_PATH("categoriesPath"),
    DATA_PATH("dataPath"),
    
    TRUNCATE_TEXT_TO("truncateLength"),
    BATCH_SIZE("batchSize"),
    EPOCHS("epochs"),

    LEARNING_RATE("learningRate"),
    DECAY_RATE("decayRate"),
    DECAY_STEP("step"),
    
    /**
     * Word2Vec
     */
    MIN_WORD_FREQUENCY("minWordFrequency"),
    ITERATIONS("iterations"),
    LAYER_SIZE("layerSize"),
    SEED("seed"),
    WINDOW_SIZE("windowSize"),
    
    ;
        
    private final String name;
    
    private ConfigurationFields(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
}
