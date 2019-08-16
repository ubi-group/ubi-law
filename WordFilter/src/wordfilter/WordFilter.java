package wordfilter;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class WordFilter {

    private final static int THRESHOLD = 50;
    private final static String SOURCE = "/home/nahum/Desktop/hebrew_news/";
    private final static String TARGET = "/home/nahum/Desktop/hebrew_news_processed/";
    
    private final static ArrayList<HashMap<String, Integer>> lexicons = new ArrayList<>();
    
    public static void main(String[] args) throws Exception {
    
        createLexicons();
        HashSet<String> trash = identifyCommon();
        removeCommon(trash);

        System.out.println("Common: " + trash.size());
        for (int i = 0; i < lexicons.size() - 1; i++) {
            System.out.println("Lexicon " + i + ": " + lexicons.get(i).size());
        }
    
    }

    private static void createLexicons() throws Exception {
        
        for (int i = 0; i < 4; i++) {
            
            HashMap<String, Integer> lexicon = new HashMap<>();
            
            String fileName = Integer.toString(i) + ".txt";
            
            List<String> lines = Files.readAllLines(Paths.get(SOURCE + fileName));
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
                    
                    if (lexicon.containsKey(word)) {
                        lexicon.put(word, lexicon.get(word) + 1);
                    } else {
                        lexicon.put(word, 1);
                    }
                    
                }
            }
            
            lexicons.add(lexicon);
            
        }
        
    }
    
    private static HashSet<String> identifyCommon() {

        HashSet<String> retVal = new HashSet<>();
        
        for (int i = 0; i < lexicons.size() - 1; i++) {
            
            HashMap<String, Integer> current = lexicons.get(i);
            
            Iterator<Map.Entry<String, Integer>> currentIterator = current.entrySet().iterator();
            while (currentIterator.hasNext()) {

                Map.Entry<String, Integer> entry = currentIterator.next();
                
                int foo = entry.getValue();
                String word = entry.getKey();
                
                if (retVal.contains(word)) continue;
                
                if (foo > THRESHOLD) {
                    
                    boolean removed = false;
                    
                    for (int j = i + 1; j < lexicons.size(); j++) {
                        
                        HashMap<String, Integer> other = lexicons.get(j);
                        
                        if (other.containsKey(word)) {
                            
                            int otherFOO = other.get(word);
                            if (otherFOO > 0.5 * foo) {
                                other.remove(word);
                                retVal.add(word);
                                removed = true;
                            }
                            
                        }
                        
                    }
                    
                    if (removed) currentIterator.remove();
                }

            }

        }
        
        return retVal;
        
    }
    
    private static void removeCommon(HashSet<String> trash) throws Exception {
        
        for (int i = 0; i < 4; i++) {
            
            String fileName = Integer.toString(i) + ".txt";
            
            StringBuilder buffer = new StringBuilder();
                
            List<String> lines = Files.readAllLines(Paths.get(SOURCE + fileName));
            for (String line : lines) {
            
                StringBuilder miniBuffer = new StringBuilder();

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
                    
                    if (!trash.contains(word)) {
                        miniBuffer.append(word).append(" ");
                    }
                    
                }
                
                
                if (miniBuffer.length() == 0) continue;
                
                if (buffer.length() > 0) buffer.append("\n");
                buffer.append(miniBuffer.toString().trim());
                
            }

            Files.write(Paths.get(TARGET + fileName), buffer.toString().getBytes());
            
        }
        
    }
    
}
