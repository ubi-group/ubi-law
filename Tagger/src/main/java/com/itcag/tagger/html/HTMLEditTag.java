package com.itcag.tagger.html;

import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.tagger.lang.Tag;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLEditTag {

    public final static String get(Tag tag) throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.EDIT_TAG.getTitle(), WebConstants.VERSION, doc));

        root.appendChild(getBody(tag, doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Tag tag, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, Targets.TAGS.getTitle(), Targets.TAGS.getUrl(), doc);
        elt.appendChild(breadcrumbs);

        Element subElt = HTMLGeneratorToolbox.getForm(Targets.EDIT_TAG.getUrl(), true, doc);

        subElt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.EDIT_TAG.getTitle(), doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getHiddenInput(tag.get_id(), FormFields._ID.getName(), doc));

        subElt.appendChild(HTMLGeneratorToolbox.getLabeledInput("ID", tag.getId(), FormFields.ID.getName(), true, false, doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getLabeledInput("Label", null, FormFields.LABEL.getName(), false, true, doc));
        
        subElt.appendChild(HTMLGeneratorToolbox.getSubmitButton(doc));
        
        elt.appendChild(subElt);
        
        return elt;
        
    }

}
