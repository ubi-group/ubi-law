package com.itcag.dl.eval;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;
import com.itcag.util.io.TextFileReader;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

/**
 * Evaluates training data in a particular category, in order to detect unproductive training data.
 */
public class CategoryEvaluator {
    

    public static void main(String args[]) throws Exception {

        /**
         * The following evaluates a training or a test file for a selected category,
         * and identifies all cases where the highest scoring category is a "confused"
         * category that is also preselected.
         * This class is used to detect sentences in the training and test data that
         * are not sufficiently specific to the corresponding category, so that they
         * can be replaced by more specific sentences.
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
        
        Tester tester = new Tester();
        
        for (Paragraph paragraph : document.getParagraphs()) {

            tester.test(paragraph.getText(), paragraph.getResult());

            int count = 0;
            for (Map.Entry<Integer, Category> entry : paragraph.getEvaluation(categories.get()).entrySet()) {
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
