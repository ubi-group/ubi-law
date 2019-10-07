package com.itcag.scraper.court_rulings;

import com.itcag.util.Converter;
import com.itcag.util.io.TextFileReader;
import com.itcag.util.txt.TextToolbox;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import java.util.ArrayList;

public class Extractor {


    public static void main(String[] args) throws Exception {
        
        String sourceFilePath = "/home/nahum/Desktop/legaltech/verdicts";
        String sourceFolder = "/home/nahum/Desktop/hebrew/high court rulings/";
        String targetFolder = "/home/nahum/Desktop/legaltech/experiments/original/";
        
        Extractor extractor = new Extractor(sourceFilePath, sourceFolder, targetFolder);
        extractor.extract();
        
    }
    
    private final String indexPath;
    private final String sourceFolder;
    private final String targetFolder;
    
    public Extractor(String indexPath, String sourceFolder, String targetFolder) {
        this.indexPath = indexPath;
        this.sourceFolder = sourceFolder;
        this.targetFolder = targetFolder;
    }
    
    public void extract() throws Exception {
        
        int count = 0;
        
        ArrayList<String> lines = TextFileReader.read(this.indexPath);
        for (String line : lines) {
            
            String[] elts = line.split("\t");

            String id = elts[0].trim();
            
            String tag = selectTags(elts);
            if (tag == null) continue;

            parseVerdict(id, tag);

            Thread.sleep(1000);
            
            count++;
            System.out.println(count);
            if (count % 100 == 0) {
                Thread.sleep(10000);
            }
            
        }

    }

    private String selectTags(String[] elts) {

        String tag1 = elts[2].trim();
        String tag2 = null;
        if (elts.length > 3) tag2 = elts[3].trim();
        
        if (tag1.toLowerCase().endsWith("- other")) tag1 = null;
        if (tag2 != null && tag2.toLowerCase().endsWith("- other")) tag2 = null;
        
        if (tag1 != null) return tag1;
        if (tag2 != null) return tag2;
        
        return null;
        
    }
    
    private void parseVerdict(String id, String tag) throws Exception {
        
        String sourceFilePath = this.sourceFolder + id + ".txt";

        ArrayList<String> lines = TextFileReader.read(sourceFilePath);

        StringBuilder text = new StringBuilder();
        boolean trigger = false;
        int lastLine = 0;
        
        for (int i = 0; i < lines.size(); i++) {

            String line = lines.get(i);
            
            line = stripOffBullet(line).trim();

            if (!trigger) {
                if (isTrigger(line)) {
                    trigger = true;
                    lastLine = i + 6;
                    continue;
                } else {
                    continue;
                }
            }
            
            if (isStopper(line)) break;
            
            if (!isValid(line)) {
                lastLine++;
                continue;
            }
            
            line = removeQuotes(line);
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            line = TextToolbox.removeParentheses(line.trim(), "(", ")");
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            line = TextToolbox.removeParentheses(line.trim(), "[", "]");
            if (TextToolbox.isReallyEmpty(line)) continue;
            if (line.length() < 2) continue;

            text.append(line).append(" ");

            if (text.length() > 300) break;
            
            if (i == lastLine) break;
            
        }

        record(tag, text);
        
    }

    private String stripOffBullet(String line) {

        String bullet = ". ";
        
        int end = line.indexOf(bullet);
        if (end == -1) {
            bullet = ") ";
            end = line.indexOf(bullet);
            if (end == -1) return line;
        }
        
        if (end > 4) return line;
        
        String test = line.substring(0, end);
        if (Converter.convertStringToInteger(test) != null) {
            line = line.substring(end + 2);
            return line;
        }
        
        if (end > 3) return line;
        
        for (char c : test.toCharArray()) {
            if (!Character.isLetter(c)) return line;
        }
        
        line = line.substring(end + 2);
        return line;
        
    }
    
    private boolean isTrigger(String line) {
        
        if (line.startsWith("פסק-דין")) return true;
        if (line.startsWith("פסק - דין")) return true;
        if (line.startsWith("פסק- דין")) return true;
        if (line.startsWith("פסק דין")) return true;

        return false;

    } 
    
    private boolean isValid(String line) {

        if (line.startsWith("פתח דבר")) return false;
        if (line.startsWith("תוכן עניינים")) return false;
        if (line.startsWith("השופט")) return false;
        if (line.startsWith("שופט")) return false;
        if (line.startsWith("המשנה ל")) return false;
        if (line.startsWith("משנה ל")) return false;
        if (line.startsWith("הנשיא")) return false;
        if (line.startsWith("נשיא")) return false;

        return true;

    } 
    
    private String removeQuotes(String sentence) {
        
        StringBuilder retVal = new StringBuilder();
        
        for (int i = 0; i < sentence.length(); i++) {
            
            Character pre = null;
            if (i > 0) pre = sentence.charAt(i - 1);
            
            Character post = null;
            if (i < sentence.length() - 2) post = sentence.charAt(i + 1);
            
            String curr = Character.toString(sentence.charAt(i));
            if (curr.equals("\"")) {
                if (pre == null) {
                    /* DO NOTHING */
                } else if (post == null) {
                    /* DO NOTHING */
                } else if (Character.isLetter(pre) && Character.isLetter(post)) {
                    retVal.append(curr);
                } else {
                    /* DO NOTHING */
                }
            } else {
                retVal.append(curr);
            }
        }
        
        return retVal.toString();
    }
    
    private boolean isStopper(String line) {
        
        if (line.startsWith("ניתן ביום")) return true;
        if (line.startsWith("ניתן היום")) return true;
        if (line.startsWith("ניתן והודע ביום")) return true;
        if (line.startsWith("ניתן ושומע בתאריך")) return true;
        if (line.startsWith("ניתן ותוקן היום")) return true;
        if (line.startsWith("ניתנה ביום")) return true;
        if (line.startsWith("ניתנה היום")) return true;
        if (line.startsWith("תוקן ביום")) return true;
        if (line.startsWith("תוקן היום")) return true;
        if (line.startsWith("תוקנה ביום")) return true;
        if (line.startsWith("תוקנה היום")) return true;
        if (line.startsWith("תוקן על פי")) return true;

        if (line.startsWith("ה ש ו פ ט")) return true;
        if (line.startsWith("ש ו פ ט")) return true;
        if (line.startsWith("ה מ ש נ ה")) return true;
        if (line.startsWith("מ ש נ ה")) return true;
        if (line.startsWith("ה נ ש י א")) return true;
        if (line.startsWith("נ ש י א")) return true;

        if (line.startsWith("__________")) return true;
        
        return false;

    } 
    
    private void record(String tag, StringBuilder text) throws Exception {
        
        String input = text.toString().trim();
        input += "\n";
        
        tag = tag.replace("/", "-");

        String targetFilePath = this.targetFolder + tag + ".txt";
        if (!Files.exists(Paths.get(targetFilePath))) Files.createFile(Paths.get(targetFilePath));

        Files.write(Paths.get(targetFilePath), input.getBytes(), StandardOpenOption.APPEND);
            
    }
    
}
