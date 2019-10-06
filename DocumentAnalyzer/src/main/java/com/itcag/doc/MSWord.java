package com.itcag.doc;

import com.itcag.doc.lang.Document;
import com.itcag.doc.lang.Sentence;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

public class MSWord {

    public Document getSentences(String id, String filePath) throws Exception {
        
        Document retVal = new Document(id);
        
        Path msWordPath = Paths.get(filePath);
        
        int paragraphIndex = 0;
        int sentenceIndex = 0;
        
        try (XWPFDocument document = new XWPFDocument(Files.newInputStream(msWordPath))) {
            
            List<XWPFParagraph> paragraphs = document.getParagraphs();
            
            for (XWPFParagraph paragraph : paragraphs) {
                
            }
            
        }        
        
        return retVal;
        
    }
    
}
