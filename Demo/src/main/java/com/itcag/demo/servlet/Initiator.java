package com.itcag.demo.servlet;

import com.itcag.demo.WebConstants;
import com.itcag.demo.util.Categories;
import com.itcag.demo.util.ClassificationCategory;
import com.itcag.dl.eval.Tester;
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

            WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(WebConstants.WORD_VECTOR_PATH));
        
            TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
            tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());
            
            for (Map.Entry<String, String> entry : WebConstants.MODEL_PATHS.entrySet()) {

                ClassificationCategory category = new ClassificationCategory(entry.getKey(), entry.getValue());

                MultiLayerNetwork model = MultiLayerNetwork.load(new File(entry.getValue()), true);
                
                Tester tester = new Tester();
                category.setTester(tester);

                Categories.addCategory(category);
                
            }

            System.out.println("Word embedding and model successfully loaded; categories successfully created.");

        } catch (Exception ex) {
            throw new ServletException(ex);
        }
         
     }

}
