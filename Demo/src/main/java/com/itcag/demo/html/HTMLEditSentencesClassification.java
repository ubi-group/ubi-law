package com.itcag.demo.html;

import com.itcag.datatier.schema.SentenceFields;
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

    public final static String get(com.itcag.legalyzer.util.doc.Document document, String id, int paragraphIndex) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), WebConstants.VERSION, htmlDoc, HTMLHeader.getScripts()));

        root.appendChild(getBody(document, htmlDoc, id, paragraphIndex));        

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
  
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc, String id, int paragraphIndex) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(htmlDoc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, htmlDoc);
        
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.PROCESS_DOCUMENT_INPUT.getTitle(), Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), htmlDoc));
        
        elt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.EDIT_CLASSIFICATION_RESULT.getTitle(), htmlDoc));

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
            if (sentence.getResult() != null && sentence.getResult().getHighestRanking() != null) {
                if (sentence.getResult().getHighestRanking().getScore() > 0.5) {
                    if (sentence.getResult().getHighestRanking().getIndex() != 0) {                      

                    }

                }

            }

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
            Category category = sentence.getResult().getHighestRanking();
            if(category != null)
                categoryId = sentence.getResult().getHighestRanking().getLabel();
        }
        subElt.appendChild(HTMLGeneratorToolbox.getInlineSpan(categoryId, false, htmlDoc));
                
        Element edit = HTMLGeneratorToolbox.getDiv(htmlDoc);
        
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
        button.setAttribute("class", "ui-widget");
//        button.setAttribute("class", "form-control");
        button.setAttribute("placeholder", "Search");
//        button.setAttribute("style", "width:100%");
        
        if(categoryId != null && !categoryId.isEmpty()) {
            button.setAttribute("value", categoryId);
            edit.appendChild(HTMLGeneratorToolbox.getButton(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Reject", sentence.getText()));
            subElt.appendChild(HTMLGeneratorToolbox.getHiddenInput("false", FormFields.IS_CATEGORY_ADDITION.getName(), htmlDoc));
        
        } else {
            edit.appendChild(HTMLGeneratorToolbox.getButton(htmlDoc, FormFields.SENTENCE_TEXT.getName(), "Add", sentence.getText()));
            subElt.appendChild(HTMLGeneratorToolbox.getHiddenInput("true", FormFields.IS_CATEGORY_ADDITION.getName(), htmlDoc));
            classInput.appendChild(button);
            classCustomeSearch.appendChild(classInput);
            classCol.appendChild(classCustomeSearch);
            classRow.appendChild(classCol);
            classDiv.appendChild(classRow);
        }                
        
        
        subElt.appendChild(classDiv); 
        retVal.appendChild(subElt);
        
        subElt.appendChild(edit);
        
        return retVal;
        
    }   
    
}
