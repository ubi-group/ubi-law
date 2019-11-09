package com.itcag.demo;

import com.itcag.legalyzer.Legalyzer;
import com.itcag.legalyzer.util.parse.HCRulingParser;
import com.itcag.legalyzer.util.parse.ParserFields;
import java.io.BufferedReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Tester {

    public static void main(String[] args) throws Exception {
        
System.out.println("Starting Legalyzer...");
long startTime = System.nanoTime();

            LegalyzerFactory legalyzerFactory = LegalyzerFactory.getInstance();
            
long endTime = System.nanoTime();
long durationInNano = (endTime - startTime);  //Total execution time in nano seconds
System.out.println("duration in milis: " + TimeUnit.NANOSECONDS.toSeconds(durationInNano));        


        Legalyzer legalyzer = legalyzerFactory.getLegalyzer();

        List<String> lines = new BufferedReader(new StringReader("")).lines().collect(Collectors.toList());        
        
        Properties config = new Properties();
        config.setProperty(ParserFields.MAX_LINE_LENGTH.getName(), "300");
        config.setProperty(ParserFields.MAX_NUM_PARAGRAPHS.getName(), "6");
        config.setProperty(ParserFields.STRIP_OFF_BULLETS.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_QUOTES.getName(), Boolean.TRUE.toString());
        config.setProperty(ParserFields.REMOVE_PARENTHESES.getName(), Boolean.TRUE.toString());
        
        com.itcag.legalyzer.util.doc.Document document = new com.itcag.legalyzer.util.doc.Document("על בסיס של בחינה פרטנית. בנסיבות העניין ועל פי המלצתנו חזר בו בא-כוח העותרים מהעתירה, תוך שרשמנו לפנינו את האמור.", new ArrayList(lines), new HCRulingParser(config));           
        
    }

    
}
