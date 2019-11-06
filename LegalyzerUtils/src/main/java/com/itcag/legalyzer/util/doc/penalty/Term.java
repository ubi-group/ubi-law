package com.itcag.legalyzer.util.doc.penalty;

import java.text.NumberFormat;

public class Term {
    
        
    private final double duration;
    private final Period period;

    public Term(double duration, Period period) {
        this.duration = duration;
        this.period = period;
    }

    public double getDuration() {
        return this.duration;
    }

    public Period getPeriod() {
        return this.period;
    }

    @Override
    public String toString() {

        NumberFormat formatter = NumberFormat.getNumberInstance();
        formatter.setMaximumFractionDigits(1);
        String durationString = formatter.format(this.duration);

        switch (this.period) {
            case DAY:
                if (Double.doubleToLongBits(this.duration) > 1) {
                    return durationString + " " + "days";
                } else {
                    return durationString + " " + "day";
                }
            case MONTH:
                if (Double.doubleToLongBits(this.duration) > 1) {
                    return durationString + " " + "months";
                } else {
                    return durationString + " " + "month";
                }
            case YEAR:
                if (Double.doubleToLongBits(this.duration) > 1) {
                    return durationString + " " + "years";
                } else {
                    return durationString + " " + "year";
                }
            case LIFE:
                return "Life sentence";
            default:
                return null;
        }

    }

}
