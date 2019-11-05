package com.itcag.legalyzer.util.extract.util;

import com.itcag.legalyzer.util.extract.LawExtractor;
import com.itcag.util.txt.TextToolbox;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * Creates various versions of the official law name.
 * It can either read the resource that contains all laws and normalize them,
 * or it can normalize a single law name.
 */
public class LawNormalizer {

    private static HashSet<String> index = new HashSet<>();
    
    public static void main(String[] args) throws Exception {
    
//        processOfficialName("חוק להארכת תוקף של תקנות שעת חירום (פיקוח על כלי שיט) [נוסח משולב], התשל\"ג-1973");
        processResource();
    
        for (String item : index) {
            System.out.println(item);
        }
        
    }
    
    private static void processResource() throws Exception {

        /**
         * Load the complete list of Israeli laws.
         */
        try (InputStream input = LawNormalizer.class.getClassLoader().getResourceAsStream("extr/laws")) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line = reader.readLine();
            while (line != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    if (line.charAt(0) == 8235) line = line.substring(1);
                    if (line.charAt(line.length() - 1) == 8236) line = line.substring(0, line.length() - 1);
                    String[] elts = line.split("\t");
                    String key = elts[0].trim();
                    String label = elts[1].trim();
                    processOfficialName(label);
                }
                line = reader.readLine();
            }

        }

    }
    
    private static void processOfficialName(String label) throws Exception {
        
        TreeSet<String> tmp = new TreeSet<>();

        String normalized = LawExtractor.normalize(label);
        tmp.add(normalized + "\t" + label);

        /**
         * Remove round brackets.
         */
        if (label.contains("(")) {
            String newKey = TextToolbox.removeParentheses(label, "(", ")");
            newKey = LawExtractor.normalize(newKey);
            tmp.add(newKey + "\t" + label);
        }

        /**
         * Remove square brackets.
         */
        if (label.contains("[")) {
            String newKey = TextToolbox.removeParentheses(label, "[", "]");
            newKey = LawExtractor.normalize(newKey);
            tmp.add(newKey + "\t" + label);
        }
    
        /**
         * Remove both round and square brackets.
         */
        if (label.contains("(") && label.contains("[")) {
            String newKey = TextToolbox.removeParentheses(label, "(", ")");
            newKey = TextToolbox.removeParentheses(newKey, "[", "]");
            newKey = LawExtractor.normalize(newKey);
            tmp.add(newKey + "\t" + label);
        }
    
        /**
         * Remove hei ha-yedia and hey from the beginning of a Hebrew year.
         */
        if (label.contains(" ה")) {
            
            /**
             * First, fix only the Hebrew year.
             */
            if (label.contains(" התש")) {
        
                for (String item : new HashSet<>(tmp)) {
                    
                    String[] elts = item.split("\t");
                    String key = elts[0].trim();
                    String newKey = key.replace(" התש", " תש");
                    tmp.add(newKey + "\t" + label);

                    /**
                     * Next, remove heys also from other words.
                     */
                    if (newKey.contains(" ה")) {

                        /**
                         * Now, all heys are stripped off.
                         */
                        String newNewKey = newKey.replace(" ה", " ");
                        tmp.add(newNewKey + "\t" + label);

                        /**
                         * But then also return the hey in the Hebrew year.
                         */
                        newNewKey = key.replace(" תש", " התש");
                        tmp.add(newNewKey + "\t" + label);

                    }

                }
            
            } else {
                
                /**
                 * In case that there is no Hebrew year,
                 * remove only the heys from other words.
                 */
                for (String item : new HashSet<>(tmp)) {
                    String[] elts = item.split("\t");
                    String key = elts[0].trim();
                    String newKey = key.replace(" ה", " ");
                    tmp.add(newKey + "\t" + label);
                }
            }
        
        }

        /**
         * Replace double yuds.
         */ 
        if (label.contains("יי")) {
            for (String item : new HashSet<>(tmp)) {
                String[] elts = item.split("\t");
                String key = elts[0].trim();
                String newKey = key.replace("יי", "י");
                tmp.add(newKey + "\t" + label);
            }
        }

        /**
         * Replace double vavs.
         */
        if (label.contains("יי")) {
            for (String item : new HashSet<>(tmp)) {
                String[] elts = item.split("\t");
                String key = elts[0].trim();
                String newKey = key.replace("וו", "ו");
                tmp.add(newKey + "\t" + label);
            }
        }

        index.addAll(tmp);

    }
    
}
