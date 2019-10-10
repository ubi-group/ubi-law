package com.itcag.legalyzer;

import com.itcag.dlutil.lang.Recommendation;
import com.itcag.dlutil.Categories;
import com.itcag.dlutil.lang.Category;
import com.itcag.dlutil.lang.Document;
import com.itcag.dlutil.lang.Paragraph;
import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Tester {
    
    /**
     * Key = document ID,
     * Value = list of category indices.
     */
    private final static HashMap<String, ArrayList<Integer>> DOCS = new HashMap<>();

    public static void main(String[] args) throws Exception {

        String corpusIndexPath = "/home/nahum/Desktop/legaltech/verdicts";
        String corpusFolder = "/home/nahum/Desktop/hebrew/high court rulings/";

        String word2vecFilePath = "/home/nahum/Desktop/legaltech/experiments/wordvec.txt";
        String modelPath = "/home/nahum/Desktop/legaltech/experiments/Model.net";
        
        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        String anchorFilePath = "/home/nahum/Desktop/legaltech/application/anchors";
        
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(word2vecFilePath));
        MultiLayerNetwork model = MultiLayerNetwork.load(new File(modelPath), true);
        
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        Categories categories = new Categories(categoryFilePath);

        Anchors anchors = new Anchors(categories, anchorFilePath);
        Siblings siblings = new Siblings(categories);
        
        Inference inference = new Inference(anchors, siblings);
        
        Legalyzer legalyzer = new Legalyzer(wordVectors, model, tokenizerFactory, categories, inference);

        loadDocumentIndex(corpusIndexPath, categories);
        
        int count = 0;
        
        for (Map.Entry<String, ArrayList<Integer>> entry : DOCS.entrySet()) {
            try {
                
                String fileName = entry.getKey() + ".txt";
                processDocument(corpusFolder + fileName, legalyzer);
                
                count++;
                if (count > 1000) break;
            
            } catch (Exception ex) {
                //DO NOTHING!
            }
        }
        
    }

    private static void loadDocumentIndex(String documentIndexFilePath, Categories categories) throws Exception {
        
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
    
    private static void processDocument(String filePath, Legalyzer legalyzer) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        Document document = new Document(lines);
        
        legalyzer.insertRecommendations(document);

        for (Paragraph paragraph : document.getParagraphs()) {
            
//            if (paragraph.getRecommendations().isEmpty()) continue;
            System.out.println(paragraph.toString());
            System.out.println();
        
        }
        System.out.println("----------------------------------------------------------------");
        System.out.println();
        
    }
    
}
