package com.itcag.dl.util.inference;

import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.inference.Matrix;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.legalyzer.util.parse.SimpleParser;
import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

public class MatrixFactory {

    private static Categories categories;
    private static Tester tester;
    
    private static double[][] totals;
    private static int[][] counts;
    private static double[][] avgs;
    
    public static void main(String[] args) throws Exception {
        
        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        categories = new Categories(categoryFilePath);
        
        /**
         * Actual category by predicted category.
         */
        totals = new double[categories.get().size()][categories.get().size()];
        counts = new int[categories.get().size()][categories.get().size()];
        avgs = new double[categories.get().size()][categories.get().size()];
        
        tester = new Tester();

        processFolder("/home/nahum/Desktop/legaltech/experiments/test");
        calculateAverages();
        
        Matrix matrix = new Matrix(avgs, categories);
        matrix.print();
        matrix.printCrossReference();
        
    }
    
    private static void processFolder(String folderPath) throws Exception {
        
        File folder = new File(folderPath);
        for (File file : folder.listFiles()) {
            
            if (file.isDirectory()) continue;
            
            int category = Integer.parseInt(file.getName().replace(".txt", ""));
            ArrayList<String> lines = TextFileReader.read(file.getPath());
            
            processFile(category, lines);
            
        }
        
    }
  
    private static void processFile(int actualCategory, ArrayList<String> lines) throws Exception {
        
        Properties config = new Properties();
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        Document document = new Document(lines, new SimpleParser(config));

        for (Paragraph paragraph : document.getParagraphs()) {

            paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
            tester.test(paragraph.getText(), paragraph.getResult());
            
            for (Map.Entry<Integer, Category> entry : paragraph.getResult().getCategories().entrySet()) {
                
                int predictedCategory = entry.getKey();
                double predictedScore = entry.getValue().getScore();
                totals[actualCategory][predictedCategory] += predictedScore;
                counts[actualCategory][predictedCategory]++;
                
            }

        }

    }
    
    private static void calculateAverages() {
        for (int i = 0; i < totals.length; i++) {
            for (int j = 0; j < totals.length; j++) {
                double total = totals[i][j];
                int count = counts[i][j];
                double avg  = total / count;
                avgs[i][j] = avg;
            }
        }
    }
 
}
