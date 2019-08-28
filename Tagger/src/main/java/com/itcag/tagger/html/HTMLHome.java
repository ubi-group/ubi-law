package com.itcag.tagger.html;

import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.util.XMLProcessor;
import com.itcag.utils.html.HTMLGeneratorToolbox;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLHome {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(WebConstants.GENERAL_TITLE, WebConstants.VERSION, doc));

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        elt.appendChild(HTMLGeneratorToolbox.getTitle(WebConstants.GENERAL_TITLE + " v." + WebConstants.VERSION, doc));
        elt.appendChild(HTMLGeneratorToolbox.getResetDiv(doc));

        {
            Element subElt = HTMLGeneratorToolbox.getMenuDiv(doc);
            subElt.appendChild(HTMLGeneratorToolbox.getH4("TAGS", doc));
            subElt.appendChild(getTagMenu(doc));
            elt.appendChild(subElt);
        }
        
        {
            Element subElt = HTMLGeneratorToolbox.getMenuDiv(doc);
            subElt.appendChild(HTMLGeneratorToolbox.getH4("DOCUMENTS", doc));
            subElt.appendChild(getDocumentMenu(doc));
            elt.appendChild(subElt);
        }
        
        return elt;
        
    }

    private static Element getTagMenu(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getMenuUl(doc);
        
        StringBuilder url = new StringBuilder();
        url.append(Targets.TAGS.getUrl());
        Element subElt =  HTMLGeneratorToolbox.getMenuItem(url.toString(), Targets.TAGS.getTitle(), doc);
        elt.appendChild(subElt);
        
        return elt;
        
    }
    
    private static Element getDocumentMenu(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getMenuUl(doc);
        
        {
            Element subElt =  HTMLGeneratorToolbox.getMenuItem(Targets.DOCUMENTS.getUrl(), "Search Documents", doc);
            elt.appendChild(subElt);
        }
        
        {
            Element subElt =  HTMLGeneratorToolbox.getMenuItem(Targets.DOCUMENTS_NOT_COMPLETED.getUrl(), Targets.DOCUMENTS_NOT_COMPLETED.getTitle(), doc);
            elt.appendChild(subElt);
        }        

        {
            Element subElt =  HTMLGeneratorToolbox.getMenuItem(Targets.DOCUMENTS_NOT_APPROVED.getUrl(), Targets.DOCUMENTS_NOT_APPROVED.getTitle(), doc);
            elt.appendChild(subElt);
        } 
        
        return elt;
        
    }
    
}
