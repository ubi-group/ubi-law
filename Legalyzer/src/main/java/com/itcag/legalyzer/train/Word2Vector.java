package com.itcag.legalyzer.train;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import java.net.URL;

import java.util.Properties;

public class Word2Vector {

    private final Properties config;
    
    private final SentenceIterator sentenceIterator;
    
    private final TokenizerFactory tokenizerFactory;
    
    private final Logger log = LoggerFactory.getLogger(Word2Vector.class);

    public Word2Vector(Properties config) throws Exception {

        this.config = config; 

        sentenceIterator = new BasicLineIterator(config.getProperty("textPath"));

        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

    }
    
    public void execute() throws Exception {

        log.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
            .minWordFrequency(Integer.parseInt(config.getProperty("minWordFrequency")))
            .iterations(Integer.parseInt(config.getProperty("iterations")))
            .layerSize(Integer.parseInt(config.getProperty("layerSize")))
            .seed(Integer.parseInt(config.getProperty("seed")))
            .windowSize(Integer.parseInt(config.getProperty("windowSize")))
            .iterate(sentenceIterator)
            .tokenizerFactory(tokenizerFactory)
            .build();

        log.info("Fitting Word2Vec model....");
        vec.fit();

        log.info("Writing word vectors to text file....");
        WordVectorSerializer.writeWordVectors(vec.lookupTable(), config.getProperty("wordVectorPath"));

    }
    
    public static void main(String[] args) throws Exception {

        Properties config = new Properties();

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource("NewsData");
        String resourcesPath = url.getPath() + File.separator;
        config.setProperty("resourcesPath", resourcesPath);

        config.setProperty("textPath", "/home/nahum/Desktop/hebrew_news/raw.txt");
        config.setProperty("wordVectorPath", "/home/nahum/Desktop/hebrew_news/wordvec.txt");
//        config.setProperty("textPath", resourcesPath  + "RawNewsToGenerateWordVector.txt");
//        config.setProperty("wordVectorPath", resourcesPath + "NewsWordVector.txt");
        config.setProperty("minWordFrequency", "2");
        config.setProperty("iterations", "5");
        config.setProperty("layerSize", "100");
        config.setProperty("seed", "42");
        config.setProperty("windowSize", "20");
//        config.setProperty("", "");

        Word2Vector generator = new Word2Vector(config);
        generator.execute();

    }
    
}
