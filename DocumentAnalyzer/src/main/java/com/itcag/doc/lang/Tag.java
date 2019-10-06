package com.itcag.doc.lang;

import com.itcag.util.Printer;

import org.json.JSONObject;

public class Tag {

    public enum Fields {
        
        _ID("_id"),
        ID("id"),
        LABEL("label"),
        
        ;
        
        private final String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
    }
    
    private String _id = null;
    
    private final String id;
    private final String label;
    
    public Tag(String id, String label) {
        this.id = id;
        this.label = label;
    }

    public Tag(JSONObject jsonObject) {
        
        if (jsonObject.has(Fields._ID.getName())) _id = jsonObject.getString(Fields._ID.getName());
        id = jsonObject.getString(Fields.ID.getName());
        label = jsonObject.getString(Fields.LABEL.getName());
        
    }
    
    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_id() {
        return id;
    }

    public String getId() {
        return _id;
    }

    public String getLabel() {
        return label;
    }
    
    public JSONObject getJSON() {
        
        JSONObject retVal = new JSONObject();
        
        if (_id != null) retVal.put(Fields._ID.getName(), _id);
        retVal.put(Fields.ID.getName(), id);
        retVal.put(Fields.LABEL.getName(), label);
        
        return retVal;
        
    }
    
    @Override
    public String toString() {
        return id + "\t" + label;
    }
    
    public void print() {
        Printer.print(this.toString());
    }
    
}
