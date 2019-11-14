package com.itcag.demo.html;

import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLProcessDocumentInput {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.PROCESS_DOCUMENT_OUTPUT.getTitle(), WebConstants.VERSION, doc));

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc));

        Element subElt = HTMLGeneratorToolbox.getForm(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), true, doc);

        subElt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.PROCESS_DOCUMENT_INPUT.getTitle(), doc));

        subElt.appendChild(HTMLGeneratorToolbox.getInput(null, FormFields.ID.getName(), false, true, doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getSearchButton(doc, "Classify"));

        elt.appendChild(subElt);
        
        return elt;
        
    }
    
}
