package com.itcag.split;

import com.itcag.util.punct.PunctuationSigns;
import com.itcag.util.punct.PunctuationToolbox;
import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.HashSet;

public final class Split {
    
    private final HashSet<String> abbreviations = new HashSet<>();
    
    public Split() {
        abbreviations.add("a.b.");
        abbreviations.add("adv.");
        abbreviations.add("aeq.");
        abbreviations.add("aet.");
        abbreviations.add("agcy.");
        abbreviations.add("agt.");
        abbreviations.add("a.k.a.");
        abbreviations.add("ala.");
        abbreviations.add("al.");	//et al.
        abbreviations.add("alta.");
        abbreviations.add("apt.");
        abbreviations.add("ariz.");
        abbreviations.add("ark.");
        abbreviations.add("ass'n.");
        abbreviations.add("asst.");
        abbreviations.add("ave.");
        abbreviations.add("bbl.");
        abbreviations.add("b.c.");
        abbreviations.add("bldg.");
        abbreviations.add("blvd.");
        abbreviations.add("brig.-gen.");
        abbreviations.add("calif.");
        abbreviations.add("capt.");
        abbreviations.add("c.c.");
        abbreviations.add("cc.");
        abbreviations.add("cf.");
        abbreviations.add("chtd.");
        abbreviations.add("cir.");
        abbreviations.add("cm.");
        abbreviations.add("cmdr.");
        abbreviations.add("co.");
        abbreviations.add("co.");        
        abbreviations.add("col.");
        abbreviations.add("colo.");
        abbreviations.add("cols.");
        abbreviations.add("comp.");        
        abbreviations.add("conn.");
        abbreviations.add("corp.");
        abbreviations.add("cpl.");
        abbreviations.add("ct.");
        abbreviations.add("ctr.");
        abbreviations.add("cu.");
        abbreviations.add("d-ala.");
        abbreviations.add("d-ariz.");
        abbreviations.add("d-ark.");
        abbreviations.add("d-calif.");
        abbreviations.add("d-colo.");
        abbreviations.add("d-conn.");
        abbreviations.add("d-del.");
        abbreviations.add("del.");
        abbreviations.add("dept.");
        abbreviations.add("d-fla.");
        abbreviations.add("d-ga.");
        abbreviations.add("d-hawaii");
        abbreviations.add("d-idaho");
        abbreviations.add("d-ill.");
        abbreviations.add("d-ind.");
        abbreviations.add("d-iowa");
        abbreviations.add("disc.");
        abbreviations.add("d-kans.");
        abbreviations.add("d-ky.");
        abbreviations.add("d-la.");
        abbreviations.add("d-maine");
        abbreviations.add("d-mass.");
        abbreviations.add("d-md.");
        abbreviations.add("d-mich.");
        abbreviations.add("d-minn.");
        abbreviations.add("d-miss.");
        abbreviations.add("d-mo.");
        abbreviations.add("d-mont.");
        abbreviations.add("d-n.c.");
        abbreviations.add("d-n.d.");
        abbreviations.add("d-nebr.");
        abbreviations.add("d-nev.");
        abbreviations.add("d-n.h.");
        abbreviations.add("d-n.j.");
        abbreviations.add("d-n.m.");
        abbreviations.add("d-n.y.");
        abbreviations.add("d-ohio");
        abbreviations.add("d-okla.");
        abbreviations.add("d-ore.");
        abbreviations.add("doz.");
        abbreviations.add("d-pa.");
        abbreviations.add("d.p.c.");
        abbreviations.add("dr.");
        abbreviations.add("d-r.i.");
        abbreviations.add("d-s.c.");
        abbreviations.add("d-s.d.");
        abbreviations.add("d-tenn.");
        abbreviations.add("d-tex.");
        abbreviations.add("d-utah");
        abbreviations.add("d-va.");
        abbreviations.add("d-vt.");
        abbreviations.add("d-wash.");
        abbreviations.add("d-wis.");
        abbreviations.add("d-w.va.");
        abbreviations.add("d-wyo.");
        abbreviations.add("e.g.");
        abbreviations.add("esq.");
        abbreviations.add("etc.");
        abbreviations.add("expy.");
        abbreviations.add("f.c.");
        abbreviations.add("fec.");
        abbreviations.add("f.k.a.");
        abbreviations.add("fl.");
        abbreviations.add("fla.");
        abbreviations.add("ft.");
        abbreviations.add("ga.");
        abbreviations.add("gal.");
        abbreviations.add("g.b.");
        abbreviations.add("gens.");
        abbreviations.add("gr.");
        abbreviations.add("gro.");
        abbreviations.add("ha.");
        abbreviations.add("hawaii");
        abbreviations.add("h.c.");
        abbreviations.add("hon.");
        abbreviations.add("hts.");
        abbreviations.add("hwy.");
        abbreviations.add("idaho");
        abbreviations.add("i.e.");
        abbreviations.add("ill.");
        abbreviations.add("inc.");
        abbreviations.add("ind.");
        abbreviations.add("ing.");
        abbreviations.add("jct.");
        abbreviations.add("jr.");
        abbreviations.add("kans.");
        abbreviations.add("kb.");
        abbreviations.add("kg.");
        abbreviations.add("kl.");
        abbreviations.add("km.");
        abbreviations.add("kt.");
        abbreviations.add("kw.");
        abbreviations.add("kwh.");
        abbreviations.add("ky.");
        abbreviations.add("l.3.c.");
        abbreviations.add("la.");
        abbreviations.add("lb.");
        abbreviations.add("l.c.");
        abbreviations.add("lc.");
        abbreviations.add("lieut.");
        abbreviations.add("lk.");
        abbreviations.add("l.l.");
        abbreviations.add("l.l.c.");
        abbreviations.add("llc.");
        abbreviations.add("ln.");
        abbreviations.add("loc.");	//ad loc.
        abbreviations.add("l.s.");
        abbreviations.add("l.t.");
        abbreviations.add("lt.");
        abbreviations.add("lt.-cmdr.");
        abbreviations.add("lt.-col.");
        abbreviations.add("ltd.");
        abbreviations.add("lt.-gen.");
        abbreviations.add("maine");
        abbreviations.add("maj.");
        abbreviations.add("maj.-gen.");
        abbreviations.add("maj.-gens.");
        abbreviations.add("man.");
        abbreviations.add("mass.");
        abbreviations.add("mb.");
        abbreviations.add("mcg.");
        abbreviations.add("md.");
        abbreviations.add("messrs.");
        abbreviations.add("mfg.");        
        abbreviations.add("mg.");
        abbreviations.add("mi.");
        abbreviations.add("mich.");
        abbreviations.add("minn.");
        abbreviations.add("miss.");
        abbreviations.add("ml.");
        abbreviations.add("mm.");
        abbreviations.add("mmes.");
        abbreviations.add("mo.");
        abbreviations.add("mont.");
        abbreviations.add("m.p.h.");
        abbreviations.add("mph.");
        abbreviations.add("mr.");
        abbreviations.add("mrs.");
        abbreviations.add("m.s.");
        abbreviations.add("ms.");
        abbreviations.add("m.sc.");
        abbreviations.add("msgr.");
        abbreviations.add("mt.");
        abbreviations.add("mtn.");
        abbreviations.add("n.b.");
        abbreviations.add("n.c.");
        abbreviations.add("n.d.");
        abbreviations.add("ne.");
        abbreviations.add("nebr.");
        abbreviations.add("nev.");
        abbreviations.add("n.h.");
        abbreviations.add("n.j.");
        abbreviations.add("n.l.");
        abbreviations.add("n.m.");
        abbreviations.add("n.s.");
        abbreviations.add("nw.");
        abbreviations.add("n.w.t.");
        abbreviations.add("n.y.");
        abbreviations.add("ohio");
        abbreviations.add("okla.");
        abbreviations.add("ont.");
        abbreviations.add("ore.");
        abbreviations.add("o.s.");
        abbreviations.add("oz.");
        abbreviations.add("p.a.");
        abbreviations.add("pa.");
        abbreviations.add("p.c.");
        abbreviations.add("pc.");
        abbreviations.add("p.e.i.");
        abbreviations.add("ph.d.");
        abbreviations.add("p.i.");
        abbreviations.add("pkwy.");
        abbreviations.add("pl.");
        abbreviations.add("p.l.l.c.");
        abbreviations.add("p.l.p.");
        abbreviations.add("plz.");
        abbreviations.add("prof.");
        abbreviations.add("p.s.c.");
        abbreviations.add("pt.");
        abbreviations.add("pte.");
        abbreviations.add("qt.");
        abbreviations.add("que.");
        abbreviations.add("r-ala.");
        abbreviations.add("r-ariz.");
        abbreviations.add("r-ark.");
        abbreviations.add("r-calif.");
        abbreviations.add("r-colo.");
        abbreviations.add("r-conn.");
        abbreviations.add("rd.");
        abbreviations.add("r-del.");
        abbreviations.add("rdg.");
        abbreviations.add("rev.");
        abbreviations.add("r-fla.");
        abbreviations.add("r-ga.");
        abbreviations.add("r-hawaii");
        abbreviations.add("r.i.");
        abbreviations.add("r-idaho");
        abbreviations.add("r-ill.");
        abbreviations.add("r-ind.");
        abbreviations.add("r-iowa");
        abbreviations.add("r-kans.");
        abbreviations.add("r-ky.");
        abbreviations.add("r-la.");
        abbreviations.add("rm.");
        abbreviations.add("r-maine");
        abbreviations.add("r-mass.");
        abbreviations.add("r-md.");
        abbreviations.add("r-mich.");
        abbreviations.add("r-minn.");
        abbreviations.add("r-miss.");
        abbreviations.add("r-mo.");
        abbreviations.add("r-mont.");
        abbreviations.add("r-n.c.");
        abbreviations.add("r-n.d.");
        abbreviations.add("r-nebr.");
        abbreviations.add("r-nev.");
        abbreviations.add("r-n.h.");
        abbreviations.add("r-n.j.");
        abbreviations.add("r-n.m.");
        abbreviations.add("r-n.y.");
        abbreviations.add("r-ohio");
        abbreviations.add("r-okla.");
        abbreviations.add("r-ore.");
        abbreviations.add("r-pa.");
        abbreviations.add("rpm");
        abbreviations.add("r-r.i.");
        abbreviations.add("r.s.a.");
        abbreviations.add("r-s.c.");
        abbreviations.add("r-s.d.");
        abbreviations.add("rt.");
        abbreviations.add("r-tenn.");
        abbreviations.add("r-tex.");
        abbreviations.add("r-utah");
        abbreviations.add("r-va.");
        abbreviations.add("r-vt.");
        abbreviations.add("r-wash.");
        abbreviations.add("r-wis.");
        abbreviations.add("r-w.va.");
        abbreviations.add("r-wyo.");
        abbreviations.add("s.a.");
        abbreviations.add("sask.");
        abbreviations.add("s.c.");
        abbreviations.add("sc.");
        abbreviations.add("sc.d.");
        abbreviations.add("s.d.");
        abbreviations.add("se.");
        abbreviations.add("sen.");
        abbreviations.add("seq.");	//et seq.
        abbreviations.add("sgt.");
        abbreviations.add("s.p.e.a.r.");
        abbreviations.add("sq.");
        abbreviations.add("sr.");
        abbreviations.add("st.");
        abbreviations.add("sta.");
        abbreviations.add("ste.");
        abbreviations.add("sub-lieut.");
        abbreviations.add("sw.");
        abbreviations.add("tbsp.");
        abbreviations.add("tenn.");
        abbreviations.add("ter.");
        abbreviations.add("tex.");
        abbreviations.add("tpke.");
        abbreviations.add("trl.");
        abbreviations.add("tsp.");
        abbreviations.add("u.k.");
        abbreviations.add("u.s.");
        abbreviations.add("u.s.a.");
        abbreviations.add("utah");
        abbreviations.add("v.");
        abbreviations.add("va.");
        abbreviations.add("vly.");
        abbreviations.add("vs.");
        abbreviations.add("vt.");
        abbreviations.add("wash.");
        abbreviations.add("wis.");
        abbreviations.add("w.va.");
        abbreviations.add("wyo.");
        abbreviations.add("yd.");
        abbreviations.add("y.t.");
        abbreviations.add("µg.");
    }
    
