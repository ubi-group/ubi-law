package com.itcag.utils.html;

import com.itcag.util.txt.TextToolbox;
import com.itcag.util.XMLProcessor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLErrorPageGenerator {

    public final static String get(Exception exception, String title, String version, String home) {

        try {
            
            Document doc = XMLProcessor.getDocument("html");
            Element root = doc.getDocumentElement();

            root.appendChild(HTMLGeneratorToolbox.getHead(title, version, doc));

            root.appendChild(getBody(exception, title, home, doc));

            return XMLProcessor.convertDocumentToString(doc);

        } catch (Exception ex) {
            
            throw new RuntimeException(ex);
            
        }
        
    }

    private static Element getBody(Exception exception, String title, String home, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", home, doc);
        elt.appendChild(breadcrumbs);

        elt.appendChild(HTMLGeneratorToolbox.getTitle(title, doc));

        elt.appendChild(HTMLGeneratorToolbox.getBlockSpan(exception.getClass().getCanonicalName(), doc));
        
        if (!TextToolbox.isEmpty(exception.getMessage())) {
            elt.appendChild(HTMLGeneratorToolbox.getBlockSpan(exception.getMessage(), doc));
        }
        
        if (exception.getCause() != null) {
            elt.appendChild(HTMLGeneratorToolbox.getBlockSpan("Caused by:", doc));
            elt.appendChild(HTMLGeneratorToolbox.getBlockSpan(exception.getCause().getClass().getCanonicalName(), doc));
        }

        Element subElt = doc.createElement("ul");
        subElt.setAttribute("style", "display:block; clear:both; float:left; margin-top:20px; margin-left: 20px");
        for (StackTraceElement element : exception.getStackTrace()) {
            Element subSubElt = doc.createElement("li");
            subSubElt.setTextContent(element.toString());
            subElt.appendChild(subSubElt);
        }
        elt.appendChild(subElt);
        
        return elt;
        
    }

}
