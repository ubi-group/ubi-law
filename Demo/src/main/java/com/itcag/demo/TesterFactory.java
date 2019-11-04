package com.itcag.demo;

import com.itcag.demo.util.Categories;
import com.itcag.demo.util.ClassificationCategory;
import com.itcag.dl.eval.Tester;
import java.io.File;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class TesterFactory {


    private static TesterFactory testerFactoryInstance = null; 
  
    private Tester tester; 
  
    private TesterFactory() throws Exception { 
        
            WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
       
            TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
            tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

            ClassificationCategory category = new ClassificationCategory("High Court", Config.MODEL_PATH);

            MultiLayerNetwork model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);

            tester = new Tester();

            category.setTester(tester);

            Categories.addCategory(category);
    } 
  
    public static TesterFactory getInstance() throws Exception {
        
        if (testerFactoryInstance == null) 
            testerFactoryInstance = new TesterFactory(); 
  
        return testerFactoryInstance; 
    } 

    public Tester getTester() {
        
        return tester;
    }
    
}
