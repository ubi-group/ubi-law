package com.itcag.wiki;

import java.util.HashSet;

public final class Blacklist {
    
    private final static HashSet<String> BLACKLIST = new HashSet<>();
    static {
        BLACKLIST.add("422994");    //DOI Digital object identifier
        BLACKLIST.add("23538754");    //Wayback Machine
        BLACKLIST.add("14919");    //ISBN International Standard Book Number
        BLACKLIST.add("3380384");    //Parent company
        BLACKLIST.add("7043");    //Chemical formula
        BLACKLIST.add("40283");    //Melting point
        BLACKLIST.add("2863723");    //PubChem
        BLACKLIST.add("240224");    //Standard state
        BLACKLIST.add("28569");    //Simplified molecular-input line-entry system
        BLACKLIST.add("1995749");    //International Chemical Identifier
        BLACKLIST.add("10468561");    //ChemSpider
        BLACKLIST.add("144241");    //Molar mass
        BLACKLIST.add("52514414");    //ECHA InfoCard
        BLACKLIST.add("48445011");    //JSmol
        BLACKLIST.add("8429");    //Density
        BLACKLIST.add("4115");    //Boiling point
        BLACKLIST.add("13204100");    //PubMed Identifier
        BLACKLIST.add("160815");    //CAS Registry Number
        BLACKLIST.add("3067384");    //European Community number
        BLACKLIST.add("3141503");    //Chemical nomenclature
        BLACKLIST.add("3971631");    //KEGG
        BLACKLIST.add("18852926");    //International Standard Name Identifier
        BLACKLIST.add("1491462");    //Library of Congress Control Number
        BLACKLIST.add("35566739");    //Integrated Authority File
        BLACKLIST.add("25175562");    //Virtual International Authority File
        BLACKLIST.add("33551951");    //Système universitaire de documentation
        BLACKLIST.add("199503");    //Bibliothèque nationale de France
        BLACKLIST.add("42152933");    //BIBSYS
        BLACKLIST.add("3198808");    //Biblioteca Nacional de España
        BLACKLIST.add("234930");    //International Standard Serial Number
        BLACKLIST.add("2855554");    //IMDb
//        BLACKLIST.add("");    //
        
    }
    
    public final static boolean exists(String pageId) {
        return BLACKLIST.contains(pageId);
    }
    
    public final static boolean isAceptableConceptLabel(String title) {
        if (title.startsWith("draft:")) {
            return false;
        } else if (title.contains(" in ")) {
            return false;
        } else if (title.endsWith(" by country")) {
            return false;
        } else if (title.endsWith(" by continent")) {
            return false;
        } else if (title.endsWith(" by nationality")) {
            return false;
        } else if (title.startsWith("list of ")) {
            return false;
        } else if (title.startsWith("lists related to ")) {
            return false;
        } else if (title.startsWith("documentary films about ")) {
            return false;
        } else if (title.endsWith(" stubs")) {
            return false;
        } else if (title.endsWith(" ingredients")) {
            return false;
        } else if (title.endsWith(" organizations")) {
            return false;
        } else if (title.endsWith(" associations")) {
            return false;
        } else if (title.endsWith(" companies")) {
            return false;
        } else if (title.endsWith(" unions")) {
            return false;
        } else if (title.endsWith(" brands")) {
            return false;
        } else if (title.endsWith(" manufacturers")) {
            return false;
        } else if (title.startsWith("people associated with ")) {
            return false;
        } else if (title.endsWith(" artists")) {
            return false;
        } else if (title.endsWith(" businesspeople")) {
            return false;
        } else {
            return true;
        }
    }
    
}
