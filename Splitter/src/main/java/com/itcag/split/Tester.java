package com.itcag.split;

import java.util.ArrayList;

public class Tester {

    public static void main(String[] args) throws Exception {
        
        processSentence();
        
    }
    
    private static void processSentence() throws Exception {

        String text = "Sen. Jeff Flake, R-Ariz., who has repeatedly battled with Trump and will retire in January, said he, too, planned to vote for Kavanaugh's confirmation.";

        Protocol protocol = new Protocol();
        ArrayList<StringBuilder> sentences = protocol.execute(new StringBuilder(text));
        sentences.stream().forEach((sentence) -> { System.out.println(sentence + "\n"); });

    }
    
}
