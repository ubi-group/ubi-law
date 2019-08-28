package com.itcag.wiki.lang;

import com.itcag.util.MathToolbox;
import com.itcag.wiki.Config;

public class Link {

    private final Page head;
    private final Page tail;
    
    private Double weight;
    
    public Link(Page head, Page tail) {
        this.head = head;
        this.tail = tail;
        this.weight = null;
    }
    
    public Link(Page head, Page tail, Double weight) {
        this.head = head;
        this.tail = tail;
        this.weight = weight;
    }

    public Page getHead() {
        return head;
    }

    public Page getTail() {
        return tail;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }
    
    public void updateWeight(Double weight) {
        if (this.weight == null) {
            this.weight = weight;
        } else {
            this.weight += weight;
        }
    }

    @Override
    public String toString() {

        StringBuilder retVal = new StringBuilder();
        
        retVal.append(MathToolbox.roundDouble(weight, 2));
        retVal.append(Config.LINK_DELIMITER).append(head.toString());
        retVal.append(Config.LINK_DELIMITER).append(tail.toString());
        
        return retVal.toString();
        
    }
    
}
