package com.itcag.demo.html;

import com.itcag.datatier.schema.SentenceFields;
import com.itcag.demo.Config;
import com.itcag.demo.DataTierAPI;
import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.LegalyzerFactory;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.Legalyzer;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.Sentence;
import com.itcag.legalyzer.util.doc.extr.CourtRuling;
import com.itcag.legalyzer.util.doc.extr.Law;
import com.itcag.legalyzer.util.doc.extr.Person;
import com.itcag.legalyzer.util.doc.extr.penalty.Penalty;
import com.itcag.util.Encoder;
import com.itcag.util.Printer;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import org.json.JSONObject;
import org.w3c.dom.Element;

public class HTMLProcessDocumentOutput {

    public final static String get(com.itcag.legalyzer.util.doc.Document document) throws Exception {
        
        org.w3c.dom.Document htmlDoc = XMLProcessor.getDocument("html");
        Element root = htmlDoc.getDocumentElement();
       
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), WebConstants.VERSION, htmlDoc, HTMLHeader.getScripts()));

        root.appendChild(getBody(document, htmlDoc));        

        return XMLProcessor.convertDocumentToString(htmlDoc);
        
    }
  
    private static Element getBody(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(htmlDoc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, htmlDoc);
        
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.PROCESS_DOCUMENT_INPUT.getTitle(), Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), htmlDoc));
        
        elt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), htmlDoc));
        
        Element eleDiv = HTMLGeneratorToolbox.getDiv(htmlDoc);
        
        Element eleDiv1 = HTMLGeneratorToolbox.getDiv50(htmlDoc);
        eleDiv1.appendChild(getList(document, htmlDoc));
        Element eleDiv2 = HTMLGeneratorToolbox.getDiv50(htmlDoc);
        eleDiv2.appendChild(extract(document.getId(), htmlDoc));
        
        eleDiv.appendChild(eleDiv1); 
        eleDiv.appendChild(eleDiv2); 
        
        elt.appendChild(eleDiv);
        
        return elt;
        
    }

    private static Element getList(com.itcag.legalyzer.util.doc.Document document, org.w3c.dom.Document htmlDoc) throws Exception {

        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);

        for (Paragraph paragraph : document.getParagraphs()) {
            
            retVal.appendChild(getListItem(paragraph, document.getId(), htmlDoc));
        }
        
        return retVal;
        
    }
    
    private static Element getListItem(Paragraph paragraph, String docId, org.w3c.dom.Document htmlDoc) throws Exception {
        
        StringBuilder url = new StringBuilder();
        url.append(Targets.CLASSIFICATION_RESULT.getUrl());
        url.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText(docId));
        url.append("&").append(FormFields.PARAGRAPH_INDEX.getName()).append("=").append(paragraph.getIndex());
        
        Element subElt = HTMLGeneratorToolbox.getForm(url.toString(), true, htmlDoc);
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);

        StringBuilder label = new StringBuilder();
        label.append(paragraph.getText());
        label.append(" (").append(paragraph.getEvaluation(LegalyzerFactory.getCategories().get())).append(")");
        
        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(paragraph.getText(), htmlDoc));
        
        TreeMap<Integer, Category> mapScoreCat = LegalyzerFactory.getCategories().get();  
        LinkedHashMap<Integer, Category> map = paragraph.getEvaluation(mapScoreCat);
        
        ArrayList<String> arrCats = new ArrayList();
        for(Sentence sentence: paragraph.getSentences()) {
            JSONObject jsonSentence = DataTierAPI.getCorrection(sentence.getText());
            String categoryId = null;           
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
            if(categoryId != null && !categoryId.isEmpty())
                arrCats.add(categoryId);

        }        

        int size = arrCats.size();

        int pos = 0;
        boolean indent = false;
        for(String catLabl : arrCats) {           
            String catLabel = catLabl.replace("_", " ");
            if(size != 1 && pos != size -1)
              catLabel = catLabel + ",";
           
            retVal.appendChild(HTMLGeneratorToolbox.getInlineSpan(catLabel, indent, htmlDoc));
            pos++;
            indent = true;
        }       
        
        Element edit = HTMLGeneratorToolbox.getDiv(htmlDoc);
        edit.appendChild(HTMLGeneratorToolbox.getButton(htmlDoc, "test", "Edit", paragraph.getIndex()+""));

        retVal.appendChild(edit);
        
        subElt.appendChild(retVal);
        
        return subElt;
        
    }   
    
    private static Element extract(String id, org.w3c.dom.Document htmlDoc) throws Exception {
        
        com.itcag.legalyzer.util.doc.Document document = DocumentProcessor.classifySimpleParser(id);

        Element eleDiv = HTMLGeneratorToolbox.getDiv(htmlDoc);

        Properties extractionConfig = new Properties();
        extractionConfig.put(Legalyzer.ExtractionOptions.LAW.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.RULINGS.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.PERSONNEL.getName(), Boolean.TRUE);
        extractionConfig.put(Legalyzer.ExtractionOptions.PENALTY.getName(), Boolean.TRUE);
        LegalyzerFactory.getInstance().getLegalyzer().extract(document, extractionConfig);
        
        Element listJudges = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);
        listJudges.appendChild(HTMLGeneratorToolbox.getH3("Personnel:", htmlDoc));
        
        if (!document.getJudges().isEmpty() || !document.getPlaintiffAttorneys().isEmpty() || !document.getDefendantAttorneys().isEmpty()) {
            for (Person person : document.getJudges()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(person.toString(), htmlDoc));
                listJudges.appendChild(retVal);
            }
            for (Person person : document.getPlaintiffAttorneys()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(person.toString(), htmlDoc));
                listJudges.appendChild(retVal);
            }
            for (Person person : document.getDefendantAttorneys()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(person.toString(), htmlDoc));
                listJudges.appendChild(retVal);
            }
        }
        eleDiv.appendChild(listJudges);
        
        Element listLaws = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);
        listLaws.appendChild(HTMLGeneratorToolbox.getH3("Referenced laws:", htmlDoc));
        if (!document.getLaws().isEmpty()) {
            for (Map.Entry<String, Law> entry : document.getLaws().entrySet()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(entry.getValue().toString(), htmlDoc));
                listLaws.appendChild(retVal);
            }
        }
        eleDiv.appendChild(listLaws);
        
        Element listRulings = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);
        listRulings.appendChild(HTMLGeneratorToolbox.getH3("Referenced court rulings:", htmlDoc));
        if (!document.getRulings().isEmpty()) {
            for (Map.Entry<String, CourtRuling> entry : document.getRulings().entrySet()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(entry.getValue().toString(), htmlDoc));
                listRulings.appendChild(retVal);
            }
        }
        eleDiv.appendChild(listRulings);

        Element listPenalties = HTMLGeneratorToolbox.getUlNoDiscs(htmlDoc);        
        listPenalties.appendChild(HTMLGeneratorToolbox.getH3("Penalties:", htmlDoc));
        if (!document.getPenalties().isEmpty()) {
            for (Penalty penalty : document.getPenalties()) {
                Element retVal = HTMLGeneratorToolbox.getListItem(null, htmlDoc);
                retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(penalty.toString(), htmlDoc));
                listPenalties.appendChild(retVal);
            }
        }        
        eleDiv.appendChild(listPenalties);
        
        return eleDiv;
        
    }    
    
}
