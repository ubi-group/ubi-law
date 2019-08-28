package com.itcag.wiki;

public class Config {

    public final static Double INITIAL_SUBCATEGORY_WEIGHT = 1.0;
    public final static Double SUBCATEGORY_WEIGHT_DECREASE = 0.2;
    public final static Double PAGE_BASIC_WEIGHT = 0.9;
    public final static Double LINK_BASIC_WEIGHT = 0.4;
    public final static Double LINK_MIN_WEIGHT = 0.2;
    public final static Double LINK_FOO_WEIGHT_INCREASE_FACTOR = 0.01;
    public final static Double LINK_FOO_WEIGHT_DECREASE = 0.1;
    
    public final static Double SUBCATEGORY_THRESHOLD = 0.6;
    public final static Integer MIN_LINK_FOO_KNOWN_THRESHOLD = 11;
    public final static Integer MIN_LINK_FOO_UNKNOWN_THRESHOLD = 20;
    
    public final static String PAGE_DELIMITER = "+";
    public final static String LINK_DELIMITER = "|";
    
}
