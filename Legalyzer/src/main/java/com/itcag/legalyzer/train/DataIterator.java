package com.itcag.legalyzer.train;

import com.itcag.legalyzer.ConfigurationFields;

import org.apache.commons.lang3.tuple.Pair;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.DataSetPreProcessor;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;

import static org.nd4j.linalg.indexing.NDArrayIndex.all;
import static org.nd4j.linalg.indexing.NDArrayIndex.point;

public class DataIterator implements DataSetIterator {

    private final Properties config;
    
    private final WordVectors wordVectors;
    private final TokenizerFactory tokenizerFactory;

    private final int vectorSize;
    
    private int maxLength;

    private final List<Pair<String, List<String>>> categoryData = new ArrayList<>();
    private int cursor = 0;
    private int total = 0;
    private int position = 0;
    private final List<String> labels;
    private int currCategory = 0;

    public DataIterator(Properties config, WordVectors wordVectors, TokenizerFactory tokenizerFactory) {

        this.config = config;
        
        this.wordVectors = wordVectors;
        this.vectorSize = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;

        this.tokenizerFactory = tokenizerFactory;

        this.populateData();

        this.labels = new ArrayList<>();
        for (int i = 0; i < this.categoryData.size(); i++) {
            this.labels.add(this.categoryData.get(i).getKey().split(",")[1]);
        }

    }

    private void populateData() {

        File categories = new File(this.config.getProperty(ConfigurationFields.CATEGORIES_PATH.getName()));

        try {

            try (BufferedReader brCategories = new BufferedReader(new FileReader(categories))) {
                
                String category;
                while ((category = brCategories.readLine()) != null) {
                    
                    String[] elts = category.split(",");
                    if (elts.length != 2) throw new IllegalArgumentException("Invalid category definition: " + category);
                    
                    String fileName = elts[0].trim() + ".txt";
                    
                    String filePath = this.config.getProperty(ConfigurationFields.CURRENT_DATA_PATH.getName()) + fileName;
                    File file = new File(filePath);
                    
                    List<String> tempList;
                    try (BufferedReader currBR = new BufferedReader((new FileReader(file)))) {
                        tempList = new ArrayList<>();
                        String tempCurrLine;
                        while ((tempCurrLine = currBR.readLine()) != null) {
                            tempList.add(tempCurrLine);
                            this.total++;
                        }
                    }
                    
                    Pair<String, List<String>> tempPair = Pair.of(category, tempList);
                    this.categoryData.add(tempPair);
                    
                }
            
            }

        } catch (Exception e) {
            System.out.println("Exception in reading file :" + e.getMessage());
        }
    
    }

    @Override
    public DataSet next(int batchSize) {
        if (cursor >= this.total) throw new NoSuchElementException();
        try {
            return nextDataSet(batchSize);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private DataSet nextDataSet(int batchSize) throws IOException {

        List<String> sentences = new ArrayList<>(batchSize);
        int[] category = new int[batchSize];

        for (int i = 0; i < batchSize && cursor < this.total; i++) {
            if (currCategory < categoryData.size()) {
                sentences.add(this.categoryData.get(currCategory).getValue().get(position));
                category[i] = Integer.parseInt(this.categoryData.get(currCategory).getKey().split(",")[0]);
                currCategory++;
                cursor++;
            } else {
                currCategory = 0;
                position++;
                i--;
            }
        }

        //Second: tokenize sentences and filter out unknown words
        List<List<String>> allTokens = new ArrayList<>(sentences.size());
        maxLength = 0;
        for (String sentence : sentences) {
            List<String> tokens = tokenizerFactory.create(sentence).getTokens();
            List<String> tokensFiltered = new ArrayList<>();
            for (String t : tokens) {
                if (wordVectors.hasWord(t)) tokensFiltered.add(t);
            }
            allTokens.add(tokensFiltered);
            maxLength = Math.max(maxLength, tokensFiltered.size());
        }

        //If longest sentence exceeds 'truncateLength': only take the first 'truncateLength' words
        if (maxLength > Integer.parseInt(this.config.getProperty(ConfigurationFields.TRUNCATE_TEXT_TO.getName()))) maxLength = Integer.parseInt(this.config.getProperty(ConfigurationFields.TRUNCATE_TEXT_TO.getName()));

        //Create data for training
        //Here: we have sentences.size() examples of varying lengths
        INDArray features = Nd4j.create(sentences.size(), vectorSize, maxLength);
        INDArray labels = Nd4j.create(sentences.size(), this.categoryData.size(), maxLength);    //Three labels: Crime, Politics, Bollywood

        //Because we are dealing with sentences of different lengths and only one output at the final time step: use padding arrays
        //Mask arrays contain 1 if data is present at that time step for that example, or 0 if data is just padding
        INDArray featuresMask = Nd4j.zeros(sentences.size(), maxLength);
        INDArray labelsMask = Nd4j.zeros(sentences.size(), maxLength);

        int[] temp = new int[2];
        for (int i = 0; i < sentences.size(); i++) {
            List<String> tokens = allTokens.get(i);
            temp[0] = i;
            //Get word vectors for each word in the sentence, and put them in the training data
            for (int j = 0; j < tokens.size() && j < maxLength; j++) {
                String token = tokens.get(j);
                INDArray vector = wordVectors.getWordVectorMatrix(token);
                features.put(new INDArrayIndex[]{point(i),
                    all(),
                    point(j)}, vector);
                temp[1] = j;
                featuresMask.putScalar(temp, 1.0);
            }
            int idx = category[i];
            int lastIdx = Math.min(tokens.size(), maxLength);
            labels.putScalar(new int[]{i, idx, lastIdx - 1}, 1.0);
            labelsMask.putScalar(new int[]{i, lastIdx - 1}, 1.0);
        }

        DataSet ds = new DataSet(features, labels, featuresMask, labelsMask);
        return ds;
    }

    @Override
    public int inputColumns() {
        return vectorSize;
    }

    @Override
    public int totalOutcomes() {
        return this.categoryData.size();
    }

    @Override
    public void reset() {
        cursor = 0;
        position = 0;
        currCategory = 0;
    }

    @Override
    public boolean resetSupported() {
        return true;
    }

    @Override
    public boolean asyncSupported() {
        return true;
    }

    @Override
    public int batch() {
        return Integer.parseInt(this.config.getProperty(ConfigurationFields.BATCH_SIZE.getName()));
    }

    @Override
    public void setPreProcessor(DataSetPreProcessor preProcessor) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<String> getLabels() {
        return this.labels;
    }

    @Override
    public boolean hasNext() {
        return cursor < this.total;
    }

    @Override
    public DataSet next() {
        return next(Integer.parseInt(this.config.getProperty(ConfigurationFields.BATCH_SIZE.getName())));
    }

    @Override
    public void remove() {

    }

    @Override
    public DataSetPreProcessor getPreProcessor() {
        throw new UnsupportedOperationException("Not implemented");
    }

    public int getMaxLength() {
        return this.maxLength;
    }

}
