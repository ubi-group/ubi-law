package com.itcag.legalyzer;

import com.itcag.dlutil.Categories;
import com.itcag.dlutil.lang.Document;
import com.itcag.dlutil.lang.Paragraph;
import com.itcag.dlutil.lang.Sentence;
import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Tester {
    
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

        processFolder(corpusIndexPath, corpusFolder, categories, legalyzer);

//        processDocument("", legalyzer);
        
    }

    private static void processFolder(String corpusIndexPath, String corpusFolder, Categories categories, Legalyzer legalyzer) throws Exception {
        
        DocumentIndex documentIndex = new DocumentIndex(corpusIndexPath, categories);
        
        int count = 0;
        
        for (Map.Entry<String, ArrayList<Integer>> entry : documentIndex.get().entrySet()) {
            try {
                
                String fileName = entry.getKey() + ".txt";
System.out.println("----------------------------------------------------------------");
System.out.println();
//System.out.println(fileName);
for (int index : entry.getValue()) {
    System.out.println(categories.get().get(index).toString());
}
System.out.println();

                processDocument(corpusFolder + fileName, legalyzer);
                
                count++;
//                if (count > 10) break;
            
            } catch (Exception ex) {
                //DO NOTHING!
            }
        }

    }
    
    private static void processDocument(String filePath, Legalyzer legalyzer) throws Exception {
        
        ArrayList<String> lines = TextFileReader.read(filePath);
        Document document = new Document(lines);
        
        legalyzer.insertRecommendations(document);

        for (Paragraph paragraph : document.getParagraphs()) {

            for (Sentence sentence : paragraph.getSentences()) {
                System.out.println(sentence.toString());
                System.out.println();
            }
            
        }
        System.out.println();
        
    }
    
}
