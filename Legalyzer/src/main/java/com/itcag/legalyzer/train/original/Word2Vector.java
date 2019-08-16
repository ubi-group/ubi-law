package com.itcag.legalyzer.train.original;

import com.itcag.legalyzer.train.*;
import org.datavec.api.util.ClassPathResource;

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

public class Word2Vector {


    private static final Logger LOG = LoggerFactory.getLogger(Word2Vector.class);

    public static void main(String[] args) throws Exception {

        // Gets Path to Text file
        String classPathResource = new ClassPathResource("NewsData").getFile().getAbsolutePath() + File.separator;
        String filePath = new File(classPathResource + File.separator + "RawNewsToGenerateWordVector.txt").getAbsolutePath();

        LOG.info("Load & Vectorize Sentences....");
        // Strip white space before and after for each line
        SentenceIterator iter = new BasicLineIterator(filePath);
        // Split on white spaces in the line to get words
        TokenizerFactory t = new DefaultTokenizerFactory();

        //CommonPreprocessor will apply the following regex to each token: [\d\.:,"'\(\)\[\]|/?!;]+
        //So, effectively all numbers, punctuation symbols and some special symbols are stripped off.
        //Additionally it forces lower case for all tokens.
        t.setTokenPreProcessor(new CommonPreprocessor());

        LOG.info("Building model....");
        Word2Vec vec = new Word2Vec.Builder()
            .minWordFrequency(2)
            .iterations(5)
            .layerSize(100)
            .seed(42)
            .windowSize(20)
            .iterate(iter)
            .tokenizerFactory(t)
            .build();

        LOG.info("Fitting Word2Vec model....");
        vec.fit();

        LOG.info("Writing word vectors to text file....");

        // Write word vectors to file
        WordVectorSerializer.writeWordVectors(vec.lookupTable(), classPathResource + "NewsWordVector.txt");

    }
    
}
