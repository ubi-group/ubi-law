package com.itcag.demo.html;

import com.itcag.demo.FormFields;
import com.itcag.demo.LegalyzerFactory;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import java.util.Map;
import java.util.TreeMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLProcessDocumentOutput {

    public final static String get(com.itcag.legalyzer.util.doc.Document document) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), WebConstants.VERSION, htmlDoc));

        root.appendChild(getBody(document, htmlDoc));

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
/*
        for (Paragraph paragraph : document.getParagraphs()) {
            
            LinkedHashMap<Integer, Category> categories = paragraph.getEvaluation(LegalyzerFactory.getCategories().get());

            for (Sentence sentence : paragraph.getSentences()) {
                
                if (sentence.getResult() != null && sentence.getResult().getHighestRanking() != null) {
                    
                    if (sentence.getResult().getHighestRanking().getScore() > 0.5) {

                        if (sentence.getResult().getHighestRanking().getIndex() != 0) {
                            
                            Printer.print(sentence.getText());
                            Printer.print(document.getId());
                            Printer.print(sentence.getResult().getHighestRanking().toString());

                            for (Recommendation recommendation : sentence.getRecommendations()) {
                                if (recommendation.getValue() > 0.70) {
                                    Printer.print("\t" + recommendation.toString());
                                }
                            }

                            Printer.print();
                        
                        }

                    }

                }
                
            }
            
        }    
*/    
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(htmlDoc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, htmlDoc);
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.PROCESS_DOCUMENT_INPUT.getTitle(), Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), htmlDoc));

        elt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), htmlDoc));

        elt.appendChild(getList(document, htmlDoc));
        
        return elt;
        
    }

    private static Element getList(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc) {

        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);

        for (Paragraph paragraph : document.getParagraphs()) {
            
            retVal.appendChild(getListItem(paragraph, document.getId(), htmlDoc));
        }
        
        return retVal;
        
    }
    
    private static Element getListItem(Paragraph paragraph, String docId, org.w3c.dom.Document htmlDoc) {
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);

        StringBuilder url = new StringBuilder();
        url.append(Targets.EDIT_CLASSIFICATION_RESULT.getUrl());
        url.append("?").append(FormFields.ID.getName()).append("=").append(docId);
        url.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraph.getIndex());

        StringBuilder label = new StringBuilder();
        label.append(paragraph.getText());
        label.append(" (").append(paragraph.getEvaluation(LegalyzerFactory.getCategories().get())).append(")");
        
        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(paragraph.getText(), htmlDoc));
        
        TreeMap<Integer, Category> mapScoreCat = LegalyzerFactory.getCategories().get();        
        for(Map.Entry<Integer, Category> entry : mapScoreCat.entrySet()) {
          Category cat = entry.getValue();
          retVal.appendChild(HTMLGeneratorToolbox.getInlineSpan(cat.getLabel(), false, htmlDoc));
        }
        
 //       retVal.appendChild(HTMLGeneratorToolbox.getBlockLink(url.toString(), "Edit", "left", htmlDoc));
        retVal.appendChild(HTMLGeneratorToolbox.getHiddenInput(docId, FormFields.ID.getName(), htmlDoc));
        retVal.appendChild(HTMLGeneratorToolbox.getHiddenInput(paragraph.getIndex()+"", FormFields.PARAGRAPH_INDEX.getName(), htmlDoc));
        retVal.appendChild(HTMLGeneratorToolbox.getSearchButton(htmlDoc, "Edit"));


        
        return retVal;
        
    }    
    
}
