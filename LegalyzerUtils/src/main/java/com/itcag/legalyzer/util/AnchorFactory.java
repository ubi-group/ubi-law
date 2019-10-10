package com.itcag.legalyzer.util;

import com.itcag.dl.eval.Tester;
import com.itcag.dlutil.Categories;
import com.itcag.dlutil.eval.SigmoidResult;
import com.itcag.dlutil.lang.Category;
import com.itcag.dlutil.lang.Document;
import com.itcag.dlutil.lang.Paragraph;
import com.itcag.dlutil.parse.CourtRulingParser;
import com.itcag.util.MathToolbox;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;

import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer;
import org.deeplearning4j.models.embeddings.wordvectors.WordVectors;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

/**
 * Analyzes associated categories in a manually tagged corpus of documents, and generates anchors.
 * Anchor is a category that was identified as the top scoring category in a document
 * that was manually tagged by a different category.
 */
public class AnchorFactory {

    /**
     * Key = document ID,
     * Value = list of category indices.
     */
    private final static HashMap<String, ArrayList<Integer>> DOCS = new HashMap<>();
    
    /**
     * Key = category index,
     * Value = tag (category that was manually assigned to a document).
     */
    private final static HashMap<Integer, Tag> TAGS = new HashMap<>();

    public static void main(String[] args) throws Exception {
        
        /**
         * A text file containing the list of all documents in a corpus, and their
         * associated tags.
         * The expected format:
         *      document ID (used as the file name with the extension "txt"),
         *      original URL from which the document was retrieved (not used by this class)
         *      first category assigned to the document,
         *      second category assigned to the document (optional).
         * The elements must be all in a single line delimited by tab character.
         */
        String corpusIndexPath = "/home/nahum/Desktop/legaltech/verdicts";

        /**
         * Path to the folder containing the corpus documents.
         */
        String corpusFolder = "/home/nahum/Desktop/hebrew/high court rulings/";
        
        /**
         * Path to the file in which the anchors and their corresponding tags are recorded.
         * Format:
         *      anchor category index
         *      tag weight
         *      tag category index
         * All elements in a single line, delimited by tab characters.
         */
        String anchorFilePath = "/home/nahum/Desktop/legaltech/application/anchors";
        
        /**
         * A text file containing all categories.
         * The expected format:
         *      category index (consecutive numbers starting with 0),
         *      category name (no empty spaces).
         * The elements must be separated by a comma character,
         * Each category must be in a separate line.
         * No empty spaces are allowed  in the entire file.
         */
        Categories categories = new Categories("/home/nahum/Desktop/legaltech/experiments/categories.txt");
        
        loadDocumentIndex(corpusIndexPath, categories);
        
        WordVectors wordVectors = WordVectorSerializer.readWord2VecModel(new File(Config.WORD_2_VEC_PATH));
        MultiLayerNetwork model = MultiLayerNetwork.load(new File(Config.MODEL_PATH), true);
        
        TokenizerFactory tokenizerFactory = new DefaultTokenizerFactory();
        tokenizerFactory.setTokenPreProcessor(new CommonPreprocessor());

        Tester tester = new Tester(wordVectors, model, tokenizerFactory);

        for (Map.Entry<String, ArrayList<Integer>> entry : DOCS.entrySet()) {
            
            try {
                
                String fileName = entry.getKey() + ".txt";
                ArrayList<String> lines = TextFileReader.read(corpusFolder + fileName);

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
      
        HashMap<Integer, Anchor> anchors = getAnchors();
        recordAnchors(anchors, anchorFilePath);
        doStats();
        
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
    
    /**
     * Iterates over the evaluation received from the neural net,
     * in order to identify the highest scoring evaluation, and record it.
     * If this evaluation is different than the manually assigned category,
     * it will become a anchor.
     * @param documentId String holding the ID of a document used to retrieve the manually assigned categories.
     * @param categories Instance of the class Categories that holds all categories used in evaluation.
     * @param evaluation A map that holds all categories - each with the corresponding evaluation score.
     */
    private static void processEvaluation(String documentId, Categories categories, TreeMap<Double, ArrayList<Category>> evaluation) {
        
        ArrayList<Integer> tags = DOCS.get(documentId);
        if (tags.isEmpty()) return;

        /**
         * The first manually assigned category is considered to represent the semantics of the document.
         */
        int tagIndex = tags.get(0);
        
        /**
         * Retrieve an existing tag, or create a new one:
         * anchors are added to a tag as the documents are processed.
         */
        Tag tagCategory;
        if (TAGS.containsKey(tagIndex)) {
            tagCategory =  TAGS.get(tagIndex);
        } else {
            Category category = categories.get().get(tagIndex);
            tagCategory = new Tag(category);
            TAGS.put(tagIndex, tagCategory);
        }

        /**
         * Identify the top category in the evaluation.
         */
        for (Map.Entry<Double, ArrayList<Category>> entry : evaluation.entrySet()) {
            
            /**
             * Ignore all evaluations with the score below the threshold.
             * In the case that there is no evaluation with the score above the threshold,
             * the document is considered to be generic.
             */
            if (entry.getKey() < Config.SCORE_THRESHOLD) return;
            
            /**
             * If the first evaluation is 0 generic, ignore it,
             * and continue iterating to select the next.
             */
            if (entry.getValue().get(0).getIndex() > 0) {
                tagCategory.addAnchor(entry.getValue().get(0), entry.getKey());
                break;
            }
            
        }
        
    }

    /**
     * Transforms the highest scoring categories into a map
     * that maps the manually assigned categories (tags)
     * into the highest scoring categories (anchors).
     * @return A map that holds all anchors: the highest scoring categories that were not manually assigned.
     */
    private static HashMap<Integer, Anchor> getAnchors() {
        
        HashMap<Integer, Anchor> retVal = new HashMap<>();
        
        for (Map.Entry<Integer, Tag> outer : TAGS.entrySet()) {
            
            Tag tag = outer.getValue();
            int total = tag.getTotal();
            
            for (Map.Entry<Integer, TopCategory> inner : tag.getAnchors(Config.MAXIMUM_ANCHORS, Config.EXCLUDE_TAG_FROM_ANCHORS).entrySet()) {
                
                TopCategory topCategory = inner.getValue();
                
                if (retVal.containsKey(topCategory.getCategory().getIndex())) {
                    Anchor anchor = retVal.get(topCategory.getCategory().getIndex());
                    double percentage = (double) topCategory.getFoo() / total;
                    anchor.addTag(tag.getCategory(), percentage);
                } else {
                    Anchor anchor = new Anchor(topCategory.getCategory());
                    double percentage = (double) topCategory.getFoo() / total;
                    anchor.addTag(tag.getCategory(), percentage);
                    retVal.put(topCategory.getCategory().getIndex(), anchor);
                }
                
            }

        }
        
        return retVal;
        
    }
    
    private static void recordAnchors(HashMap<Integer, Anchor> anchors, String filePath) throws Exception {
        
        TextFileWriter writer = new TextFileWriter(filePath);
        
        for (Map.Entry<Integer, Anchor> outer : anchors.entrySet()) {
            
            Anchor anchor = outer.getValue();
            for (Map.Entry<Double, Category> inner : anchor.getTags().entrySet()) {
                /**
                 * Format:
                 *      anchor category index
                 *      tag weight
                 *      tag category index
                 * All elements in a single line, delimited by tab characters.
                 */
                writer.write(Integer.toString(outer.getKey()) + "\t" + Double.toString(inner.getKey()) + "\t" + Integer.toString(inner.getValue().getIndex()));
            }
            
        }
        
        writer.close();
        
    }
    
    private static void doStats() {

        for (Map.Entry<Integer, Tag> outer : TAGS.entrySet()) {
            
            Tag tag = outer.getValue();
            int total = tag.getTotal();
            
            System.out.println(tag.getindex() + " " + tag.getLabel());
            for (Map.Entry<Integer, TopCategory> inner : tag.getAnchors(Config.MAXIMUM_ANCHORS, Config.EXCLUDE_TAG_FROM_ANCHORS).entrySet()) {
                TopCategory topCategory = inner.getValue();
                System.out.println("\t" + topCategory.getCategory().getIndex() + "\t" + topCategory.getCategory().getLabel());
                double percentage = 100 * topCategory.getFoo() / total;
                System.out.println("\t\t" + topCategory.getFoo() + " (" + MathToolbox.roundDouble(percentage, 2) + "%)");
                System.out.println("\t\tAverage score: " + topCategory.getAvgScore());
                System.out.println("\t\tMin score: " + topCategory.getMinScore());
                System.out.println("\t\tMax score: " + topCategory.getMaxScore());
            }

            System.out.println();
            
        }
        
    }
    
}
