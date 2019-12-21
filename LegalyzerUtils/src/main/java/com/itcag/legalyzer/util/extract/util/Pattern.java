package com.itcag.legalyzer.util.extract.util;

import org.json.JSONObject;

/**
 *
 */
public class Pattern {

    private enum Fields {
        
        SCOPE("scope"),
        ROLE("role"),
        TYPE("type"),
        VALUE("value"),
        SEMANTIC("semantic"),
        
        ;
        
        private String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        private String getname() {
            return this.name;
        }
        
    }
    
    public final PatternScopes scope;
    public final PatternRoles role;
    public final PatternTypes type;
    public final String value;
    public final PatternSemantics semantic;
    
    public Pattern(JSONObject jsonObject) throws Exception {
        
        this.scope = jsonObject.getEnum(PatternScopes.class, Fields.SCOPE.getname());
        this.role = jsonObject.getEnum(PatternRoles.class, Fields.ROLE.getname());
        this.type = jsonObject.getEnum(PatternTypes.class, Fields.TYPE.getname());
        this.value = jsonObject.getString(Fields.VALUE.getname());
        this.semantic = jsonObject.getEnum(PatternSemantics.class, Fields.SEMANTIC.getname());
        
    }

    public PatternScopes getScope() {
        return scope;
    }

    public PatternRoles getRole() {
        return role;
    }

    public PatternTypes getType() {
        return type;
    }
    
    public String getValue() {
        return this.value;
    }

    public PatternSemantics getSemantic() {
        return semantic;
    }
    
}
