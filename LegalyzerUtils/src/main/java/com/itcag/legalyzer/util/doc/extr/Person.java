package com.itcag.legalyzer.util.doc.extr;

public class Person extends ExtractedInfo {

    public enum Type {
        JUDGE,
        ATTORNEY_PLAINTIFF,
        ATTORNEY_DEFENDANT
    }
    
    private final String name;
    private final Type type;
    
    public Person(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name + " (" + type.name() + ")";
    }
    
}
