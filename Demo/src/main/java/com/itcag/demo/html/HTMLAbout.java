package com.itcag.demo.html;

import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLAbout {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        
        Element head = HTMLGeneratorToolbox.getHead(Targets.ABOUT.getTitle(), WebConstants.VERSION, doc, null);
        Element subElt = HTMLGeneratorToolbox.getHebrewFont(doc);
        head.appendChild(subElt);
        root.appendChild(head);

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        elt.setAttribute("style", "font-family: 'Varela Round', sans-serif; padding: 20px; ");
        
        Element subElt = HTMLGeneratorToolbox.getTitle(Targets.ABOUT.getTitle(), doc);
        subElt.setAttribute("style", "clear:both; float:right;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getParagraph("דמו זה נועד להדגים את היכולת לעבד בהצלחה טקסט חופשי בשפה העברית.", doc);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph(null, doc);
        Element subSubElt = HTMLGeneratorToolbox.getInlineSpan("למטרה זו עובדו כ-20,000 כותרות מהארכיון של האתר וולה (", 0, 0, doc);
        subElt.appendChild(subSubElt);
        subSubElt = HTMLGeneratorToolbox.getInlineLink("https://www.walla.co.il/archive", "www.walla.co.il/archive", doc);
        subSubElt.setAttribute("style", "margin: 0;");
        subElt.appendChild(subSubElt);
        subSubElt = HTMLGeneratorToolbox.getInlineSpan("). הכותרות חולקו לארבע קטגוריות: צבא וביטחון, כדורגל, סלבס וטכנולוגיה.", 0, 0, doc);
        subElt.appendChild(subSubElt);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph("הכותרות שימשו קבוצת אימון עבור רשת עצבית רקורסיבית בעלת מספר שכבות מוסתרות. שימוש ברשתות כאלה מכונה \"למידה עמוקה\" (\"deep learning\").", doc);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph(null, doc);
        subSubElt = HTMLGeneratorToolbox.getInlineSpan("כדי לנסות את הרשת העצבית לחץ", 0, 0, doc);
        subElt.appendChild(subSubElt);
        subSubElt = HTMLGeneratorToolbox.getInlineLink(Targets.TEST.getUrl(), "כאן", doc);
        subSubElt.setAttribute("style", "margin-right: 5px;");
        subElt.appendChild(subSubElt);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph("על מנת להזין רשת עצבית בטקסט, יצרנו הטבעה מילולית (\"word embedding\") עברית - הראשונה מסוגה למיטב ידיעתנו. למטרה זו השתמשנו בויקיפדיה העברית בכללותה (234,907 מילים ייחודיות). בכוונתינו לאפשר הורדה חופשית של ההטבעה המילולית העברית, על מנת לעזור לגורמים אחרים המעוניינים להשתמש ברשתות עצביות לעיבוד השפה העברית.", doc);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph("להלן הסטטיסטיקה המאפיינת את המערכת אחורי הדמו הזה:", doc);
        elt.appendChild(subElt);
        
        elt.appendChild(getTable(doc));
        
        subElt = HTMLGeneratorToolbox.getParagraph(null, doc);
        subSubElt = HTMLGeneratorToolbox.getInlineSpan("על מנת להבין מדוע לא ניתן לשפר דיוק, ראה ", 0, 0, doc);
        subElt.appendChild(subSubElt);
        subSubElt = HTMLGeneratorToolbox.getInlineLink(Targets.EXAMPLES.getUrl(), "דוגמאות רב משמשעותיות", doc);
        subSubElt.setAttribute("style", "margin: 0;");
        subElt.appendChild(subSubElt);
        subSubElt = HTMLGeneratorToolbox.getInlineSpan(".", 0, 0, doc);
        subElt.appendChild(subSubElt);
        elt.appendChild(subElt);
        
        return elt;
        
    }

    private static Element getTable(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getTable(doc);
        retVal.setAttribute("style", "display:block; clear:both; float:right; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        retVal.setAttribute("cellpadding", "10");
        retVal.setAttribute("dir", "ltr");
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("Accuracy:", "left"));
            content.add(getCell("0.9646", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("Precision:", "left"));
            content.add(getCell("0.9676", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("Recall:", "left"));
            content.add(getCell("0.9614", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("F1 Score:", "left"));
            content.add(getCell("0.9645", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        return retVal;
        
    } 
    
    private static Map.Entry<String, String> getCell(String text, String alignment) {
        return new AbstractMap.SimpleEntry(text, alignment);
    }
    
}
