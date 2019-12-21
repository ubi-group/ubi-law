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

public class HTMLEditSentencesClassification {

    public final static String get(com.itcag.legalyzer.util.doc.Document document, String id, int paragraphIndex, boolean isBeingModifiedMode) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        root.setAttribute("lang", "he");
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), WebConstants.VERSION, htmlDoc, HTMLHeader.getScripts(), HTMLHeader.getStyles()));

        root.appendChild(getBody(document, htmlDoc, id, paragraphIndex, isBeingModifiedMode));        

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
  
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex, boolean isBeingModifiedMode) throws Exception {
        
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
        
        elt.appendChild(getList(selectedParagraph, htmlDoc, id, paragraphIndex, isBeingModifiedMode));
        
        return elt;
        
    }

    private static Element getList(Paragraph paragraph, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex, boolean isBeingModifiedMode) throws Exception {
        
        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);       

        for (Sentence sentence : paragraph.getSentences()) {
            retVal.appendChild(getListItem(sentence, htmlDoc, id, paragraphIndex, isBeingModifiedMode));
        }        
        
        return retVal;
        
    }
    
    private static Element getListItem(Sentence sentence, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex, boolean isBeingModifiedMode) throws Exception {
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);        

        StringBuilder urlModify = new StringBuilder();
        urlModify.append(Targets.CLASSIFICATION_RESULT.getUrl());
        urlModify.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(id));
        urlModify.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraphIndex);
        urlModify.append("&").append(FormFields.IS_BEING_MODIFIED.getName()).append("=").append("true"); 
        
        Element subEltFormModify = HTMLGeneratorToolbox.getForm(urlModify.toString(), false, htmlDoc);
        
        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(sentence.getText(), htmlDoc));       
        
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
            retVal.appendChild(HTMLGeneratorToolbox.getInlineSpan(categoryId, false, htmlDoc));
        
        Element classDiv = htmlDoc.createElement("div");
        classDiv.setAttribute("class", "container");
        
        Element classRow = htmlDoc.createElement("div");
        classRow.setAttribute("class", "row");
        
        Element classCol = htmlDoc.createElement("div");
        classCol.setAttribute("class", "col-12");
        
        Element classCustomeSearch = htmlDoc.createElement("div");
        classCustomeSearch.setAttribute("id", "custom-search-input");
        
        Element classInput = htmlDoc.createElement("div");
        classInput.setAttribute("class", "input-group");        
        
        Element button = htmlDoc.createElement("input");
        button.setAttribute("name", "search");
        button.setAttribute("id", "search");
        button.setAttribute("type", "text");
//        button.setAttribute("class", "ui-widget");
        button.setAttribute("style", "float:left");;
//        button.setAttribute("placeholder", "Search");

        classInput.appendChild(button);
        classCustomeSearch.appendChild(classInput);
        classCol.appendChild(classCustomeSearch);
        classRow.appendChild(classCol);
        classDiv.appendChild(classRow);   
        subEltFormModify.appendChild(button);    
        
 //       subEltFormModify.appendChild(classDiv);    

        StringBuilder urlCancel = new StringBuilder();
        urlCancel.append(Targets.CLASSIFICATION_RESULT.getUrl());
        urlCancel.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(id));
        urlCancel.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraphIndex);
        urlCancel.append("&").append(FormFields.IS_BEING_MODIFIED.getName()).append("=").append("false");   
        
        subEltFormModify.appendChild(HTMLGeneratorToolbox.getButtonInlineNoMargin(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Modify", sentence.getText()));
                
        Element subEltFormCancelButton = HTMLGeneratorToolbox.getForm(urlCancel.toString(), false, htmlDoc);
        subEltFormCancelButton.appendChild(HTMLGeneratorToolbox.getButtonInlineNoMargin(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Cancel", sentence.getText()));
        
        retVal.appendChild(subEltFormModify);
        retVal.appendChild(subEltFormCancelButton);  
     
        return retVal;
        
    }   
    
}
