package com.itcag.legalyzer;

import com.itcag.dlutil.lang.Recommendation;
import com.itcag.dl.eval.Tester;
import com.itcag.dlutil.Categories;
import com.itcag.dlutil.eval.SigmoidResult;
import com.itcag.dlutil.lang.Document;
import com.itcag.dlutil.lang.Paragraph;
import com.itcag.dlutil.parse.CourtRulingParser;

import java.util.ArrayList;

import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

public class Legalyzer {

    private final Tester tester;
    
    private final Categories categories;
    
    private final Inference inference;
    
    public Legalyzer(WordVectors wordVectors, MultiLayerNetwork model, TokenizerFactory tokenizerFactory, Categories categories, Inference inference) throws Exception {
        
        this.tester = new Tester(wordVectors, model, tokenizerFactory);

        this.inference = inference;
        this.categories = categories;
        
    }
    
    public void insertRecommendations(Document document) throws Exception {
        
        document.selectParagraphs(new CourtRulingParser(6, 300));

        Evaluator evaluator = new Evaluator(inference);

        for (Paragraph paragraph : document.getParagraphs()) {
            paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
            tester.test(paragraph.getText(), paragraph.getResult());
            evaluator.run(paragraph);
        }

    }
    
}
