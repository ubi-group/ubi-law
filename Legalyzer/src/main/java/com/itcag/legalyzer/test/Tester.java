package com.itcag.legalyzer.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

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

    private final WordVectors wordVectors;
    private final MultiLayerNetwork model;
    
    private final ArrayList<String> categories = new ArrayList<>();
    
    private final TokenizerFactory tokenizerFactory;
    
    public Tester(String wordVectorPath, String modelPath, String categoriesPath) throws Exception {

        wordVectors = WordVectorSerializer.readWord2VecModel(new File(wordVectorPath));
        model = MultiLayerNetwork.load(new File(modelPath), true);
        
        tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        File file = new File(categoriesPath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                categories.add(line);
            }
            reader.close();
        }
        
    }
    
    public String test(String input) throws Exception {
        
        if (input == null) throw new IllegalArgumentException("Input is null.");
        input = input.trim();
        if (input.isEmpty()) throw new IllegalArgumentException("Input is empty.");
        
        DataSet testData = prepareTestData(input);
        
        INDArray fet = testData.getFeatures();
        
        INDArray predicted = model.output(fet, false);
        long[] arrsiz = predicted.shape();

        double max = 0;
        int pos = 0;
        for (int i = 0; i < arrsiz[1]; i++) {
            if (max < (double) predicted.slice(0).slice(i).sumNumber()) {
                max = (double) predicted.slice(0).slice(i).sumNumber();
                pos = i;
            }
        }

        return categories.get(pos).split(",")[1];
        
    }
    
    private DataSet prepareTestData(String input) throws Exception {
    
        List<String> tokens = new ArrayList<>();
        for (String token : tokenizerFactory.create(input).getTokens()) {
            if (wordVectors.hasWord(token)) tokens.add(token);
        }
        if (tokens.isEmpty()) throw new IllegalArgumentException("Input consists of unrecognized words.");
        
        INDArray features = Nd4j.create(1, wordVectors.lookupTable().layerSize(), tokens.size());
        INDArray labels = Nd4j.create(1, 4, tokens.size());
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

    public static void main(String args[]) throws Exception {

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        URL url = classLoader.getResource("NewsData");
        String resourcesPath = url.getPath() + File.separator;

        String wordVectorPath = "/home/nahum/code/ubi-law/hebrew_news/wordvec.txt";
        String modelPath = "/home/nahum/code/ubi-law/hebrew_news/NewsModel.net";
        String categoriesPath = "/home/nahum/code/ubi-law/hebrew_news/WordFilteredNews/categories.txt";
        
        String input = "פיגוע הדריסה: המחבל היה כלוא בישראל בגין טרור; דרס ברכב גנוב";
//        String input = "אלפי פלסטינים מפגינים בגבול רצועת עזה";
//        String input = "בנו של הזמר חיים אוליאל נמצא ללא רוח חיים: \"מוזיקאי מדהים\"";
//        String input = "הדוגמנית החמה: מישל טימושנקו";
//        String input = "ריאל מדריד: אדן הזאר יחמיץ את משחק הפתיחה מול סלטה ויגו בשל פציעה";
//        String input = "העברות בעולם - 16.8: \"פול פוגבה רוצה לעזוב, הוא לא יכול לעשות הכל במנצ'סטר יונייטד\"";
//        String input = "האם בקרוב נוכל להיפטר מהמטען? סמסונג מתכננת סמארטפון עם סוללה מהפכנית";
//        String input = "שריל את שומעת? כנראה שכן: פייסבוק האזינה לשיחות מסנג'ר";
        
        Tester tester = new Tester(wordVectorPath, modelPath, categoriesPath);
        String output = tester.test(input);
        System.out.println(System.lineSeparator() + "CATEGORY: " + output + System.lineSeparator());
        
    }

}
