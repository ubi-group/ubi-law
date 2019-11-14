package com.itcag.dl.train;

import com.itcag.dl.ConfigurationFields;
import com.itcag.dl.Config;

import java.io.File;

import java.util.Properties;

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

import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.evaluation.classification.ROC;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.RmsProp;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.schedule.ScheduleType;
import org.nd4j.linalg.schedule.StepSchedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Trainer {

    private final Properties config;
    
    private final WordVectors wordVectors;
    
    private final TokenizerFactory tokenizerFactory;
    
    private final DataIterator trainingData;
    private final DataIterator testData;
    
    private final Logger log = LoggerFactory.getLogger(Trainer.class);

    public Trainer(Properties config) throws Exception {

        this.config = config;
        
        wordVectors = WordVectorSerializer.readWord2VecModel(new File(config.getProperty(ConfigurationFields.WORD_VECTOR_PATH.getName())));
        
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        config.setProperty(ConfigurationFields.CURRENT_DATA_PATH.getName(), Config.TRAINING_DATA_PATH);
        trainingData = new DataIterator(config, wordVectors, tokenizerFactory);

        config.setProperty(ConfigurationFields.CURRENT_DATA_PATH.getName(), Config.TEST_DATA_PATH);
        testData = new DataIterator(config, wordVectors, tokenizerFactory);

    }

    public final void execute() throws Exception {
        
        int inputNeurons = wordVectors.getWordVector(wordVectors.vocab().wordAtIndex(0)).length;
        int outputs = trainingData.getLabels().size();

        double learningRate = Double.parseDouble(config.getProperty(ConfigurationFields.LEARNING_RATE.getName()));
        double decayRate = Double.parseDouble(config.getProperty(ConfigurationFields.DECAY_RATE.getName()));
        double step = Double.parseDouble(config.getProperty(ConfigurationFields.DECAY_STEP.getName()));
        
        //Set up network configuration
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            /**
             * StepSchedule receives 4 arguments:
             *      schedule type is either iteration or epoch,
             *      initial learning rate,
             *      decay rate,
             *      step size.
             * The decay is calculated by this formula:
             *    initial learning rate * decay rate^(floor(epoch/step))
             */
            .updater(new RmsProp(new StepSchedule(ScheduleType.EPOCH, learningRate, decayRate, step)))
            .l2(1e-5)
            .weightInit(WeightInit.XAVIER)
            .gradientNormalization(GradientNormalization.ClipElementWiseAbsoluteValue).gradientNormalizationThreshold(1.0)
            .list()
            .layer( new LSTM.Builder().nIn(inputNeurons).nOut(200)
                .activation(Activation.TANH).build())
            .layer( new LSTM.Builder().nIn(200).nOut(200)
                .activation(Activation.TANH).build())
            .layer( new LSTM.Builder().nIn(200).nOut(200)
                .activation(Activation.TANH).build())
            .layer( new LSTM.Builder().nIn(200).nOut(200)
                .activation(Activation.TANH).build())
            .layer( new LSTM.Builder().nIn(200).nOut(200)
                .activation(Activation.TANH).build())
            .layer(new RnnOutputLayer.Builder().activation(Activation.SIGMOID)
                .lossFunction(LossFunctions.LossFunction.XENT).nIn(200).nOut(outputs).build())
            .build();

        MultiLayerNetwork net = new MultiLayerNetwork(conf);
        net.init();

        net.setListeners(new ScoreIterationListener(1), new EvaluativeListener(testData, 1, InvocationType.EPOCH_END));

        log.info("Starting training...");
        net.fit(trainingData, Integer.parseInt(config.getProperty("epochs")));

        net.save(new File(config.getProperty(ConfigurationFields.MODEL_PATH.getName())), true);
        log.info("----- Example complete -----");

        log.info("Evaluating...");

        Evaluation eval = net.evaluate(testData);
        log.info(eval.stats());

//        ROC roc = net.evaluateROC(testData, 0);
//        log.info(roc.stats());
        
    }
    
    public static void main(String[] args) throws Exception {

        Properties config = new Properties();
        
        config.setProperty(ConfigurationFields.WORD_VECTOR_PATH.getName(), Config.WORD_2_VEC_PATH);
        config.setProperty(ConfigurationFields.MODEL_PATH.getName(), Config.MODEL_PATH);
        config.setProperty(ConfigurationFields.CATEGORIES_PATH.getName(), Config.CATEGORIES_PATH);
        config.setProperty(ConfigurationFields.DATA_PATH.getName(), Config.DATA_PATH);
        config.setProperty(ConfigurationFields.TRAINING_DATA_PATH.getName(), Config.TRAINING_DATA_PATH);
        config.setProperty(ConfigurationFields.TEST_DATA_PATH.getName(), Config.TEST_DATA_PATH);
        
        config.setProperty(ConfigurationFields.TRUNCATE_TEXT_TO.getName(), "300");
        config.setProperty(ConfigurationFields.BATCH_SIZE.getName(), "50");
        config.setProperty(ConfigurationFields.EPOCHS.getName(), "100");
        
        config.setProperty(ConfigurationFields.LEARNING_RATE.getName(), "0.0018");
        config.setProperty(ConfigurationFields.DECAY_RATE.getName(), "0.1");
        config.setProperty(ConfigurationFields.DECAY_STEP.getName(), "25");
//        config.setProperty("", "");

        
        Trainer tester = new Trainer(config);
        tester.execute();
        
    }
    
    private static void insertNetConfiguration() {
        
    }
    
}
