package com.itcag.demo.servlet;

import com.itcag.demo.Config;
import static com.itcag.demo.Config.CATEGORIES_PATH;
import static com.itcag.demo.Config.DATA_PATH;
import static com.itcag.demo.Config.MODEL_PATH;
import static com.itcag.demo.Config.TRAINING_DATA_PATH;
import static com.itcag.demo.Config.WORD_2_VEC_DATA_PATH;
import static com.itcag.demo.Config.WORD_2_VEC_PATH;
import com.itcag.demo.WebConstants;
import com.itcag.demo.util.Categories;
import com.itcag.demo.util.ClassificationCategory;
import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.MyConfiguration;
import java.io.File;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Initiator extends HttpServlet {
    
     @Override
     public void init() throws ServletException {

        try {
System.out.println(MyConfiguration.FILE_NAME);            
System.out.println(WORD_2_VEC_DATA_PATH);
System.out.println(WORD_2_VEC_PATH);
System.out.println(CATEGORIES_PATH);
System.out.println(DATA_PATH);
System.out.println(TRAINING_DATA_PATH);
System.out.println(MODEL_PATH);
            WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
       
            TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
            tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

            ClassificationCategory category = new ClassificationCategory("High Court", Config.MODEL_PATH);

            MultiLayerNetwork model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);

            Tester tester = new Tester();

            category.setTester(tester);

            Categories.addCategory(category);

            System.out.println("Word embedding and model successfully loaded; categories successfully created.");

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
         
     }

}
