package com.itcag.split;

import com.itcag.util.Converter;
import com.itcag.util.txt.TextToolbox;

public final class TextProcessingInstructions {

    public final static int DEFAULT = 0;
    public final static int DETACH_PUNCTUATION = 2;
    public final static int FIRST_PARAGRAPH_ONLY = 4;
    public final static int REMOVE_DATELINE = 8;
    public final static int VERIFY_LANGUAGE = 16;
    public final static int REJECT_IF_LANGUAGE_NULL = 32;
    public final static int EXTRACT_HASHTAGS = 64;
    public final static int REMOVE_HASHTAGS = 128;
    public final static int SEPARATE_CONCATENATIONS = 256;
    public final static int REMOVE_FOOTNOTES = 512;
    public final static int SPLIT_LISTS = 1024;
    public final static int SPLIT_ON_CAPS = 2048;

    private final static String PRE = "[[[";
    private final static String POST = "]]]";

    private final int instructions;
    private final String text;
    
    public final static String getFlag(int textProcessingInstruction) {
        return PRE + Integer.toString(textProcessingInstruction) + POST;
    }

    public TextProcessingInstructions(String text) {
        
        if (text == null) throw new IllegalArgumentException("Input is empty");
        
        text = text.trim();
        if (text.isEmpty()) throw new IllegalArgumentException("Input is empty");
        
        if (!text.startsWith(PRE)) {

            this.instructions = 0;
            this.text = text;

        } else {
            
            int start = 0;
            int end = text.indexOf(POST);

            if (end == -1) {
                
                this.instructions = 0;
                this.text = text;
            
            } else {
                
                end += POST.length();
                
                this.text = text.substring(end);

                String flag = text.substring(start, end);
                flag = TextToolbox.replace(flag, PRE, "");
                flag = TextToolbox.replace(flag, POST, "");

                Integer test = Converter.convertStringToInteger(flag);
                if (test == null) {
                    this.instructions = 0;
                } else {
                    this.instructions = test;
                }

            }

        }
        
    }

    public int getInstructions() {
        return instructions;
    }

    public boolean isInstruction(int instruction) {
        return (instructions & instruction) == instruction;
    }
    
    public String getText() {
        return text;
    }
    
}
