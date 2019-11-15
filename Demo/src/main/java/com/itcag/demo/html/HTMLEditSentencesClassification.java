package com.itcag.demo.html;

import com.itcag.demo.FormFields;
import com.itcag.demo.LegalyzerFactory;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.util.Printer;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import org.w3c.dom.Element;

public class HTMLEditSentencesClassification {

    public final static String get(com.itcag.legalyzer.util.doc.Document document, String id, int paragraphIndex) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), WebConstants.VERSION, htmlDoc));

        root.appendChild(getBody(document, htmlDoc, id, paragraphIndex));        

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
  
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(htmlDoc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, htmlDoc);
        
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.PROCESS_DOCUMENT_INPUT.getTitle(), Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), htmlDoc));
        
        Element subElt = HTMLGeneratorToolbox.getForm(Targets.EDIT_CLASSIFICATION_RESULT.getUrl(), true, htmlDoc);
        
        subElt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), htmlDoc));
System.out.println(document.getJSON());
        ArrayList<Paragraph> arrParagraphs = document.getParagraphs();
        Paragraph selectedParagraph = null;
System.out.println("paragraphIndex = " + paragraphIndex);        
        for(Paragraph paragraph: arrParagraphs) {
System.out.println("paragraph.getIndex() = " + paragraph.getIndex());
            if(paragraphIndex == paragraph.getIndex())
                selectedParagraph = paragraph;
        }
        
        subElt.appendChild(getList(selectedParagraph, htmlDoc, id, paragraphIndex));
        
        elt.appendChild(subElt);
        
        return elt;
        
    }

    private static Element getList(Paragraph paragraph, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) {

        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);

        for (Sentence sentence : paragraph.getSentences()) {
System.out.println("sentence.getText(): "  + sentence.getText());
            retVal.appendChild(getListItem(sentence, htmlDoc, id, paragraphIndex));
            if (sentence.getResult() != null && sentence.getResult().getHighestRanking() != null) {
System.out.println("sentence.getResult() != null && sentence.getResult().getHighestRanking() != null -->" + sentence.getResult().getHighestRanking());
                if (sentence.getResult().getHighestRanking().getScore() > 0.5) {
System.out.println("sentence.getResult().getHighestRanking().getScore() > 0.5 --> " + sentence.getResult().getHighestRanking().getScore());
                    if (sentence.getResult().getHighestRanking().getIndex() != 0) {                      
System.out.println("sentence.getResult().getHighestRanking().getIndex() != 0 --> " + sentence.getResult().getHighestRanking().getIndex());                        
//                        retVal.appendChild(getListItem(sentence, htmlDoc, id, paragraphIndex));

                    }

                }

            }

        }        
        
        return retVal;
        
    }
    
    private static Element getListItem(Sentence sentence, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) {
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
        
        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(sentence.getText(), htmlDoc));        

        retVal.appendChild(HTMLGeneratorToolbox.getInlineSpan(sentence.getResult().getHighestRanking().getLabel(), false, htmlDoc));
        
        retVal.appendChild(HTMLGeneratorToolbox.getHiddenInput(id, FormFields.ID.getName(), htmlDoc));
        
        Element edit = HTMLGeneratorToolbox.getDiv(htmlDoc);
        
        edit.appendChild(HTMLGeneratorToolbox.getSearchButton(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Reject", sentence.getText()+""));

        retVal.appendChild(edit);
        
        return retVal;
        
    }   
    
}
