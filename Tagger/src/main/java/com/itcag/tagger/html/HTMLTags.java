package com.itcag.tagger.html;

import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.doc.lang.Tag;
import com.itcag.util.Encoder;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLTags {
 
    public final static String get(ArrayList<Tag> tags) throws Exception {

        String title = Targets.TAGS.getTitle();
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(title, WebConstants.VERSION, doc));
        
        root.appendChild(getBody(title, tags, doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(String title, ArrayList<Tag> tags, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc);
        elt.appendChild(breadcrumbs);

        elt.appendChild(HTMLGeneratorToolbox.getTitle(title, doc));
        
        elt.appendChild(HTMLGeneratorToolbox.getBlockLink(Targets.ADD_TAG.getUrl(), Targets.ADD_TAG.getTitle(), "right", doc));

        elt.appendChild(getList(tags, doc));
        
        return elt;
        
    }

    private static Element getList(ArrayList<Tag> tags, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getUl(doc);
        
        for (Tag tag : tags) {
            Element subElt = HTMLGeneratorToolbox.getListItem(null, doc);
            subElt.appendChild(HTMLGeneratorToolbox.getLink(Targets.EDIT_TAG.getUrl() + "?" + FormFields._ID.getName() + "=" + Encoder.encodeText(tag.get_id()), tag.getId(), doc));
            subElt.appendChild(HTMLGeneratorToolbox.getTinyInlineLink(Targets.DELETE_TAG.getUrl() + "?" + FormFields._ID.getName() + "=" + Encoder.encodeText(tag.get_id()), Targets.DELETE_TAG.getTitle(), doc));
            elt.appendChild(subElt);
        }
        
        return elt;

    }    
    
}
