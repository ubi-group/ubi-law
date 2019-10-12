package com.itcag.split;

import java.util.ArrayList;

public final class Splitter {

    public final ArrayList<StringBuilder> split(StringBuilder input) throws Exception {
        
        Protocol protocol = new Protocol();
        return protocol.execute(new StringBuilder(input));
    
    }

}
