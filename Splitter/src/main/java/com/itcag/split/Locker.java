package com.itcag.split;

import com.itcag.split.url.LinkExtractor;
import com.itcag.split.url.LinkSpan;
import com.itcag.split.url.LinkType;
import com.itcag.util.txt.TextToolbox;

import java.util.EnumSet;

public final class Locker {

    public final void lock(StringBuilder input) throws Exception {
        
        lockURL(input);
        
        lockPeriods(input);
        lockColons(input);
        lockCommas(input);
        
        input.append(" ");
        
    }

    private void lockPeriods(StringBuilder input) {
        int start = input.indexOf(".");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Constants.PERIOD);
            } else if (Character.isLetter(c)) {
                lockAcronym(input, start);
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockColons(StringBuilder input) {
        /**
         * This is to preserve the time.
         */
        int start = input.indexOf(":");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) {
                input.replace(start, start + 1, Constants.COLON);
            }
            start = input.indexOf(".", start + 1);
        }
    }
    
    private void lockCommas(StringBuilder input) {
        int start = input.indexOf(",");
        while (start > -1 && start < input.length() - 1) {
            char c = input.charAt(start + 1);
            if (Character.isDigit(c)) input.replace(start, start + 1, Constants.COMMA);
            start = input.indexOf(",", start + 1);
        }
    }
    
    private void lockURL(StringBuilder input) {

        LinkExtractor linkExtractor = LinkExtractor.builder().linkTypes(EnumSet.of(LinkType.URL, LinkType.WWW, LinkType.EMAIL)).build();
        Iterable<LinkSpan> links = linkExtractor.extractLinks(input);
        while (links.iterator().hasNext()) {
            LinkSpan link = links.iterator().next();
            String original = input.substring(link.getBeginIndex(), link.getEndIndex());
            if (original.endsWith(".")) {
                original = original.substring(0, original.length() - 2);
            }
            String replacement = encode(original);
            TextToolbox.replace(input, original, replacement);
        }

    }
    
    private void lockAcronym(StringBuilder input, int start) {
        
        int dot = start;
        int letter = start - 1;
        for (int i = start; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == 46) {
                if (i == letter + 1) {
                    dot = i;
                    if (i == (input.length() - 1) && i >= start + 2) {
                        TextToolbox.replaceWithin(input, input.substring(start, i + 1), ".", Constants.PERIOD);
                        input.append(".");
                    }
                } else {
                    break;
                }
            } else if (Character.isLetter(c)) {
                if (i == dot + 1) {
                    letter = i;
                } else {
                    break;
                }
            } else {
                if (i > start + 2) {
                    TextToolbox.replaceWithin(input, input.substring(start, i), ".", Constants.PERIOD);
                }
                break;
            }
        }
        
    }
    
    public final void unlock(StringBuilder input) throws Exception {
        decode(input);
    }
    
    private String encode(String input) {

        String retVal = input;

        retVal = TextToolbox.replace(retVal, ".", Constants.PERIOD);
        retVal = TextToolbox.replace(retVal, "!", Constants.EXCLAMATION);
        retVal = TextToolbox.replace(retVal, "?", Constants.QUESTION);
        retVal = TextToolbox.replace(retVal, "-", Constants.HYPHEN);
        retVal = TextToolbox.replace(retVal, ":", Constants.COLON);
        retVal = TextToolbox.replace(retVal, ";", Constants.SEMICOLON);
        retVal = TextToolbox.replace(retVal, "/", Constants.SLASH);

        return retVal;

    }
    
    private void decode(StringBuilder input) {
        
        TextToolbox.replace(input, Constants.PERIOD, ".");
        TextToolbox.replace(input, Constants.EXCLAMATION, "!");
        TextToolbox.replace(input, Constants.QUESTION, "?");
        TextToolbox.replace(input, Constants.COLON, ":");
        TextToolbox.replace(input, Constants.SEMICOLON, ";");
        TextToolbox.replace(input, Constants.COMMA, ",");
        TextToolbox.replace(input, Constants.SLASH, "/");
        TextToolbox.replace(input, Constants.HYPHEN, "-");
        
    }
    
}
