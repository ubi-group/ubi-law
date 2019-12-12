package com.itcag.demo.html;

import com.itcag.datatier.schema.SentenceFields;
import com.itcag.demo.Config;
import com.itcag.demo.DataTierAPI;
import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.util.Encoder;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import java.util.ArrayList;
import org.json.JSONObject;
import org.w3c.dom.Element;

public class HTMLSentencesClassification {

    public final static String get(com.itcag.legalyzer.util.doc.Document document, String id, int paragraphIndex) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        root.setAttribute("lang", "he");        
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), WebConstants.VERSION, htmlDoc, HTMLHeader.getScripts(), HTMLHeader.getStyles()));

        root.appendChild(getBody(document, htmlDoc, id, paragraphIndex));        

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
  
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(htmlDoc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, htmlDoc);
        
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.PROCESS_DOCUMENT_INPUT.getTitle(), Targets.PROCESS_DOCUMENT_INPUT.getUrl(), htmlDoc));
                      
        Element title = HTMLGeneratorToolbox.getTitle(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), htmlDoc);
        Element link = HTMLGeneratorToolbox.getLink(document.getId(), document.getId(), htmlDoc);
        
        title.appendChild(link);
        
        elt.appendChild(title);       
        
        ArrayList<Paragraph> arrParagraphs = document.getParagraphs();
        Paragraph selectedParagraph = null;
     
        for(Paragraph paragraph: arrParagraphs) {
            if(paragraphIndex == paragraph.getIndex())
                selectedParagraph = paragraph;
        }
        
        elt.appendChild(HTMLGeneratorToolbox.getHiddenInput(id, FormFields.ID.getName(), htmlDoc));
        elt.appendChild(HTMLGeneratorToolbox.getHiddenInput(selectedParagraph.getIndex()+"", FormFields.PARAGRAPH_INDEX.getName(), htmlDoc));        
        
        elt.appendChild(getList(selectedParagraph, htmlDoc, id, paragraphIndex));
        
        return elt;
        
    }

    private static Element getList(Paragraph paragraph, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) throws Exception {
        
        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);       

        for (Sentence sentence : paragraph.getSentences()) {
            retVal.appendChild(getListItem(sentence, htmlDoc, id, paragraphIndex));
        }        
        
        return retVal;
        
    }
    
    private static Element getListItem(Sentence sentence, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) throws Exception {
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);        

        StringBuilder url = new StringBuilder();
        url.append(Targets.EDIT_CLASSIFICATION_RESULT.getUrl());
        url.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(id));
        url.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraphIndex);
        
        Element subElt = HTMLGeneratorToolbox.getForm(url.toString(), true, htmlDoc);
        
        subElt.appendChild(HTMLGeneratorToolbox.getBlockSpan(sentence.getText(), htmlDoc));       
        
        String categoryId = null;

        JSONObject jsonSentence = DataTierAPI.getCorrection(sentence.getText());
        
        if(jsonSentence != null) {
            categoryId = jsonSentence.getString(SentenceFields.categoryId.getFieldName());
        } else {
            if(sentence.getResult() != null && sentence.getResult().getHighestRanking() != null) {
                if(sentence.getResult().getHighestRanking().getScore() > Config.HIGHEST_RANKING) {
                    if(sentence.getResult().getHighestRanking().getIndex() > Config.HIGHEST_GENERIC_INDEX) {
                        if(jsonSentence == null) {                              
                            Category category = sentence.getResult().getHighestRanking();
                            if(category != null)
                                categoryId = sentence.getResult().getHighestRanking().getLabel();
                        }                        
                    }
                }
            }                
        }
System.out.println("categoryId" + categoryId);
        if(categoryId != null)
            subElt.appendChild(HTMLGeneratorToolbox.getInlineSpan(categoryId, false, htmlDoc));
                
        Element ModifyDiv = HTMLGeneratorToolbox.getDiv(htmlDoc);
        ModifyDiv.appendChild(HTMLGeneratorToolbox.getButton(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Modify", sentence.getText()));

        subElt.appendChild(ModifyDiv);
        
        retVal.appendChild(subElt);
        
        return retVal;
        
    }   
    
}
