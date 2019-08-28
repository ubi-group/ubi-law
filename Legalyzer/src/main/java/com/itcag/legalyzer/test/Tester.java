package com.itcag.legalyzer.test;

import com.itcag.legalyzer.ConfigurationFields;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

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

    private static Tester instance = null;
    
    private final WordVectors wordVectors;
    private final MultiLayerNetwork model;
    
    private final TokenizerFactory tokenizerFactory;

    private Tester(Properties config) throws Exception {

        wordVectors = WordVectorSerializer.readWord2VecModel(new File(config.getProperty(ConfigurationFields.WORD_VECTOR_PATH.getName())));
        model = MultiLayerNetwork.load(new File(config.getProperty(ConfigurationFields.MODEL_PATH.getName())), true);
        
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

    }
    
    public final static Tester getInstance(Properties config) throws Exception {
        if (instance == null) instance = new Tester(config);
        return instance;
    }
    
    public final static Tester getInstance() {
        return instance;
    }
    
    public void test(String input, Result result) throws Exception {
        
        if (input == null) throw new IllegalArgumentException("Input is null.");
        input = input.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("Input is empty.");
        
        DataSet testData = prepareTestData(input);
        
        INDArray fet = testData.getFeatures();
        
        INDArray predicted = model.output(fet, false);
        long[] arrsiz = predicted.shape();

        for (int i = 0; i < arrsiz[1]; i++) {
            double sumNum = (double) predicted.slice(0).slice(i).sumNumber();
            result.insertScore(i, sumNum);
        }

    }
    
    private DataSet prepareTestData(String input) throws Exception {
    
        List<String> tokens = new ArrayList<>();
        for (String token : tokenizerFactory.create(input).getTokens()) {
            if (wordVectors.hasWord(token)) tokens.add(token);
        }
        if (tokens.isEmpty()) throw new IllegalArgumentException("Input consists of unrecognized words.");
        
        INDArray features = Nd4j.create(1, wordVectors.lookupTable().layerSize(), tokens.size());
        INDArray labels = Nd4j.create(1, 4, tokens.size());
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

    public static void main(String args[]) throws Exception {

        Properties config = new Properties();
        
        config.setProperty(ConfigurationFields.WORD_VECTOR_PATH.getName(), "/home/nahum/Desktop/hebrew/wordvec.txt");
        config.setProperty(ConfigurationFields.MODEL_PATH.getName(), "/home/nahum/code/ubi-law/hebrew_news/NewsModel.net");
        config.setProperty(ConfigurationFields.CATEGORIES_PATH.getName(), "/home/nahum/code/ubi-law/hebrew_news/LabelledNews/categories.txt");
        
        Result result = new Result(config.getProperty(ConfigurationFields.CATEGORIES_PATH.getName()));
            
        Tester tester = Tester.getInstance(config);
        
        ArrayList<String> lines = readFile("/home/nahum/code/ubi-law/hebrew_news/LabelledNews/train/3.txt");
        for (String line : lines) {
            
            try {

                Result copy = result.copy();
                tester.test(line, copy);
                if (copy.getTopCategory().getIndex() != 3) {
                    System.out.println("FAIL!");
                    System.out.println("Wrong guess: " + copy.getTopCategory().getLabel());
                    System.out.println(line);
                    System.out.println(copy.toString());
                    System.out.println();
                }
//                System.out.println(copy.toString());
//                System.out.println();
                
            } catch (Exception ex) {
//                System.out.println(line);
//                System.out.println("ERROR: " + ex.getMessage());
//                System.out.println();
            }
        
        }
        
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
