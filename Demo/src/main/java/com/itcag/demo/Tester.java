package com.itcag.demo;

import com.itcag.datatier.SearchIndex;
import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.DocumentFields;
import com.itcag.legalyzer.Legalyzer;
import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.parse.ParserFields;
import com.itcag.util.Encoder;
import java.util.ArrayList;
import java.util.Properties;
import org.json.JSONArray;
import org.json.JSONObject;


public class Tester {
    
    public static ArrayList<String> getLines() {
        ArrayList<String> lines = new ArrayList();
        lines.add("בבית המשפט העליון");
        lines.add("ע\"א 7054/16 - כ\"ו");
        lines.add("לפני:");
        lines.add("כבוד הרשמת שרית עבדיאן");
        lines.add("המערער:");
        lines.add("עמוס שנהר");
        lines.add("נ ג ד");
        lines.add("המשיבים:");
        lines.add("1. יניב אינסל ,עו\"ד");
        lines.add("2. כלל חברה לביטוח בע\"מ");
        lines.add( "3. כונס הנכסים הרשמי");
        lines.add( "בקשה למחיקת הערעור");
        lines.add("פסק-דין");
        lines.add( "כמבוקש, אני מורה על מחיקת ההליך שבכותרת.");
        lines.add("הדיון הקבוע ליום 9.3.2020 מבוטל.");
        lines.add("המזכירות תשיב למערער את העירבון בהיעדר מניעה שבדין לכך.");
        lines.add( "פסק הדין יובא לידיעת יומן בית המשפט.");
        lines.add( "ניתן היום, ‏ב' באלול התשע\"ט (‏2.9.2019).");
        lines.add("שרית עבדיאן");
        lines.add("ר ש מ ת");
        lines.add("_________________________");
        lines.add( "16070540_D13.docx מש");
        lines.add("מרכז מידע, טל' 077-2703333 ; אתר אינטרנט, http://supreme.court.gov.il");
        return lines;
    }

    public static JSONObject getObject() {

        JSONObject obj = new JSONObject();
        obj.put("id", "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts%5C%5C%5C%5C%5C%5C%5C%5C16%5C%5C%5C%5C%5C%5C%5C%5C540%5C%5C%5C%5C%5C%5C%5C%5C070%5C%5C%5C%5C%5C%5C%5C%5Cd13&fileName=16070540.D13&type=2");
        JSONArray lines = new JSONArray();
        lines.put("בבית המשפט העליון");
        lines.put("ע\"א 7054/16 - כ\"ו");
        lines.put("לפני:");
        lines.put("כבוד הרשמת שרית עבדיאן");
        lines.put("המערער:");
        lines.put("עמוס שנהר");
        lines.put("נ ג ד");
        lines.put("המשיבים:");
        lines.put("1. יניב אינסל ,עו\"ד");
        lines.put("2. כלל חברה לביטוח בע\"מ");
        lines.put( "3. כונס הנכסים הרשמי");
        lines.put( "בקשה למחיקת הערעור");
        lines.put("פסק-דין");
        lines.put( "כמבוקש, אני מורה על מחיקת ההליך שבכותרת.");
        lines.put("הדיון הקבוע ליום 9.3.2020 מבוטל.");
        lines.put("המזכירות תשיב למערער את העירבון בהיעדר מניעה שבדין לכך.");
        lines.put( "פסק הדין יובא לידיעת יומן בית המשפט.");
        lines.put( "ניתן היום, ‏ב' באלול התשע\"ט (‏2.9.2019).");
        lines.put("שרית עבדיאן");
        lines.put("ר ש מ ת");
        lines.put("_________________________");
        lines.put( "16070540_D13.docx מש");
        lines.put("מרכז מידע, טל' 077-2703333 ; אתר אינטרנט, http://supreme.court.gov.il");
        obj.put("lines", lines);
        
        return obj;
        
    }
    
    
    
    
    
    
    public static void main(String[] args) throws Exception {
        String s = Encoder.decodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts%5C%5C%5C%5C%5C%5C%5C%5C16%5C%5C%5C%5C%5C%5C%5C%5C540%5C%5C%5C%5C%5C%5C%5C%5C070%5C%5C%5C%5C%5C%5C%5C%5Cd13&fileName=16070540.D13&type=2");
        System.out.println(s);
        
        ArrayList<JSONObject> results = SearchIndex.searchIndex(Indices.COURT_RULINGS.getFieldName(), 0, 1, DocumentFields.id.getFieldName(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\16\\540\\070\\d13&fileName=16070540.D13&type=2");
        System.out.println(results);
    }

    
}
