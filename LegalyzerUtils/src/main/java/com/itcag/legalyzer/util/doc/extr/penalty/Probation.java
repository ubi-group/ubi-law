package com.itcag.legalyzer.util.doc.extr.penalty;

public class Probation extends CommunityService implements Penalty {
    
    public Probation(double duration, Period period) {
        super(duration, period);
    }
    
    @Override
    public String toString() {
        return "Probation for the duration of " + this.term.toString();
    }

}
