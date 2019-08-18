package com.itcag.util.txt;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Collection;

public final class TextToolbox {

    public final static boolean isEmpty(String input) {
        return input == null || input.isEmpty();
    }
    
    public final static boolean isReallyEmpty(String input) {
        if (input == null) return true;
        input = input.trim();
        return input.isEmpty();
    }
    
    public final static boolean containsCaIn(String text, String searchStr)  {
        return text.toLowerCase().contains(searchStr.toLowerCase());
    }

    public final static boolean startsWithCaIn(String text, String searchStr)  {
        return text.toLowerCase().startsWith(searchStr.toLowerCase());
    }

    public final static boolean endsWithCaIn(String text, String searchStr)  {
        return text.toLowerCase().endsWith(searchStr.toLowerCase());
    }

    public final static int indexOfCaIn(String text, String searchStr)  {
        return text.toLowerCase().indexOf(searchStr.toLowerCase());
    }

    public final static int lastIndexOfCaIn(String text, String searchStr)  {
        return text.toLowerCase().lastIndexOf(searchStr.toLowerCase());
    }

    public final static String replace(String text, String searchString, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(searchString) || replacement == null) {
            return text;
        }
        int start = 0;
        int end = text.indexOf(searchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            end = text.indexOf(searchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    public final static String replaceCaIn(String text, String searchString, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(searchString) || replacement == null) {
            return text;
        }
        String lowerCaseText = text.toLowerCase();
        String lowerCaseSearchString = searchString.toLowerCase();
        int start = 0;
        int end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = searchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end)).append(replacement);
            start = end + replLength;
            end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    public final static String replaceWithin(String text, String primarySearchString, String secondarySearchString, String replacement) {
        if (TextToolbox.isEmpty(text) || TextToolbox.isEmpty(primarySearchString) || replacement == null) {
            return text;
        }
        String lowerCaseText = text.toLowerCase();
        String lowerCaseSearchString = primarySearchString.toLowerCase();
        int start = 0;
        int end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        if (end == -1) {
            return text;
        }
        int replLength = primarySearchString.length();
        int increase = replacement.length() - replLength;
        increase = (increase < 0 ? 0 : increase);
        increase *= 64;
        StringBuilder stringBuilder = new StringBuilder(text.length() + increase);
        while (end != -1) {
            stringBuilder.append(text.substring(start, end));
            String excerpt = text.substring(end, end + replLength);
            excerpt = replace(excerpt, secondarySearchString, replacement);
            stringBuilder.append(excerpt);
            start = end + replLength;
            end = lowerCaseText.indexOf(lowerCaseSearchString, start);
        }
        stringBuilder.append(text.substring(start));
        return stringBuilder.toString();
    }

    public final static boolean isEmpty(StringBuilder input) {
        return input == null || input.length() == 0;
    }

    public final static void trim(StringBuilder text) {

        if (isEmpty(text)) return;

        while (Character.isWhitespace(text.charAt(0))) {
            text.deleteCharAt(0);
            if (isEmpty(text)) return;
        }
        
        if (isEmpty(text)) return;

        while (Character.isWhitespace(text.charAt(text.length() - 1))) {
            text.deleteCharAt(text.length() - 1);
            if (isEmpty(text)) return;
        }
        
    }

    public final static int indexOfCaIn(StringBuilder text, String searchString) {

        if (isEmpty(text) || TextToolbox.isEmpty(searchString)) {
            return -1;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        return lowerCaseText.indexOf(searchString.toLowerCase());
        
    }
    
    public final static int indexOfCaIn(StringBuilder text, String searchString, int start) {

        if (isEmpty(text) || TextToolbox.isEmpty(searchString)) {
            return -1;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        return lowerCaseText.indexOf(searchString.toLowerCase(), start);
        
    }
    
    public final static void replace(StringBuilder text, String searchString, String replacement) {

        if (isEmpty(text) || TextToolbox.isEmpty(searchString) || replacement == null) {
            return;
        }

        int start = text.indexOf(searchString, 0);
        if (start == -1) return;

        while (start != -1) {
            int end = start + searchString.length();
            text.replace(start, end, replacement);
            end = start + replacement.length();
            start = text.indexOf(searchString, end);
        }

    }

    public final static void replaceCaIn(StringBuilder text, String searchString, String replacement) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(searchString) || replacement == null) {
            return;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());

        String lowerCaseSearchString = searchString.toLowerCase();
        
        int start = lowerCaseText.indexOf(lowerCaseSearchString);

        if (start == -1) return;

        int length = searchString.length();
        
        while (start != -1) {
            
            int end = start + length;
            
            text.replace(start, end, replacement);
            lowerCaseText.replace(start, end, replacement);

            end = start + replacement.length();
            start = lowerCaseText.indexOf(lowerCaseSearchString, end);
        
        }

    }

