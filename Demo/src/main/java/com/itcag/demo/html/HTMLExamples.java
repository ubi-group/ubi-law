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

public class HTMLExamples {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        
        Element head = HTMLGeneratorToolbox.getHead(Targets.EXAMPLES.getTitle(), WebConstants.VERSION, doc);
        Element subElt = HTMLGeneratorToolbox.getHebrewFont(doc);
        head.appendChild(subElt);
        root.appendChild(head);

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        elt.setAttribute("style", "font-family: 'Varela Round', sans-serif; padding: 20px; ");
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc);
        elt.appendChild(breadcrumbs);

        Element subElt = HTMLGeneratorToolbox.getTitle(Targets.EXAMPLES.getTitle(), doc);
        subElt.setAttribute("style", "clear:both; float:right;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getParagraph("בנושאים רחבים ומגוונים, כגון כותרות החדשות, אין לצפות שקטגוריות תהיינה לגמרי נבדלות זו מזו. כותרת מסוימת יכולה להשתייך למספר קטגוריות משום שהיא מתייחסת למספר נושאים. אולם הרשת העצבית מכויילת בדרך המאפשרת לה לזהות מקרים כאלה, ולשייך את הכותרת בו זמנית לשתי קטגוריות.", doc);
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getParagraph("להלן מספר דוגמאות:", doc);
        elt.appendChild(subElt);
        
        elt.appendChild(getExample_1(doc));
        elt.appendChild(getExample_2(doc));
        elt.appendChild(getExample_3(doc));
        elt.appendChild(getExample_4(doc));

        return elt;
        
    }

    private static Element getExample_1(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getDiv(doc);
        retVal.setAttribute("style", "clear:both; display:block; float:right; width:100%; margin-bottom:60px;");
        
        Element elt = HTMLGeneratorToolbox.getBlockSpan("כותרת:", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getBlockSpan("צילום הלוויין חושף: המחסן המסתורי במרכז המחקר שהושמד בסוריה", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right; margin-right: 20px; font-weight: bold; font-style: italic;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getParagraph(null, doc);

        Element subElt = HTMLGeneratorToolbox.getInlineSpan("שייכת הן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getInlineSpan("טכנולוגיה ", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("והן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("צבא וביטחון.", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        retVal.appendChild(elt);
        
        retVal.appendChild(getTable_1(doc));
        
        return retVal;
        
    }
    
    private static Element getTable_1(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getTable(doc);
        retVal.setAttribute("style", "display:block; clear:both; float:right; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        retVal.setAttribute("cellpadding", "10");
        retVal.setAttribute("dir", "ltr");
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("1.2828307151794434", "right"));
            content.add(getCell("צבא וביטחון", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("4.527286218944937E-4", "right"));
            content.add(getCell("כדורגל", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("0.02607514150440693", "right"));
            content.add(getCell("סלבס", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("7.32424259185791", "right"));
            content.add(getCell("טכנולוגיה", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        return retVal;
        
    } 
    
    
    private static Element getExample_2(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getDiv(doc);
        retVal.setAttribute("style", "clear:both; display:block; float:right; width:100%; margin-bottom:60px;");
        
        Element elt = HTMLGeneratorToolbox.getBlockSpan("כותרת:", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getBlockSpan("הדרת נשים: שרית חדד לא תופיע בפני חיילי גבעתי", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right; margin-right: 20px; font-weight: bold; font-style: italic;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getParagraph(null, doc);

        Element subElt = HTMLGeneratorToolbox.getInlineSpan("שייכת הן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getInlineSpan("סלבס ", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("והן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("צבא וביטחון.", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        retVal.appendChild(elt);
        
        retVal.appendChild(getTable_2(doc));
        
        return retVal;
        
    }
    
    private static Element getTable_2(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getTable(doc);
        retVal.setAttribute("style", "display:block; clear:both; float:right; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        retVal.setAttribute("cellpadding", "10");
        retVal.setAttribute("dir", "ltr");
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("1.3295598030090332", "right"));
            content.add(getCell("צבא וביטחון", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("0.5671847462654114", "right"));
            content.add(getCell("כדורגל", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("6.496984958648682", "right"));
            content.add(getCell("סלבס", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("0.8543418049812317", "right"));
            content.add(getCell("טכנולוגיה", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        return retVal;
        
    } 
    
    private static Element getExample_3(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getDiv(doc);
        retVal.setAttribute("style", "clear:both; display:block; float:right; width:100%; margin-bottom:60px;");
        
        Element elt = HTMLGeneratorToolbox.getBlockSpan("כותרת:", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getBlockSpan("אלונה ברקת התפטרה מתפקידיה בהתאחדות", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right; margin-right: 20px; font-weight: bold; font-style: italic;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getParagraph(null, doc);

        Element subElt = HTMLGeneratorToolbox.getInlineSpan("שייכת הן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getInlineSpan("סלבס ", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("והן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("כדורגל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        retVal.appendChild(elt);
        
        retVal.appendChild(getTable_3(doc));
        
        return retVal;
        
    }
    
    private static Element getTable_3(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getTable(doc);
        retVal.setAttribute("style", "display:block; clear:both; float:right; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        retVal.setAttribute("cellpadding", "10");
        retVal.setAttribute("dir", "ltr");
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("5.134960520081222E-4", "right"));
            content.add(getCell("צבא וביטחון", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("1.0228948593139648", "right"));
            content.add(getCell("כדורגל", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("3.99676775932312", "right"));
            content.add(getCell("סלבס", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("9.669674909673631E-5", "right"));
            content.add(getCell("טכנולוגיה", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        return retVal;
        
    } 
    
    private static Element getExample_4(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getDiv(doc);
        retVal.setAttribute("style", "clear:both; display:block; float:right; width:100%; margin-bottom:60px;");
        
        Element elt = HTMLGeneratorToolbox.getBlockSpan("כותרת:", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getBlockSpan("רמי לוי נכנס לשוק הטלוויזיה; יציע תכנים של הוט ב-89 שקלים לחודש", doc);
        elt.setAttribute("style", "display:block; width:100%; clear:both; float:right; margin-right: 20px; font-weight: bold; font-style: italic;");
        retVal.appendChild(elt);
        
        elt = HTMLGeneratorToolbox.getParagraph(null, doc);

        Element subElt = HTMLGeneratorToolbox.getInlineSpan("שייכת הן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);
        
        subElt = HTMLGeneratorToolbox.getInlineSpan("סלבס ", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("והן ל", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: normal;");
        elt.appendChild(subElt);

        subElt = HTMLGeneratorToolbox.getInlineSpan("טכנולוגיה", 0, 0, doc);
        subElt.setAttribute("style", "font-weight: bold;");
        elt.appendChild(subElt);

        retVal.appendChild(elt);
        
        retVal.appendChild(getTable_4(doc));
        
        return retVal;
        
    }
    
    private static Element getTable_4(Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getTable(doc);
        retVal.setAttribute("style", "display:block; clear:both; float:right; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        retVal.setAttribute("cellpadding", "10");
        retVal.setAttribute("dir", "ltr");
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("0.0011597592383623123", "right"));
            content.add(getCell("צבא וביטחון", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("2.7817790508270264", "right"));
            content.add(getCell("כדורגל", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("6.096650123596191", "right"));
            content.add(getCell("סלבס", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        {
            ArrayList<Map.Entry<String, String>> content = new ArrayList<>();
            content.add(getCell("4.2400898933410645", "right"));
            content.add(getCell("טכנולוגיה", "right"));
            retVal.appendChild(HTMLGeneratorToolbox.getTableRow(content, doc));
        }
        
        return retVal;
        
    } 
    
    private static Map.Entry<String, String> getCell(String text, String alignment) {
        return new AbstractMap.SimpleEntry(text, alignment);
    }
    
}
