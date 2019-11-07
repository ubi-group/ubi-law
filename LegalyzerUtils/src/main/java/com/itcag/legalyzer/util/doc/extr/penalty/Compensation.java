package com.itcag.legalyzer.util.doc.extr.penalty;

import java.text.NumberFormat;

public class Compensation extends Fine implements Penalty {
    
    public Compensation(int amount) {
        super(amount);
    }
    
    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(this.amount);
        return "Compensation in the amount of " + moneyString + " NIS";
    }
    
}
