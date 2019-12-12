package com.itcag.demo.html;

import com.itcag.util.txt.TextToolbox;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLErrorPageGenerator {

    public final static String get(String msg, String title, String version, String home) {

        try {
            
            Document doc = XMLProcessor.getDocument("html");
            Element root = doc.getDocumentElement();
            root.setAttribute("dir", "rtl");
            root.setAttribute("lang", "he");            

            root.appendChild(HTMLGeneratorToolbox.getHead(title, version, doc, null, HTMLHeader.getStyles()));

            root.appendChild(getBody(msg, title, home, doc));

            return XMLProcessor.convertDocumentToString(doc);

        } catch (Exception ex) {
            
            throw new RuntimeException(ex);
            
        }
        
    }

    private static Element getBody(String msg, String title, String home, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", home, doc);
        elt.appendChild(breadcrumbs);

        elt.appendChild(HTMLGeneratorToolbox.getTitle(title, doc));

        elt.appendChild(HTMLGeneratorToolbox.getBlockSpan(msg, doc));
          
        return elt;
        
    }

}
