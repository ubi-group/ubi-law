package com.itcag.tagger.html;

import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLAddTag {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.ADD_TAG.getTitle(), WebConstants.VERSION, doc));

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, Targets.TAGS.getTitle(), Targets.TAGS.getUrl(), doc);
        elt.appendChild(breadcrumbs);

        Element subElt = HTMLGeneratorToolbox.getForm(Targets.ADD_TAG.getUrl(), true, doc);

        subElt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.ADD_TAG.getTitle(), doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getLabeledInputWithInstruction("ID", "Id should be human readable, and cannot be edited once it is submitted.", null, FormFields.ID.getName(), false, true, doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getLabeledInput("Label", null, FormFields.LABEL.getName(), false, false, doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getSubmitButton(doc));
        
        elt.appendChild(subElt);
        
        return elt;
        
    }

}
