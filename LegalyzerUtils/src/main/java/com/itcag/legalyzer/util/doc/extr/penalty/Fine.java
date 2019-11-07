package com.itcag.legalyzer.util.doc.extr.penalty;

import java.text.NumberFormat;

public class Fine implements Penalty {
    
    protected final int amount;
    
    public Fine(int amount) {
        this.amount = amount;
    }
    
    @Override
    public String toString() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        String moneyString = formatter.format(this.amount);
        return "Fine in the amount of " + moneyString + " NIS";
    }
    
}
