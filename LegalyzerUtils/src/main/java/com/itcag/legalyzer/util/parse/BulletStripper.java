package com.itcag.legalyzer.util.parse;

/**
 * Strips off bullets at the beginning of a paragraph.
 * Recognizes the following bullet types:
 *      - one or more digits followed by a period and a white space character,
 *      - one or more digits followed by the closing round parenthesis and a white space character,
 *      - a single letter followed by a period and a white space character, and
 *      - a single letter followed by the closing round parenthesis and a white space character.
 * The maximum length of a bullet (including the white space character) cannot exceed 5.
 */
public class BulletStripper {
    
    public final static String stripOffBullet(String line) {

        line = line.trim();
        
        boolean digit = false;
        boolean letter = false;
        
        boolean symbol = false;
        
        for (int i = 0; i < 5; i++) {
            
            char c = line.charAt(i);
            
            if (Character.isDigit(c)) {
                if (!symbol && !letter) {
                    digit = true;
                } else {
                    return line;
                }
            } else if (Character.isLetter(c)) {
                if (!symbol && !letter && !digit) {
                    digit = true;
                } else {
                    return line;
                }
            } else if (".".equals(String.valueOf(c))) {
                if ((digit || letter) && !symbol) {
                    symbol = true;
                } else {
                    return line;
                }
            } else if (")".equals(String.valueOf(c))) {
                if ((digit || letter) && !symbol) {
                    symbol = true;
                } else {
                    return line;
                }
            } else if (Character.isWhitespace(c)) {
                if (symbol) return line.substring(i + 1).trim();
            } else {
                return line;
            }
            
        }
     
        return line;
        
    }
    
    public static void main(String[] args) throws Exception {
        System.out.println(BulletStripper.stripOffBullet("1.    הנאשם הורשע, על פי הודאתו בעובדות כתב אישום מתוקן, במסגרת הסדר דיוני, בעבירה של סיוע לייבוא סם מסוכן, לפי סעיף 13 ביחד עם סעיף 19א לפקודת הסמים המסוכנים [נוסח חדש], התשל\"ג-1973 [פקודת הסמים], ביחד עם סעיף 31 לחוק העונשין, התשל\"ז-1977 [חוק העונשין]."));
    }

}
