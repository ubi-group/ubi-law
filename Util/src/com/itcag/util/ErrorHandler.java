package com.itcag.util;

public final class ErrorHandler {
    
    public final static String toString(Throwable ex) {

        StringBuilder retVal = new StringBuilder();
                
        retVal.append(ex.getClass().getName()).append(" \n");

        retVal.append(" \t").append(ex.getMessage()).append(" \n");

        retVal.append("Stack trace:").append(" \n");

        for (StackTraceElement stackTraceElement : ex.getStackTrace()) {
            retVal.append(" \t").append(stackTraceElement.toString()).append(" \n");
        }

        return retVal.toString();

    }

    public final static void print(Throwable ex) {
        print(toString(ex));
    }
    
    private static void print(String msg) {
        Printer.print(msg);
    }

}
