package wordfilter;

import com.itcag.util.io.TextFileReader;

import java.io.File;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class TokenCounter {

    private final static HashMap<String, Integer> LEXICON = new HashMap<>();

    public static void main(String[] args) throws Exception {
        
        readFolder("/home/nahum/Desktop/hebrew/news/clean/");
        
        TreeMap<Integer, TreeSet<String>> invertedIndex = invertedIndex();
        
        for (Map.Entry<Integer, TreeSet<String>> entry : invertedIndex.entrySet()) {
            
            if (entry.getKey() < 100) break;
            
            for (String token : entry.getValue()) {
                System.out.println(entry.getKey() + "\t" + token);
            }
        
        }
        
    }

    private static void readFolder(String path) throws Exception {
        
        File folder = new File(path);
        for (File file : folder.listFiles()) {
            if (!file.isFile()) continue;
            System.out.println("Reading: " + file.getName());
            process(readFile(file.getAbsolutePath()));
        }
        
    }
    
    private static ArrayList<String> readFile(String path) throws Exception {
        return TextFileReader.read(path);
    }
    
    private static void process(ArrayList<String> lines) throws Exception {
        
        for (String line : lines) {
            
            String[] words = line.split(" ");
            for (String word : words) {

                while (word.length() > 0) {
                    char c = word.charAt(0);
                    if (Character.isAlphabetic(c)) {
                        break;
                    } else {
                        word = word.substring(1);
                    }
                }

                if (word.isEmpty()) continue;

                while (word.length() > 0) {
                    char c = word.charAt(word.length() - 1);
                    if (Character.isAlphabetic(c)) {
                        break;
                    } else {
                        word = word.substring(0, word.length() - 1);
                    }
                }

                if (word.isEmpty()) continue;

                if (Character.UnicodeScript.of(word.charAt(0)) != Character.UnicodeScript.HEBREW) {
                    continue;
                }
                
                if (LEXICON.containsKey(word)) {
                    LEXICON.put(word, LEXICON.get(word) + 1);
                } else {
                    LEXICON.put(word, 1);
                }

            }
        
        }

    }
    
    private static TreeMap<Integer, TreeSet<String>> invertedIndex() throws Exception {
        
        TreeMap<Integer, TreeSet<String>> retVal = new TreeMap<>(Collections.reverseOrder());
        
        int count = 0;
        
        for (Map.Entry<String, Integer> entry : LEXICON.entrySet()) {
            
            String token = entry.getKey();
            int foo = entry.getValue();
            
            if (foo < 3) continue;
            
            count++;
            
            if (retVal.containsKey(foo)) {
                retVal.get(foo).add(token);
            } else {
                retVal.put(foo, new TreeSet<>(Arrays.asList(token)));
            }
            
        }
        
        System.out.println("Total: " + count);
        
        return retVal;
        
    }
    
}