    public final ArrayList<StringBuilder> split(StringBuilder input) {

        TextToolbox.trim(input);
        
        if (TextToolbox.isEmpty(input)) return new ArrayList<>();

        ArrayList<StringBuilder> retVal = new ArrayList<>();

        StringBuilder buffer = new StringBuilder();
        StringBuilder lastWord = new StringBuilder();
        
        char p;
        char n;
        
        int i = 0;
        while (i < input.length()) {

            char c = input.charAt(i);

            if (i > 0) {
                p = input.charAt(i - 1);
            } else {
                p = 0;
            }

            if ((i + 1) < input.length()) {
                n = input.charAt(i + 1);
            } else {
                n = 0;
            }

            switch (c) {
                case 32:
                    
                    /*
                     * First character is empty space - ignore it.
                     */
                    if (p == 0) continue;
                    
                    /*
                     * Last character is empty space - ignore it.
                     * If buffer is not empty,
                     * it will be inserted into the return list by default.
                     */
                    if (n == 0) continue;
                    
                    if (isEndOfSentence(p, n, lastWord) && buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                    } else {
                        buffer.append(c);
                    }
                    
                    lastWord = new StringBuilder();

                    break;

                case 9:
                case 10:
                case 13:
                    /*
                     * Split on line break.
                     */
                    if (buffer.length() > 0) {
                        retVal.add(buffer);
                        buffer = new StringBuilder();
                        lastWord = new StringBuilder();
                    }
                    break;
                default:
                    buffer.append(c);
                    lastWord.append(c);
                    break;
            }
             
            i++;
            
        }

        if (buffer.length() > 0) retVal.add(buffer);
        
        return retVal;
    
    }
    
    private boolean isEndOfSentence(char p, char n, StringBuilder lastWord) {

        /**
         * Do not split after a single letter word.
         */
        if (lastWord.length() == 1) return false;
        
        if (!PunctuationToolbox.isPunctuation(p)) return false;
        
        /**
         * Do not split on comma.
         */
        if (PunctuationSigns.COMMA.getCharacter() == p) return false;
        
        return !abbreviations.contains(lastWord.toString().toLowerCase());

    }
    
}
