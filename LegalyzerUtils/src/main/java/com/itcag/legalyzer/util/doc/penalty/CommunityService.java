package com.itcag.legalyzer.util.doc.penalty;

public class CommunityService implements Penalty {
    
    protected final Term term;

    public CommunityService(double duration, Period period) {
        this.term = new Term(duration, period);
    }

    public CommunityService() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String toString() {
        return "Community service for the duration of " + this.term.toString();
    }

}
