package wordfilter;

import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

public class WikiCleaner {
    
    private final static HashSet<String> NEW_LINES = new HashSet<>();

    private static final String SOURCE_FOLDER = "/home/nahum/Desktop/hebrew/wikipedia/";
    private static final String TARGET_FOLDER = "/home/nahum/Desktop/hebrew/wikipedia/clean/";
    
    private static int fileNum = 0;
    
    public static void main(String[] args) throws Exception {
        
        readFolder();
        
    }

    private static void readFolder() throws Exception {
        
        File folder = new File(SOURCE_FOLDER);
        for (File file : folder.listFiles()) {
            if (!file.isFile()) continue;
            System.out.println("Reading: " + file.getName());
            process(readFile(file.getAbsolutePath()));
        }
        
        if (!NEW_LINES.isEmpty()) recordNewLines();

    }
    
    private static ArrayList<String> readFile(String path) throws Exception {
        return TextFileReader.read(path);
    }
    
    private static void process(ArrayList<String> lines) throws Exception {
        
        for (String line : lines) {
            
            if (TextToolbox.isReallyEmpty(line)) continue;
            
            while (line.contains("\n")) line = TextToolbox.replace("line", "\n", " ");
            while (line.contains("\r")) line = TextToolbox.replace("line", "\r", " ");
            while (line.contains("  ")) line = TextToolbox.replace("line", "  ", " ");
            
            line = line.trim();
            if (line.isEmpty()) continue;
            
            if (line.contains("ויקינתונים")) continue;
            if (line.startsWith("דף זה נערך לאחרונה")) continue;
            if (line.startsWith("קצרמר")) continue;
            if (line.startsWith("חיפוש")) continue;
            if (line.startsWith("ראו ")) continue;
            if (line.startsWith("ראו:")) continue;
            if (line.startsWith("(ראו ")) continue;
            if (line.startsWith("(ראו:")) continue;
            
            if (!line.contains(" ")) continue;
            if (line.split(" ").length < 4) continue;
            
            if (!containsHebrewCharacters(line)) continue;
            if (isMishMash(line)) continue;
            if (Character.isDigit(line.charAt(0))) continue;
            
            NEW_LINES.add(line);
            
            if (NEW_LINES.size() >= 100000) recordNewLines();
            
        }
        
    }

    private static boolean containsHebrewCharacters(String line) throws Exception {
    
        int countHebrew = 0;
        int countNonHebrew = 0;
        for (char c : line.toCharArray()) {
            if (Character.UnicodeScript.of(c) == Character.UnicodeScript.HEBREW) {
                countHebrew++;
            } else {
                countNonHebrew++;
            }
        }
        
        return countHebrew > countNonHebrew;
    
    }
    
    private static boolean isMishMash(String line) {
        
        HashSet<String> ending = new HashSet<>(Arrays.asList("ך", "ם", "ן", "ף", "ץ"));
        
        for (int i = 0; i < line.toCharArray().length - 1; i++) {
            char c = line.charAt(i);
            char d = line.charAt(i + 1);
            if (ending.contains(String.valueOf(c))) {
                if (Character.UnicodeScript.of(d) == Character.UnicodeScript.HEBREW) {
                    return true;
                }
            }
        }
        
        return false;
        
    }
    
    private static void recordNewLines() throws Exception {
        
        StringBuilder buffer = new StringBuilder();
        
        for (String newLine : NEW_LINES) {
            if (buffer.length() > 0) buffer.append("\n");
            buffer.append(newLine);
        }
        
        String fileName = Integer.toString(fileNum) + ".txt";
        Files.write(Paths.get(TARGET_FOLDER + fileName), buffer.toString().getBytes());

        fileNum++;
        NEW_LINES.clear();

    }
    
}
