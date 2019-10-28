package com.itcag.dl.eval;

import com.itcag.dl.Config;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;
import com.itcag.util.io.TextFileReader;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Evaluates training data in a particular category, in order to detect unproductive training data.
 */
public class CategoryEvaluator {
    

    public static void main(String args[]) throws Exception {

        /**
         * Example how to test a document.
         */
        
        String documentFilePath = "/home/nahum/Desktop/legaltech/experiments/test/6.txt";
        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        
        int confusedCategory = 19;
        
        ArrayList<String> lines = TextFileReader.read(documentFilePath);
        
        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        Document document = new Document(lines, new SimpleParser(config));

        Categories categories = new Categories(categoryFilePath);
        
        for (Paragraph paragraph : document.getParagraphs()) {
            paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
        }
        
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
        MultiLayerNetwork model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);
        
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        Tester tester = new Tester(wordVectors, model, tokenizerFactory);
        for (Paragraph paragraph : document.getParagraphs()) {

            tester.test(paragraph.getText(), paragraph.getResult());

            int count = 0;
            for (Map.Entry<Integer, Category> entry : paragraph.getResult().getCategoriesSortedByScore().entrySet()) {
                if (entry.getKey() != confusedCategory) break; 
                System.out.println(paragraph.getText());
                System.out.println("\t" + entry.getKey() + "\t" + entry.getValue().getIndex() + "\t" + entry.getValue().getLabel());
                System.out.println();
                count++;
                if (count == 1) break;
            }
        
        }
        
    }

}
