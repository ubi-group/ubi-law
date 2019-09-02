package com.itcag.demo.html;

import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLTest {

    public final static String get(String query, HashMap<String, String> results) throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        
        Element head = HTMLGeneratorToolbox.getHead(Targets.TEST.getTitle(), WebConstants.VERSION, doc);
        Element subElt = HTMLGeneratorToolbox.getHebrewFont(doc);
        head.appendChild(subElt);
        root.appendChild(head);

        root.appendChild(getBody(query, results, doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(String query, HashMap<String, String> results, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        elt.setAttribute("style", "font-family: 'Varela Round', sans-serif; padding: 20px; ");
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc);
        elt.appendChild(breadcrumbs);

        Element subElt = HTMLGeneratorToolbox.getTitle(Targets.TEST.getTitle(), doc);
        subElt.setAttribute("style", "clear:both; float:right;");
        elt.appendChild(subElt);

        elt.appendChild(getSearchBox(query, doc));
        
        if (results != null) elt.appendChild(getResultDisplay(query, results, doc));
        
        return elt;
        
    }

    private static Element getSearchBox(String query, Document doc) {

        Element retVal = HTMLGeneratorToolbox.getForm(Targets.TEST.getUrl(), false, doc);
        
        retVal.appendChild(HTMLGeneratorToolbox.getLabeledInput("הרשת העצבית אומנה בארבע קטגוריות: צבא וביטחון, כדורגל, סלבס וטכנולוגיה. הקלד כותרת חדשות באחת הקטגוריות הללו:", query, FormFields.QUERY.getName(), false, true, doc));
        
        Element elt = HTMLGeneratorToolbox.getSearchButton(doc);
        elt.setAttribute("value", "נסה");
        elt.setAttribute("style", "display:block; clear:both; float:right; margin-bottom:20px;");
        retVal.appendChild(elt);
        
        return retVal;
    
    }
    
    private static Element getResultDisplay(String query, HashMap<String, String> results, Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getDiv(doc);
        
        Element elt = HTMLGeneratorToolbox.getBlockSpan("הערכה עבור הכותרת:", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getBlockSpan(query, doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right; margin-right: 20px; font-weight: bold; font-style: italic;");
        retVal.appendChild(elt);
        
        Element table = HTMLGeneratorToolbox.getTable(doc);
        table.setAttribute("style", "display:block; clear:both; float:right; margin-top: 20px; width:100%; max-width:100%; border-collapse:collapse; table-layout:fixed; ");
        table.setAttribute("cellpadding", "10");
        for (Map.Entry<String, String> entry : results.entrySet()) {
            table.appendChild(getRow(entry, doc));
        }
        retVal.appendChild(table);
        
        return retVal;
        
    }
    
    private static Element getRow(Map.Entry<String, String> entry, Document doc) {
        
        ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
        content.add(getCell(entry.getKey(), "right"));
        content.add(getCell(entry.getValue(), "right"));

        return HTMLGeneratorToolbox.getTableRow(content, doc);

    }
    
    private static Map.Entry<String, String> getCell(String text, String alignment) {
        return new AbstractMap.SimpleEntry(text, alignment);
    }
    
}
