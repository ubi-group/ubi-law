package com.itcag.legalyzer.util.doc.penalty;

public class Incarceration extends CommunityService implements Penalty {
    
    private final boolean suspended;

    public Incarceration(double duration, Period period, boolean suspended) {
        super(duration, period);
        this.suspended = suspended;
    }

    public boolean isSuspended() {
        return this.suspended;
    }
    
    public String toString() {
        if (this.suspended) {
            return "Incarceration for the duration of " + super.term.toString() + " (suspended)";
        } else {
            return "Incarceration for the duration of " + super.term.toString();
        }
    }

}
