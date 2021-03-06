package com.itcag.dl.eval;

import com.itcag.dl.ConfigurationFields;
import com.itcag.dl.Config;
import com.itcag.dl.train.DataIterator;
import com.itcag.dl.train.Trainer;

import java.io.File;

import java.util.Properties;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROC;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Evaluator {
    
    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        
        config.setProperty(ConfigurationFields.WORD_VECTOR_PATH.getName(), Config.WORD_2_VEC_PATH);
        config.setProperty(ConfigurationFields.MODEL_PATH.getName(), Config.MODEL_PATH);
        config.setProperty(ConfigurationFields.CATEGORIES_PATH.getName(), Config.CATEGORIES_PATH);
        config.setProperty(ConfigurationFields.DATA_PATH.getName(), Config.DATA_PATH);
        config.setProperty(ConfigurationFields.TEST_DATA_PATH.getName(), Config.TEST_DATA_PATH);
        
        config.setProperty(ConfigurationFields.TRUNCATE_TEXT_TO.getName(), "300");
        config.setProperty(ConfigurationFields.BATCH_SIZE.getName(), "50");

        Evaluator evaluator = new Evaluator(config);
        evaluator.execute();
        
    }
    
    private final Properties config;
    
    private final WordVectors wordVectors;
    private final MultiLayerNetwork model;
    
    private final TokenizerFactory tokenizerFactory;
    
    private final DataIterator testData;
    
    private final Logger log = LoggerFactory.getLogger(Trainer.class);

    public Evaluator(Properties config) throws Exception {

        this.config = config;
        
        wordVectors = WordVectorSerializer.readWord2VecModel(new File(config.getProperty(ConfigurationFields.WORD_VECTOR_PATH.getName())));
        model = MultiLayerNetwork.load(new File(config.getProperty(ConfigurationFields.MODEL_PATH.getName())), true);
        
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        config.setProperty(ConfigurationFields.CURRENT_DATA_PATH.getName(), Config.TEST_DATA_PATH);
        testData = new DataIterator(config, wordVectors, tokenizerFactory);

    }

    public final void execute() throws Exception {
        
        Evaluation eval = model.evaluate(testData);
        log.info(eval.stats());

        ROC roc = model.evaluateROC(testData, 0);
        log.info(roc.stats());
        
    }
    
}
