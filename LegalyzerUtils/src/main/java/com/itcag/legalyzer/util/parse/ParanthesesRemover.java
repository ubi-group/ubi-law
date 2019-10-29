package com.itcag.legalyzer.util.parse;

import com.itcag.util.txt.TextToolbox;

/**
 *
 */
public class ParanthesesRemover {

    public final static String removeParantheses(String line) throws Exception {
        
//        String retVal = removeRoundParantheses(line);
        String retVal = TextToolbox.removeParentheses(line, "(", ")");
        retVal = TextToolbox.removeParentheses(retVal, "[", "]");
        retVal = TextToolbox.removeParentheses(retVal, "{", "}");
        retVal = TextToolbox.removeParentheses(retVal, "<", ">");
    
        while (retVal.contains("  ")) retVal = retVal.replace("  ", " ");
        
        return retVal;
    
    }
    
    private static String removeRoundParantheses(String text) throws Exception {
        
        
        StringBuilder stringBuilder = new StringBuilder(text.length());
        while (text.contains("(")) {
            int start = text.indexOf("(");
            int end = text.indexOf(")");
            if (end == -1) break;
            if (end > start) {
                stringBuilder.append(text.substring(0, start));
                stringBuilder.append(" ");
                text = text.substring(end + 1);
            } else {
                stringBuilder.append(text.substring(0, start));
                stringBuilder.append(" ");
                text = text.substring(start + 1);
            }
        }
        stringBuilder.append(text);
        String retVal = stringBuilder.toString();
        return retVal;
        
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(ParanthesesRemover.removeParantheses("הנאשם הורשע, על פי הודאתו בעובדות כתב אישום מתוקן (במסגרת הסדר דיוני) בעבירה של סיוע לייבוא סם מסוכן, לפי סעיף 13 ביחד עם סעיף 19א לפקודת הסמים המסוכנים [נוסח חדש], התשל\"ג-1973 [פקודת הסמים], ביחד עם סעיף 31 לחוק העונשין, התשל\"ז-1977 [חוק העונשין]."));
    }

}
