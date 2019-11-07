package com.itcag.dl.eval;

import com.itcag.legalyzer.util.cat.Categories;
import com.itcag.legalyzer.util.eval.Result;
import com.itcag.legalyzer.util.eval.SigmoidResult;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.dl.Config;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.util.Printer;
import com.itcag.util.io.TextFileReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.indexing.INDArrayIndex;
import org.nd4j.linalg.indexing.NDArrayIndex;

public class Tester {

    public static void main(String args[]) throws Exception {

        /**
         * Example how to test a document.
         */
        
        String documentFilePath = "/home/nahum/Desktop/hebrew/high court rulings/1108295.txt";
        String categoryFilePath = "/home/nahum/Desktop/legaltech/experiments/categories.txt";
        
        ArrayList<String> lines = TextFileReader.read(documentFilePath);
        
        Properties config = new Properties();
        config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "300");
        config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        Document document = new Document(lines, new HCRulingParser(config));

        Tester tester = new Tester();
        
        /**
         * Example of testing at the sentence level.
         * All categories are retrieved and printed.
         * No inference is applied.
         */
        tester.testSentences(document);
        
        Categories categories = new Categories(categoryFilePath);
        
        for (Paragraph paragraph : document.getParagraphs()) {
            Printer.print(paragraph.getText());
            LinkedHashMap<Integer, Category> evaluation = paragraph.getEvaluation(categories.get());
            for (Map.Entry<Integer, Category> entry : evaluation.entrySet()) {
                Printer.print("\t" + entry.getValue().toString());
            }
        }

    }

    private final WordVectors wordVectors;
    private final MultiLayerNetwork model;
    
    private final TokenizerFactory tokenizerFactory;

    private final Categories categories;
    
    public Tester() throws Exception {

        this.wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
        this.model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);
        
        this.tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        this.categories = new Categories(Config.CATEGORIES_PATH);
        
    }
    
    public void testParagraphs(Document document) throws Exception {
        for (Paragraph paragraph : document.getParagraphs()) {
            paragraph.setResult(new SigmoidResult(categories.get(), 0.00));
            test(paragraph.getText(), paragraph.getResult());
        }
    }
    
    public void testSentences(Document document) throws Exception {
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {
                sentence.setResult(new SigmoidResult(categories.get(), 0.00));
                test(sentence.getText(), sentence.getResult());
            }
        }
    }
    
    public void test(String input, Result result) throws Exception {
        
        if (input == null) throw new IllegalArgumentException("Input is null.");
        input = input.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("Input is empty.");
        
        DataSet testData = prepareTestData(input, result.getCategories().size());
        
        INDArray features = testData.getFeatures();
        
        INDArray predicted = model.output(features, false);

        for (int i = 0 ; i < predicted.size(0); i++) {
            INDArray row = predicted.slice(i);
            for (int j = 0; j < predicted.size(1); j++) {
                INDArray column = row.slice(j);
                /**
                 * We are using RNN were the probabilities
                 * are update for every word in the sentence.
                 * Therefore, only the score of last word
                 * really matters - it depends on all previous
                 * words.
                 */
                Double score = column.getDouble(column.length() - 1);
                result.setCategoryScore(j, score);
            }
        }

    }
    
    private DataSet prepareTestData(String input, int numCategories) throws Exception {
    
        List<String> tokens = new ArrayList<>();
        for (String token : tokenizerFactory.create(input).getTokens()) {
            if (wordVectors.hasWord(token)) tokens.add(token);
        }
        if (tokens.isEmpty()) throw new IllegalArgumentException("Input consists of unrecognized words.");
        
        INDArray features = Nd4j.create(1, wordVectors.lookupTable().layerSize(), tokens.size());
        INDArray labels = Nd4j.create(1, numCategories, tokens.size());
        INDArray featuresMask = Nd4j.zeros(1, tokens.size());
        INDArray labelsMask = Nd4j.zeros(1, tokens.size());

        int[] tmp = new int[2];
        tmp[0] = 0;
        for (int j = 0; j < tokens.size() && j < tokens.size(); j++) {
            String token = tokens.get(j);
            INDArray vector = wordVectors.getWordVectorMatrix(token);
            features.put(new INDArrayIndex[]{NDArrayIndex.point(0), NDArrayIndex.all(), NDArrayIndex.point(j)}, vector);
            tmp[1] = j;
            featuresMask.putScalar(tmp, 1.0);
        }
        labels.putScalar(new int[]{0, 0, tokens.size() - 1}, 1.0);
        labelsMask.putScalar(new int[]{0, tokens.size() - 1}, 1.0);

        DataSet retVal = new DataSet(features, labels, featuresMask, labelsMask);
        return retVal;
    
    }

    private static ArrayList<String> readFile(String filePath) throws Exception {
        
        File file = new File(filePath);
        if (!file.exists()) throw new FileNotFoundException(filePath);

        ArrayList<String> retVal = new ArrayList<>();
        
        try (InputStream input = new FileInputStream(file)) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null){
                line = line.trim();
                if (!line.isEmpty()) retVal.add(line);
                line = reader.readLine();
            }
        }
        
        return retVal;
    
    }

}
