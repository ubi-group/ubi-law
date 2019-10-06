package com.itcag.scraper;

import com.itcag.util.io.ExcelFileReader;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.io.TextFileWriter;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Sandbox {

    private TextFileWriter writer;
    
    public Sandbox(String filePath) throws Exception {
        this.writer = new TextFileWriter(filePath);
    }
    
    public static void main(String[] args) throws Exception {
//        parseExcel();
//        download();

        /*
        Sandbox sandbox = new Sandbox("/home/nahum/Desktop/rawdata.txt");
        sandbox.processVerdicts();
        */

//        removeDuplicates();
//        countSentencesPerTag();
        generateTrainingData();
        
    }

    private ArrayList<String> preselectPragraphs(ArrayList<String> lines) {

        ArrayList<String> tmp = new ArrayList<>();
        
        for (String line : lines) {
            
            if (line.startsWith("ניתן היום")) break;
            if (line.startsWith("ניתנה היום")) break;
            if (line.startsWith("דיון והכרעה")) break;
            if (line.startsWith("רקע והליכים")) break;
            if (line.startsWith("פתח דבר")) break;
            if (line.startsWith("סוף דבר")) break;
            if (line.startsWith("רקע עובדתי")) break;
            if (line.startsWith("כבוד השופט")) break;
            if (line.startsWith("השופט")) break;
            if (line.startsWith("ש ו פ ט")) break;
//            if (line.startsWith("")) break;

            if (line.equals("רקע")) break;
            
            tmp.add(line);
        
        }
        
        if (tmp.size() <= 6) return tmp;
        
        ArrayList<String> retVal = new ArrayList<>(tmp.subList(0, 2));
        retVal.addAll(tmp.subList(tmp.size() - 3, tmp.size() - 1));
        
        return retVal;
        
    }
    
    private String removeBullet(String sentence) {

        int test = identifyBullet(sentence, ".");
        if (test > 0) return sentence.substring(test).trim();

        test = identifyBullet(sentence, ")");
        if (test > 0) return sentence.substring(test).trim();

        return sentence;
        
    }
    
    private int identifyBullet(String sentence, String delimiter) {
        
        int retVal = sentence.indexOf(delimiter);
        if (retVal < 0) return -1;
        
        if (retVal > 4) {
            /**
             * Too long to be a bullet.
             */
            return -1;
        }
        
        int space = sentence.indexOf(" ");
        if (space > -1 && space < retVal) {
            /**
             * This cannot be a bullet.
             */
            return -1;
        }
        
        return retVal + 1;
        
    }
    
    private static void parseExcel() throws Exception {
        
        TreeMap<String, Integer> issues = new TreeMap<>();
        TreeMap<String, Integer> lissues = new TreeMap<>();

        int rowCount = -1;
        
        ExcelFileReader reader = new ExcelFileReader("/home/nahum/Desktop/iscd_cases_2018-12-05.xlsx", "cases");
        for (String line : reader.getData()) {
            
            rowCount++;
            if (rowCount == 0) continue;
            
            int columnCount = 0;
            StringBuilder tmp = new StringBuilder();
            
            String[] elts = line.split("\t");
            for (String elt : elts) {
                
                elt = elt.trim();
                
                switch (columnCount) {
                    case 1:
                    case 4:
                        if (elt.isEmpty()) break;
                        tmp.append(elt).append("\t");
                        break;
                    case 36:
                        if (elt.isEmpty()) break;
                        if (lissues.containsKey(elt)) {
                            lissues.put(elt, lissues.get(elt) + 1);
                        } else {
                            lissues.put(elt, 1);
                        }
                        break;
                    case 39:
                        if (elt.isEmpty()) break;
                        /* TO DO */
                        break;
                    case 41:
                    case 42:
                        if (elt.isEmpty()) break;
                        tmp.append(elt).append("\t");
                        if (issues.containsKey(elt)) {
                            issues.put(elt, issues.get(elt) + 1);
                        } else {
                            issues.put(elt, 1);
                        }
                        break;
                }
                
                columnCount++;
            
            }

            System.out.println(tmp.toString());

        }
        
        System.out.println("----------------------------------------------");
        
        for (Map.Entry<String, Integer> entry : issues.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
        
        System.out.println("----------------------------------------------");
        
        for (Map.Entry<String, Integer> entry : lissues.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
        
        System.out.println("----------------------------------------------");
        
        TreeMap<Integer, String> inverted = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> entry : issues.entrySet()) {
            inverted.put(entry.getValue(), entry.getKey());
        }
        for (Map.Entry<Integer, String> entry : inverted.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
        
        
    }
    
    private static void insertElements(Document doc, String selector, StringBuilder retVal) throws Exception {
        Elements elts = doc.select(selector);
        for (Element elt : elts) {
            String text = elt.text();
            if (TextToolbox.isReallyEmpty(text)) continue;
            text = text.trim();
            retVal.append(text).append("\n");
        }
    }

    private static void removeDuplicates() throws Exception {
        
        /**
         * Sentence is the key, and a set of tags is the value.
         */
        HashMap<String, HashSet<String>> control = new HashMap<>();
        
        ArrayList<String> lines = TextFileReader.read("/home/nahum/Desktop/rawdata.txt");
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            String id = elts[0].trim();
            String tag = elts[1].trim();
            String sentence = elts[2].trim();
            
            if (control.containsKey(sentence)) {
                control.get(sentence).add(tag);
            } else {
                control.put(sentence, new HashSet<>(Arrays.asList(tag)));
            }
            
        }
    
        /**
         * Sentences that occur in different documents and with different tags.
         */
        HashSet<String> rejects = new HashSet<>();
        for (Map.Entry<String, HashSet<String>> entry : control.entrySet()) {
            if (entry.getValue().size() > 2 && entry.getKey().length() < 75) {
                rejects.add(entry.getKey());
                System.out.println(entry.getValue().size() + "\t" + entry.getKey());
            }
        }
        
        TextFileWriter writer = new TextFileWriter("/home/nahum/Desktop/new_rawdata.txt");
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            String id = elts[0].trim();
            String tag = elts[1].trim();
            String sentence = elts[2].trim();

            if (!rejects.contains(sentence)) {
                writer.write(id + "\t" + tag + "\t" + sentence);
            }
            
        }
        writer.close();
    
    }

    private static void countSentencesPerTag() throws Exception {

        HashMap<String, Integer> index = new HashMap<>();
        
        ArrayList<String> lines = TextFileReader.read("/home/nahum/Desktop/rawdata.txt");
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            String id = elts[0].trim();
            String tag = elts[1].trim();
            String sentence = elts[2].trim();
            
            if (index.containsKey(tag)) {
                index.put(tag, index.get(tag) + 1);
            } else {
                index.put(tag, 1);
            }

        }
            
        TreeMap<Integer, TreeSet<String>> inverted = new TreeMap<>(Collections.reverseOrder());
        for (Map.Entry<String, Integer> entry : index.entrySet()) {
            if (inverted.containsKey(entry.getValue())) {
                inverted.get(entry.getValue()).add(entry.getKey());
            } else {
                inverted.put(entry.getValue(), new TreeSet<>(Arrays.asList(entry.getKey())));
            }
        }
        
        for (Map.Entry<Integer, TreeSet<String>> entry : inverted.entrySet()) {
            for (String tag : entry.getValue()) {
                System.out.println(entry.getKey() + "\t" + tag);
            }
        }
        
    }

    private static void generateTrainingData() throws Exception {

        String category = "Civil - Land";
        
        TextFileWriter writer = new TextFileWriter("/home/nahum/Desktop/legaltech/experiments/original/" + category);
        ArrayList<String> lines = TextFileReader.read("/home/nahum/Desktop/rawdata.txt");
        for (String line : lines) {
            
            String[] elts = line.split("\t");
            String id = elts[0].trim();
            String tag = elts[1].trim();
            String sentence = elts[2].trim();

            if (category.equals(tag)) {
                writer.write(sentence);
            }

        }
        writer.close();
        
    }
    
}
