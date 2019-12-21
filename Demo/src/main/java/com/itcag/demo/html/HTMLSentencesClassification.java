package com.itcag.demo.html;

import com.itcag.datatier.schema.SentenceFields;
import com.itcag.demo.AutocompletionCategories;
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
        
        elt.appendChild(title);           
                
        Element eleDivLink = htmlDoc.createElement("div");
        eleDivLink.setAttribute("style", "clear:both; display:block; float:right; width:100%; margin-bottom:30px; margin-top:10px; ");        
        eleDivLink.appendChild(link);  
        elt.appendChild(eleDivLink);
        

        Element datalist = htmlDoc.createElement("datalist");
        datalist.setAttribute("id", "categories"); 
        for(String cat: AutocompletionCategories.getInstance(Config.ALL_CATEGORIES).getAllCategories()) {
            Element option = htmlDoc.createElement("option");
            option.setAttribute("value", cat);  
            datalist.appendChild(option);
        }
        elt.appendChild(datalist);
        
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
        url.append(Targets.CLASSIFICATION_RESULT.getUrl());
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

        if(categoryId != null) {
 /*
 <div style="clear:both; display:block; float:right; ">
<a  style="display:block; float:right;" href="SentencesClassification?id=https%3A%2F%2Fsupremedecisions.court.gov.il%2FHome%2FDownload%3Fpath%3DHebrewVerdicts%255C%255C%255C%255C%255C%255C%255C%255C16%255C%255C%255C%255C%255C%255C%255C%255C540%255C%255C%255C%255C%255C%255C%255C%255C070%255C%255C%255C%255C%255C%255C%255C%255Cd13%26fileName%3D16070540.D13%26type%3D2&amp;removeTag=true&amp;sentenceText=%D7%A4%D7%A1%D7%A7+%D7%94%D7%93%D7%99%D7%9F+%D7%99%D7%95%D7%91%D7%90+%D7%9C%D7%99%D7%93%D7%99%D7%A2%D7%AA+%D7%99%D7%95%D7%9E%D7%9F+%D7%91%D7%99%D7%AA+%D7%94%D7%9E%D7%A9%D7%A4%D7%98.&amp;paragraphIndex=3" id="removeTag">Remove</a>
<span style="display:block; float:right; margin-right:10px">Administrative - Education</span>
</div>
<div style="clear:both; display:block; float:right; width:100%;" >
<input id="search" list="categories" name="search" type="text" style=" margin-right:10px">
<button name="replaceTag" style="float:right;" type="submit" value="פסק הדין יובא לידיעת יומן בית המשפט.">Replace</button>
</div>
           
            */   
            Element divLablRemove = htmlDoc.createElement("div");
            divLablRemove.setAttribute("style", "clear:both; display:block; float:right;");
                        
            StringBuilder urlRemove = new StringBuilder();
            urlRemove.append(Targets.CLASSIFICATION_RESULT.getUrl());
            urlRemove.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(id));
            urlRemove.append("&").append(FormFields.REMOVE_TAG.getName()).append("=").append("true");
            urlRemove.append("&").append(FormFields.SENTENCE_TEXT.getName()).append("=").append(Encoder.encodeText(sentence.getText())); 
            urlRemove.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraphIndex);
            
            Element eleRemove = htmlDoc.createElement("a");
            eleRemove.setAttribute("href", urlRemove.toString());
            eleRemove.setAttribute("id", FormFields.REMOVE_TAG.getName());
            eleRemove.setAttribute("style", "display:block; float:right;");
            eleRemove.setTextContent("הסר");
            divLablRemove.appendChild(eleRemove);
            
            Element spanCategory = htmlDoc.createElement("span");
            spanCategory.setAttribute("style", "display:block; float:right; margin-right:10px");
            spanCategory.setTextContent(categoryId);
            divLablRemove.appendChild(spanCategory);
           
            Element divLabReplace = htmlDoc.createElement("div");
            divLabReplace.setAttribute("style", "lear:both; display:block; float:right; width:100%;");
            
            Element categories = htmlDoc.createElement("input");
            categories.setAttribute("id", "search");
            categories.setAttribute("name", "search");
            categories.setAttribute("type", "text");
            categories.setAttribute("list", "categories");
            categories.setAttribute("style", "margin-right:10px");
            divLabReplace.appendChild(categories);

            Element replace = htmlDoc.createElement("button");
            replace.setAttribute("style", "float:right;");
            replace.setAttribute("type", "submit");
            replace.setAttribute("value", sentence.getText());
            replace.setAttribute("name", FormFields.REPLACE_TAG.getName());
            replace.setTextContent("החלף");
            divLabReplace.appendChild(replace);
            
            subElt.appendChild(divLablRemove);   
            subElt.appendChild(divLabReplace);   

        } else {

            Element categories = htmlDoc.createElement("input");
            categories.setAttribute("id", "search");
            categories.setAttribute("name", "search");
            categories.setAttribute("type", "text");
            categories.setAttribute("list", "categories");
            categories.setAttribute("style", "margin-right:10px");

            StringBuilder urlAdd = new StringBuilder();
            urlAdd.append(Targets.CLASSIFICATION_RESULT.getUrl());
            urlAdd.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(id));
            urlAdd.append("&").append(FormFields.REPLACE_TAG.getName()).append("=").append("true");
            urlAdd.append("&").append(FormFields.SENTENCE_TEXT.getName()).append("=").append(Encoder.encodeText(sentence.getText())); 
            urlAdd.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraphIndex);
            
            subElt.appendChild(categories);             
            subElt.appendChild(HTMLGeneratorToolbox.getButtonInlineNoMargin(htmlDoc, FormFields.ADD_TAG.getName(), "הוסף", sentence.getText()));
            
        }                                    
        
        retVal.appendChild(subElt);
        
        return retVal;
        
    }   
    
}
