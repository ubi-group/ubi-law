package com.itcag.legalyzer.eval;

import com.itcag.legalyzer.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

public class Tester {

    public static void main(String args[]) throws Exception {

        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Constants.WORD_2_VEC_PATH));
        MultiLayerNetwork model = MultiLayerNetwork.load(new File(Constants.MODEL_PATH), true);
        
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
            
        Tester tester = new Tester(wordVectors, model, tokenizerFactory);

        Result result = new Result(Constants.CATEGORIES_PATH);

        ArrayList<String> lines = readFile("/home/nahum/Desktop/legaltech/experiments/test/2.txt");
        for (String line : lines) {
            
            try {

                Result copy = result.copy();
                tester.test(line, copy);
                if (copy.getTopCategory().getIndex() != 2) {
                    System.out.println(line);
                    System.out.println(copy.toString());
                    System.out.println();
                }
                
            } catch (Exception ex) {
                System.out.println(line);
                System.out.println("ERROR: " + ex.getMessage());
                System.out.println();
            }
        
        }

    }

    private final WordVectors wordVectors;
    private final MultiLayerNetwork model;
    
    private final TokenizerFactory tokenizerFactory;

    public Tester(WordVectors wordVectors, MultiLayerNetwork model, TokenizerFactory tokenizerFactory) throws Exception {
        this.wordVectors = wordVectors;
        this.model = model;
        this.tokenizerFactory = tokenizerFactory;
    }
    
    public void test(String input, Result result) throws Exception {
        
        if (input == null) throw new IllegalArgumentException("Input is null.");
        input = input.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("Input is empty.");
        
        DataSet testData = prepareTestData(input, result.getCategories().size());
        
        INDArray features = testData.getFeatures();
        
        INDArray predicted = model.output(features, false);

        for (int i = 0 ; i < predicted.size(0); i++) {
            INDArray row = predicted.slice(i);
            for (int j = 0; j < predicted.size(1); j++) {
                INDArray column = row.slice(j);
                /**
                 * We are using RNN were the probabilities
                 * are update for every word in the sentence.
                 * Therefore, only the probability of last word
                 * really matters - it depends on all previous
                 * probabilities.
                 */
                Double probability = column.getDouble(column.length() - 1);
                result.insertScore(j, probability);
            }
        }

    }
    
    private DataSet prepareTestData(String input, int numCategories) throws Exception {
    
        List<String> tokens = new ArrayList<>();
        for (String token : tokenizerFactory.create(input).getTokens()) {
            if (wordVectors.hasWord(token)) tokens.add(token);
        }
        if (tokens.isEmpty()) throw new IllegalArgumentException("Input consists of unrecognized words.");
        
        INDArray features = Nd4j.create(1, wordVectors.lookupTable().layerSize(), tokens.size());
        INDArray labels = Nd4j.create(1, numCategories, tokens.size());
        INDArray featuresMask = Nd4j.zeros(1, tokens.size());
        INDArray labelsMask = Nd4j.zeros(1, tokens.size());

        int[] tmp = new int[2];
        tmp[0] = 0;
        for (int j = 0; j < tokens.size() && j < tokens.size(); j++) {
            String token = tokens.get(j);
            INDArray vector = wordVectors.getWordVectorMatrix(token);
            features.put(new INDArrayIndex[]{NDArrayIndex.point(0), NDArrayIndex.all(), NDArrayIndex.point(j)}, vector);
            tmp[1] = j;
            featuresMask.putScalar(tmp, 1.0);
        }
        labels.putScalar(new int[]{0, 0, tokens.size() - 1}, 1.0);
        labelsMask.putScalar(new int[]{0, tokens.size() - 1}, 1.0);

        DataSet retVal = new DataSet(features, labels, featuresMask, labelsMask);
        return retVal;
    
    }

    private static ArrayList<String> readFile(String filePath) throws Exception {
        
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException(filePath);

        ArrayList<String> retVal = new ArrayList<>();
        
        try (InputStream input = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                if (!line.isEmpty()) retVal.add(line);
                line = reader.readLine();
            }
        }
        
        return retVal;
    
    }

}
