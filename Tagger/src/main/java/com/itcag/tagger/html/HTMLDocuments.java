package com.itcag.tagger.html;

import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.util.Encoder;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLDocuments {
 
    public final static String get(ArrayList<com.itcag.tagger.lang.Document> documents) throws Exception {

        String title = Targets.DOCUMENTS.getTitle();
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(title, WebConstants.VERSION, doc));
        
        root.appendChild(getBody(title, documents, doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(String title, ArrayList<com.itcag.tagger.lang.Document> documents, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc);
        elt.appendChild(breadcrumbs);

        elt.appendChild(HTMLGeneratorToolbox.getTitle(title, doc));
        
        elt.appendChild(HTMLGeneratorToolbox.getBlockLink(Targets.ADD_DOCUMENT.getUrl(), Targets.ADD_DOCUMENT.getTitle(), "right", doc));

        Element subElt = HTMLGeneratorToolbox.getForm(Targets.DOCUMENTS.getUrl(), true, doc);
        subElt.appendChild(HTMLGeneratorToolbox.getInput(null, FormFields.QUERY.getName(), false, true, doc));
        subElt.appendChild(HTMLGeneratorToolbox.getSearchButton(doc));
        elt.appendChild(subElt);

        if (!documents.isEmpty()) elt.appendChild(getList(documents, doc));
        
        return elt;
        
    }

    private static Element getList(ArrayList<com.itcag.tagger.lang.Document> documents, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getUl(doc);
        
        for (com.itcag.tagger.lang.Document document : documents) {
            Element subElt = HTMLGeneratorToolbox.getListItem(null, doc);
            subElt.appendChild(HTMLGeneratorToolbox.getLink(Targets.DOCUMENT.getUrl() + "?" + FormFields._ID.getName() + "=" + Encoder.encodeText(document.get_id()), document.getId(), doc));
            subElt.appendChild(HTMLGeneratorToolbox.getTinyInlineLink(Targets.DELETE_DOCUMENT.getUrl() + "?" + FormFields._ID.getName() + "=" + Encoder.encodeText(document.get_id()), Targets.DELETE_TAG.getTitle(), doc));
            elt.appendChild(subElt);
        }
        
        return elt;

    }    
    
}