    /*
     * Searches for primary search string within text.
     * If it finds it, it searches within it
     * for the secondary search string, and replaces it.
     * For example:
     *    text = "I live in the U.S.A."
     *    primarySearchString = "U.S.A"
     *    secondarySearchString = "."
     *    replacement = ""
     * Outcome: "I live in the USA."
     */
    public final static void replaceWithin(StringBuilder text, String primarySearchString, String secondarySearchString, String replacement) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(primarySearchString) || replacement == null) {
            return;
        }
        
        StringBuilder lowerCaseText = new StringBuilder(text.length());
        lowerCaseText.append(text.toString().toLowerCase());
        
        String lowerCaseSearchString = primarySearchString.toLowerCase();

        int start = lowerCaseText.indexOf(lowerCaseSearchString);
        if (start == -1) return;

        int length = primarySearchString.length();
        
        while (start != -1) {
            
            int end = start + length;
            
            String excerpt = text.substring(start, end);
            excerpt = replace(excerpt, secondarySearchString, replacement);
            
            text.replace(start, end, excerpt);
            lowerCaseText.replace(start, end, excerpt);
            
            start = lowerCaseText.indexOf(lowerCaseSearchString, end);
        
        }
    
    }

    public final static void remove(StringBuilder text, String searchString) {

        if (isEmpty(text) || TextToolbox.isEmpty(searchString)) return;
        
        int start = text.indexOf(searchString, 0);
        
        while (start != -1) {
            int end = start + searchString.length();
            text.replace(start, end, "");
            start = text.indexOf(searchString, start);
        }
        
    }

    public final static String removeParentheses(String text, String left, String right) {
        if (TextToolbox.isEmpty(text)) return text;
        if (!text.contains(left)) return text;
        if (!text.contains(right)) return text;
        StringBuilder stringBuilder = new StringBuilder(text.length());
        while (text.contains(left)) {
            int start = text.indexOf(left);
            int end = text.indexOf(right);
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

    public final static void removeParentheses(StringBuilder text, String left, String right) {
        
        if (isEmpty(text) || TextToolbox.isEmpty(left) || TextToolbox.isEmpty(right)) {
            return;
        }
        
        if (text.indexOf(left) == -1) return;
        if (text.indexOf(right) == -1) return;

        int start = text.indexOf(left);
        
        while (start != -1) {
            int end = text.indexOf(right, start);
            if (end == -1) break;
            end++;
            if (end > start) text.replace(start, end, " ");
            start = text.indexOf(left, start + 1);
        }
        
    }

    public final static int getLevenshteinDistance(String firstString, String secondString) {
        return LevenshteinDistance.LevenshteinDistance(firstString, secondString);
    }
    
    public final static String joinWithDelimiter(Collection<String> collection, String delimiter) {
        String retVal = "";
        for (String item : collection) {
            if (retVal.isEmpty()) {
                retVal = item;
            } else {
                retVal += delimiter + item;
            }
        }
        return retVal;
    }
    
    public final static boolean isCapitalized(String input) {
        if (isReallyEmpty(input)) return false;
        return Character.isUpperCase(input.charAt(0));
    }
    
    public final static String capitalize(String input) {
        if (isEmpty(input)) return input;
        input = input.toLowerCase();
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public final static void capitalize(StringBuilder input) {
        if (isEmpty(input)) return;
        for (int i = 0; i < input.length(); i++) {
           char c = input.charAt(i);
           input.setCharAt(i, Character.toLowerCase(c));
        }
        input.replace(0, 1, input.substring(0, 1).toUpperCase());
    }

    public final static String repeat(int times, String toRepeat) {
        if (times < 1) return "";
        if (times == 1) return toRepeat;
        StringBuilder retVal = new StringBuilder(times * toRepeat.length() + 1);
        for (int i = 0; i < times; i++) {
            retVal.append(toRepeat);
        }
        return retVal.toString();
    }
    
    public final static String removeDiacritics(String text) {
        if (isEmpty(text)) return text;
        return Normalizer.normalize(text, Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public final static String defaultString(String text) {
        if (isReallyEmpty(text)) return "";
        return text;
    }
    
}
