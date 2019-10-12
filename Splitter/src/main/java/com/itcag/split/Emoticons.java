package com.itcag.split;

import com.itcag.util.txt.TextToolbox;

import java.util.HashSet;

public final class Emoticons {

    private final HashSet<String> emoticons = new HashSet<>();
    
    public Emoticons() throws Exception {
        emoticons.add(" :-{D ");
        emoticons.add(" :-)) ");
        emoticons.add(" :'-( ");
        emoticons.add(" :'-) ");
        emoticons.add(" :-|| ");
        emoticons.add(" :-Þ ");
        emoticons.add(" :-,( ");
        emoticons.add(" :,-( ");
        emoticons.add(" :,( ");
        emoticons.add(" :-þ ");
        emoticons.add(" :^) ");
        emoticons.add(" :^* ");
        emoticons.add(" :-# ");
        emoticons.add(" :-& ");
        emoticons.add(" :o) ");		
        emoticons.add(" :-( ");
        emoticons.add(" :-) ");
        emoticons.add(" :-, ");
        emoticons.add(" :-. ");
        emoticons.add(" :-/ ");
        emoticons.add(" :-< ");
        emoticons.add(" :-D ");
        emoticons.add(" :-J ");
        emoticons.add(" :-O ");
        emoticons.add(" :-P ");
        emoticons.add(" :-X ");
        emoticons.add(" :-[ ");
        emoticons.add(" :-b ");
        emoticons.add(" :-c ");
        emoticons.add(" :'( ");
        emoticons.add(" :') ");
        emoticons.add(" :-o ");
        emoticons.add(" :-p ");
        emoticons.add(" :-| ");
        emoticons.add(" :c) ");
        emoticons.add(" :っ) ");
        emoticons.add(" :っC ");
        emoticons.add(" :þ ");
        emoticons.add(" :# ");
        emoticons.add(" :$ ");
        emoticons.add(" :& ");
        emoticons.add(" :( ");
        emoticons.add(" :) ");
        emoticons.add(" :* ");
        emoticons.add(" :< ");
        emoticons.add(" :> ");
        emoticons.add(" :| ");
        emoticons.add(" :} ");
        emoticons.add(" :Þ ");
        emoticons.add(" ;^) ");
        emoticons.add(" ;-) ");
        emoticons.add(" ;-] ");
        emoticons.add(" ;( ");
        emoticons.add(" ;) ");
        emoticons.add(" O_O ");
        emoticons.add(" O_o ");
        emoticons.add(" o_O ");
        emoticons.add(" >.< ");
        emoticons.add(" >.> ");
        emoticons.add(" <.< ");
        emoticons.add(" >_< ");
        emoticons.add(" >__< ");
        emoticons.add(" o-o ");
        emoticons.add(" o_o ");
        emoticons.add(" ಠ_ಠ ");
        emoticons.add(" ^-^ ");
        emoticons.add(" ^.^ ");
        emoticons.add(" :3 ");
        emoticons.add(" :@ ");
        emoticons.add(" :D ");
        emoticons.add(" :L ");
        emoticons.add(" :O ");
        emoticons.add(" :P ");
        emoticons.add(" :S ");
        emoticons.add(" :X ");
        emoticons.add(" :[ ");
        emoticons.add(" :] ");
        emoticons.add(" :b ");
        emoticons.add(" :c ");
        emoticons.add(" :o ");
        emoticons.add(" :p ");
        emoticons.add(" :{ ");
        emoticons.add(" ;D ");
        emoticons.add(" ;] ");
        emoticons.add(" :'( ");
        emoticons.add(" (: ");
        emoticons.add(" ): ");
    }
    
    public final void remove(StringBuilder input) throws Exception {
        
        if (TextToolbox.isEmpty(input)) return;
        
        input.insert(0, " ").append(" ");
        
        emoticons.stream().forEach((emoticon) -> {
            TextToolbox.replace(input, emoticon, " ");
        });         
    
        TextToolbox.trim(input);
        
    }

}
