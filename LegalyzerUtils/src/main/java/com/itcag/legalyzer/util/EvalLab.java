package com.itcag.legalyzer.util;

import com.itcag.dl.Config;
import com.itcag.dl.eval.Tester;
import com.itcag.dlutil.Categories;
import com.itcag.dlutil.eval.SigmoidResult;
import com.itcag.dlutil.lang.Category;
import com.itcag.dlutil.lang.Document;
import com.itcag.dlutil.lang.Paragraph;
import com.itcag.dlutil.parse.CourtRulingParser;
import com.itcag.util.MathToolbox;
import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class EvalLab {

    /**
     * Key = document ID,
     * Value = list of category indices.
     */
    private final static HashMap<String, ArrayList<Integer>> DOCS = new HashMap<>();
    
    private final static HashMap<Integer, CatStat> STATS = new HashMap<>();

    public static void main(String[] args) throws Exception {

        String documentFolder = "/home/nahum/Desktop/hebrew/high court rulings/";
        
        Categories categories = new Categories("/home/nahum/Desktop/legaltech/experiments/categories.txt");
        
        loadDocuments("/home/nahum/Desktop/legaltech/verdicts", categories);
        
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
        MultiLayerNetwork model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);
        
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        Tester tester = new Tester(wordVectors, model, tokenizerFactory);

        for (Map.Entry<String, ArrayList<Integer>> entry : DOCS.entrySet()) {
            
            try {
                
                String fileName = entry.getKey() + ".txt";
                ArrayList<String> lines = TextFileReader.read(documentFolder + fileName);

                Document document = new Document(lines);
                document.selectParagraphs(new CourtRulingParser(6, 300));

                for (Paragraph paragraph : document.getParagraphs()) {
                    paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
                }

                for (Paragraph paragraph : document.getParagraphs()) {
                    tester.test(paragraph.getText(), paragraph.getResult());
                }

                TreeMap<Double, ArrayList<Category>> evaluation = document.evaluate(categories.get());
                processEvaluation(entry.getKey(), categories, evaluation);

            } catch (Exception ex) {
                //DO NOTHING!
            }
        }
      
        doStats();
        
    }
    
    private static void loadDocuments(String documentIndexFilePath, Categories categories) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(documentIndexFilePath);
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            
            String id = elts[0].trim();
            
            Integer tag1 = null;
            Integer tag2 = null;

            String tagName1 = null;
            String tagName2 = null;

            if (elts.length > 2 && !elts[2].trim().isEmpty()) {
                tagName1 = elts[2].trim().replace(",", "").replace(" ", "_");
                for (Map.Entry<Integer, Category> entry : categories.get().entrySet()) {
                    if (entry.getValue().getLabel().equalsIgnoreCase(tagName1)) tag1 = entry.getKey();
                }
            }

            if (elts.length > 3 && !elts[3].trim().isEmpty()) {
                tagName2 = elts[3].trim().replace(",", "").replace(" ", "_");
                for (Map.Entry<Integer, Category> entry : categories.get().entrySet()) {
                    if (entry.getValue().getLabel().equalsIgnoreCase(tagName2)) tag2 = entry.getKey();
                }
            }
            
            ArrayList<Integer> tmp = new ArrayList<>();
            if (tag1 != null) tmp.add(tag1);
            if (tag2 != null) tmp.add(tag2);
            if (!tmp.isEmpty()) DOCS.put(id, tmp);
            
        }
        
    }
    
    private static void processEvaluation(String documentId, Categories categories, TreeMap<Double, ArrayList<Category>> evaluation) {
        
        ArrayList<Integer> tags = DOCS.get(documentId);
        if (tags.isEmpty()) return;

        int manuallyAssignedTag = tags.get(0);
        CatStat currentCatStat;
        if (STATS.containsKey(manuallyAssignedTag)) {
            currentCatStat =  STATS.get(manuallyAssignedTag);
        } else {
            Category category = categories.get().get(manuallyAssignedTag);
            currentCatStat = new CatStat(category);
            STATS.put(manuallyAssignedTag, currentCatStat);
        }
        
        for (Map.Entry<Double, ArrayList<Category>> entry : evaluation.entrySet()) {
            
            if (entry.getKey() < 0.5) return;
            
            int guessedTag = entry.getValue().get(0).getIndex();
            if (guessedTag > 0) {
                currentCatStat.addCocategory(entry.getValue().get(0), entry.getKey());
                break;
            }
            
        }
        
    }
    
    private static void doStats() {

        for (Map.Entry<Integer, CatStat> outer : STATS.entrySet()) {
            
            CatStat catStat = outer.getValue();
            int total = catStat.getTotal();
            
            System.out.println(catStat.getindex() + " " + catStat.getLabel());
            for (Map.Entry<Integer, CoCat> inner : catStat.getTopCocategories(3).entrySet()) {
                CoCat coCat = inner.getValue();
                System.out.println("\t" + coCat.getCategory().getIndex() + "\t" + coCat.getCategory().getLabel());
                double percentage = 100 * coCat.getFoo() / total;
                System.out.println("\t\t" + coCat.getFoo() + " (" + MathToolbox.roundDouble(percentage, 2) + "%)");
                System.out.println("\t\tAverage score: " + coCat.getAvgScore());
                System.out.println("\t\tMin score: " + coCat.getMinScore());
                System.out.println("\t\tMax score: " + coCat.getMaxScore());
            }

            System.out.println();
            
        }
        
    }
    
}
