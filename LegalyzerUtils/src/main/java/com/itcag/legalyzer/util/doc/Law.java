package com.itcag.legalyzer.util.doc;

import java.util.HashSet;

public class Law {

    private final String name;
    private final String officialName;
    
    private String alias = null;
    private int aliasIndex = -1;
    
    private final HashSet<String> clauses = new HashSet<>();
    
    public Law(String name) {
        this.name = name;
        this.officialName = name;
    }
    
    public Law(String name, String officialName) {
        this.name = name;
        this.officialName = officialName;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getOfficialName() {
        return this.officialName;
    }
    
    public String getAlias() {
        return this.alias;
    }
    
    public void setAlias(String alias, int aliasIndex) {
        this.alias = alias;
        this.aliasIndex = aliasIndex;
    }
    
    public int getAliasIndex() {
        return this.aliasIndex;
    }
    
    public HashSet<String> getClauses() {
        return this.clauses;
    }
    
    public void addClause(String clause) {
        this.clauses.add(clause);
    }

    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        
        retVal.append(officialName);
        if (this.alias != null) retVal.append(" (").append(alias).append(")");
        retVal.append("\n");
        this.clauses.stream().forEach((clause) -> { retVal.append("\t").append(clause).append("\n");});
        
        return retVal.toString();
        
    }
    
}
