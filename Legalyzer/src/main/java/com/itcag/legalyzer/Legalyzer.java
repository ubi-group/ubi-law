package com.itcag.legalyzer;

import com.itcag.dl.eval.Tester;
import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.split.Splitter;

import java.util.ArrayList;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Legalyzer {

    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    private final Splitter splitter = new Splitter();
    
    public Legalyzer(WordVectors wordVectors, MultiLayerNetwork model, TokenizerFactory tokenizerFactory, Categories categories, Inference inference) throws Exception {
        
        this.tester = new Tester(wordVectors, model, tokenizerFactory);

        this.inference = inference;
        this.categories = categories;
        
    }
    
    public void insertRecommendations(Document document) throws Exception {
        
        Evaluator evaluator = new Evaluator(inference);

        for (Paragraph paragraph : document.getParagraphs()) {
            
            ArrayList<StringBuilder> sentenceTexts = splitter.split(new StringBuilder(paragraph.getText()));
            for (StringBuilder sentenceText : sentenceTexts) {
                Sentence sentence = new Sentence(sentenceText.toString());
                sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                paragraph.addSentence(sentence);
                tester.test(sentence.getText(), sentence.getResult());
                evaluator.run(sentence);
            }
            /*
            paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
            tester.test(paragraph.getText(), paragraph.getResult());
            evaluator.run(paragraph);
            */
        }

    }
    
}
