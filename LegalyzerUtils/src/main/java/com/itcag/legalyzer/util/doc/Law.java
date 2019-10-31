package com.itcag.legalyzer.util.doc;

import java.util.HashSet;

public class Law extends ExtractedInfo {

    private final String name;
    private String officialName;
    private String guessedOfficialName;
    private String inferredOfficialName;

    private String alias = null;
    private int aliasIndex = -1;
    
    private final HashSet<String> clauses = new HashSet<>();
    
    public Law(String name) {
        this.name = name;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getOfficialName() {
        return this.officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
    
    public String getGuessedOfficialName() {
        return this.guessedOfficialName;
    }

    public void setGuessedOfficialName(String guessedOfficialName) {
        this.guessedOfficialName = guessedOfficialName;
    }
    
    public String getInferredOfficialName() {
        return this.inferredOfficialName;
    }

    public void setInferredOfficialName(String inferredOfficialName) {
        this.inferredOfficialName = inferredOfficialName;
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

    public boolean isOverlapping(Position position) {
        
        if (this.positions.getLast().getSentenceIndex() != position.getSentenceIndex()) return false;

        for (Position existing : this.positions) {
            if (position.getEnd() < existing.getStart() ) return false;
            if (position.getStart() > existing.getEnd() ) return false;
        }
        
        return true;
        
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        
        if (this.officialName != null) {
            retVal.append(this.officialName);
            if (this.alias != null) retVal.append(" (").append(alias).append(")");
            retVal.append("\n");
        } else if (this.guessedOfficialName != null) {
            retVal.append(this.guessedOfficialName).append("*").append("\n");
        } else if (this.inferredOfficialName != null) {
            retVal.append(this.inferredOfficialName).append("**").append("\n");
        } else {
            retVal.append(this.name).append("^").append("\n");
        }
        this.clauses.stream().forEach((clause) -> { retVal.append("\t").append(clause).append("\n");});
        
        return retVal.toString();
        
    }
    
}
