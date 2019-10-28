package com.itcag.legalyzer.util.parse;

/**
 * Removes quotes unless:
 *      - they mark acronym/abbreviation, or
 *      - they are used as diacritics to soften Hebrew letters.
 */
public class QuoteRemover {
    
    public final static String removeQuotes(String line) throws Exception {
        
        String retVal = removeDouble(line);
        retVal = removeSingle(retVal);
        
        return retVal;
        
    }
    
    private static String removeDouble(String line) throws Exception {
        
        int pos = line.indexOf("\"");
        while (pos > -1) {
            
            Character pre = null;
            if (pos > 0) pre = line.charAt(pos - 1);
            
            Character post = null;
            if (pos < line.length() - 2)  post = line.charAt(pos + 1);
            
            Character postPost = null;
            if (pos < line.length() - 3)  postPost = line.charAt(pos + 2);
            
            if (pre == null) {
                line = line.substring(pos + 1);
            } else if (post == null) {
                line = line.substring(0, pos) + line.substring(pos + 1);
            } else if (postPost == null) {
                if (!Character.isLetter(post)) {
                    line = line.substring(0, pos) + line.substring(pos + 1);
                } else {
                    pos++;
                }
            } else if (!Character.isLetter(post) || Character.isLetter(postPost)) {
                line = line.substring(0, pos) + line.substring(pos + 1);
            } else {
                pos++;
            }
            
            pos = line.indexOf("\"", pos);
            
        }
        
        return line;
        
    }
    
    private static String removeSingle(String line) throws Exception {
        
        int pos = line.indexOf("'");
        while (pos > -1) {
            
            Character pre = null;
            if (pos > 0) pre = line.charAt(pos - 1);
            
            if (pre == null) {
                line = line.substring(pos + 1);
            } else if ("ג".equals(String.valueOf(pre)) || "ז".equals(String.valueOf(pre)) || "צ".equals(String.valueOf(pre)) || "ץ".equals(String.valueOf(pre))) {
                pos++;
            } else {
                line = line.substring(0, pos) + line.substring(pos + 1);
            }
            
            pos = line.indexOf("'", pos);
            
        }
        
        return line;
        
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(QuoteRemover.removeQuotes("הנאשם הורשע, על פי הודאתו ב\"עובדות כתב אישום מתוקן\", ב'מסגרת הסדר דיוני', בעבירה של סיוע לייבוא סם מסוכן, לפי סעיף 13 ביחד עם סעיף 19א לפקודת הסמים המסוכנים [נוסח חדש], התשל\"ג-1973 [פקודת הסמים], ביחד עם סעיף 31 לחוק העונשין, התשל\"ז-1977 [חוק העונשין]."));
    }

}
