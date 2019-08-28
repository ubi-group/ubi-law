package com.itcag.util;

/**
 * Generates a generic error message returned whenever something goes wrong.
 * @author IT Consulting Alicja Grużdź
 */
public final class ErrorMessage {
    
    public final static String getStandardMessage(String exceptionMessage) {
        StringBuilder msg = new StringBuilder();
        msg.append("Sorry ma'm, I am a teapot and cannot brew coffee: ");
        msg.append(exceptionMessage);
        msg.append(" Please, do not submit more requests until this bug is fixed. Thanks.");
        return msg.toString();
    }
    
}
