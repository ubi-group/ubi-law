package com.itcag.tagger.doc;

import com.itcag.docanalyzer.MSWord;
import com.itcag.doc.lang.Document;
import java.io.File;

public class Extractor {

    public Document getDocument(String id, String extension, String filePath) throws Exception {
        
        switch (extension) {
            case "docx":
                MSWord parser = new MSWord();
                return parser.parse(id, filePath);
            default:
                throw new IllegalArgumentException("Cannot parse document. Unknown document type.");
        }
        
    }
    
}
