package com.itcag.legalyzer.train;

import java.io.BufferedReader;

import org.deeplearning4j.eval.Evaluation;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.conf.GradientNormalization;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.LSTM;
import org.deeplearning4j.nn.conf.layers.RnnOutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.api.InvocationType;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Trainer {

    private final Properties config;
    
    private final WordVectors wordVectors;
    
    private final ArrayList<String> categories = new ArrayList<>();
    
    private final TokenizerFactory tokenizerFactory;
    
    private final DataIterator trainingData;
    private final DataIterator testData;
    
    private final Logger log = LoggerFactory.getLogger(Trainer.class);

    public Trainer(Properties config) throws Exception {

        this.config = config;
        
        wordVectors = WordVectorSerializer.readWord2VecModel(new File(config.getProperty("wordVectorPath")));
        
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        File file = new File(config.getProperty("categoriesPath"));
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
            reader.close();
        }
        
        trainingData = new DataIterator.Builder()
            .dataDirectory(config.getProperty("dataPath"))
            .wordVectors(wordVectors)
            .batchSize(Integer.parseInt(config.getProperty("batchSize")))
            .truncateLength(Integer.parseInt(config.getProperty("truncateLength")))
            .tokenizerFactory(tokenizerFactory)
            .train(true)
            .build();

        testData = new DataIterator.Builder()
            .dataDirectory(config.getProperty("dataPath"))
            .wordVectors(wordVectors)
            .batchSize(Integer.parseInt(config.getProperty("batchSize")))
            .tokenizerFactory(tokenizerFactory)
            .truncateLength(Integer.parseInt(config.getProperty("truncateLength")))
            .train(false)
            .build();

    }

    public final void execute() throws Exception {
        
        int inputNeurons = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;
        int outputs = trainingData.getLabels().size();

        //Set up network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .updater(new RmsProp(0.0018))
            .l2(1e-5)
            .weightInit(WeightInit.XAVIER)
            .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
            .list()
            .layer( new LSTM.Builder().nIn(inputNeurons).nOut(200)
                .activation(Activation.TANH).build())
            .layer(new RnnOutputLayer.Builder().activation(Activation.SOFTMAX)
                .lossFunction(LossFunctions.LossFunction.MCXENT).nIn(200).nOut(outputs).build())
            .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        System.out.println("Starting training...");
        net.setListeners(new ScoreIterationListener(1), new EvaluativeListener(testData, 1, InvocationType.EPOCH_END));
        net.fit(trainingData, Integer.parseInt(config.getProperty("epochs")));

        log.info("Evaluating...");
        Evaluation eval = net.evaluate(testData);
        log.info(eval.stats());

        net.save(new File(config.getProperty("modelPath")), true);
        log.info("----- Example complete -----");

    }
    
    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource("NewsData");
        String resourcesPath = url.getPath() + File.separator;
        config.setProperty("resourcesPath", resourcesPath);
//        config.setProperty("wordVectorPath", resourcesPath + "NewsWordVector.txt");
//        config.setProperty("modelPath", resourcesPath + "NewsModel.net");
//        config.setProperty("categoriesPath", resourcesPath + "LabelledNews" + File.separator + "categories.txt");
//        config.setProperty("dataPath", resourcesPath + "LabelledNews");
        config.setProperty("wordVectorPath", "/home/nahum/code/ubi-law/hebrew_news/wordvec.txt");
        config.setProperty("modelPath", "/home/nahum/code/ubi-law/hebrew_news/NewsModel.net");
        config.setProperty("categoriesPath", "/home/nahum/code/ubi-law/hebrew_news/WordFilteredNews/categories.txt");
        config.setProperty("dataPath", "/home/nahum/code/ubi-law/hebrew_news/WordFilteredNews");
        config.setProperty("batchSize", "50");
        config.setProperty("truncateLength", "300");
        config.setProperty("epochs", "1000");
//        config.setProperty("", "");

        
        Trainer tester = new Trainer(config);
        tester.execute();
        
    }
    
    private static void insertNetConfiguration() {
        
    }
    
}
