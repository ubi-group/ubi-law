package com.itcag.legalyzer.train;

import com.itcag.legalyzer.ConfigurationFields;
import com.itcag.legalyzer.Constants;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.FileSentenceIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.util.Properties;

public class Word2Vector {

    private final Properties config;
    
    private final SentenceIterator sentenceIterator;
    
    private final TokenizerFactory tokenizerFactory;
    
    private final Logger log = LoggerFactory.getLogger(Word2Vector.class);

    public Word2Vector(Properties config) throws Exception {

        this.config = config; 

        sentenceIterator = new FileSentenceIterator(new File(config.getProperty(ConfigurationFields.DATA_PATH.getName())));

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

    }
    
    public void execute() throws Exception {

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
            .minWordFrequency(Integer.parseInt(config.getProperty(ConfigurationFields.MIN_WORD_FREQUENCY.getName())))
            .iterations(Integer.parseInt(config.getProperty(ConfigurationFields.ITERATIONS.getName())))
            .layerSize(Integer.parseInt(config.getProperty(ConfigurationFields.LAYER_SIZE.getName())))
            .seed(Integer.parseInt(config.getProperty(ConfigurationFields.SEED.getName())))
            .windowSize(Integer.parseInt(config.getProperty(ConfigurationFields.WINDOW_SIZE.getName())))
            .iterate(sentenceIterator)
            .tokenizerFactory(tokenizerFactory)
            .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");
        WordVectorSerializer.writeWordVectors(vec.lookupTable(), config.getProperty(ConfigurationFields.WORD_VECTOR_PATH.getName()));

    }
    
    public static void main(String[] args) throws Exception {

        Properties config = new Properties();

        config.setProperty(ConfigurationFields.DATA_PATH.getName(), Constants.WORD_2_VEC_DATA_PATH);
        config.setProperty(ConfigurationFields.WORD_VECTOR_PATH.getName(), Constants.WORD_2_VEC_PATH);
        config.setProperty(ConfigurationFields.MIN_WORD_FREQUENCY.getName(), "2");
        config.setProperty(ConfigurationFields.ITERATIONS.getName(), "5");
        config.setProperty(ConfigurationFields.LAYER_SIZE.getName(), "500");
        config.setProperty(ConfigurationFields.SEED.getName(), "42");
        config.setProperty(ConfigurationFields.WINDOW_SIZE.getName(), "7");
//        config.setProperty("", "");

        Word2Vector generator = new Word2Vector(config);
        generator.execute();

    }
    
}
