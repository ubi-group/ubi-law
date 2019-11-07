package com.itcag.legalyzer.util.doc.extr;

/**
 *
 */
public class CourtRuling extends ExtractedInfo {

    private final String code;
    private final String alternative;
    
    private String label = null;
    
    public CourtRuling(String code) {
        this.code = code;
        this.alternative = null;
    }
    
    public CourtRuling(String code, String alternative) {
        this.code = code;
        this.alternative = alternative;
    }
    
    public String getCode() {
        return this.code;
    }
    
    public String getAlternative() {
        return this.alternative;
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public void setLabel(String label) {
        this.label = label;
    }
    
    @Override
    public String toString() {
        
        StringBuilder retVal = new StringBuilder();
        
        retVal.append(code);

        if (this.alternative != null) {
            retVal.append("^").append(" (").append(this.alternative).append(")");
        }

        if (this.label != null) retVal.append(" ").append(this.label);
        
        return retVal.toString();
        
    }
    
}
