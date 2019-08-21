package com.itcag.tagger.lang;

import com.itcag.util.Printer;

import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Sentence {

    public enum Fields {
        
        TEXT("text"),
        TYPE("type"),
        DOCUMENT_INDEX("documentIndex"),
        PARAGRAPH_INDEX("paragraphIndex"),
        TAGS("tags"),
        
        ;
        
        private final String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
    }
    
    public enum Type {
        
        TITLE,
        SUBTITLE,
        SENTENCE,
        
    }
    
    private final String text;
    private final Type type;
    
    private final int documentIndex;
    private final int paragraphIndex;

    private final HashMap<String, Tag> tags = new HashMap<>(); 
    
    public Sentence(String text, Type type, int documentIndex, int paragraphIndex) {
        this.text = text;
        this.type = type;
        this.documentIndex = documentIndex;
        this.paragraphIndex = paragraphIndex;
    }
    
    public Sentence(JSONObject jsonObject) {
        
        text = jsonObject.getString(Fields.TEXT.getName());
        type = Type.valueOf(jsonObject.getString(Fields.TYPE.getName()));
        
        documentIndex = jsonObject.getInt(Fields.DOCUMENT_INDEX.getName());
        paragraphIndex = jsonObject.getInt(Fields.PARAGRAPH_INDEX.getName());
        
        if (jsonObject.has(Fields.TAGS.getName())) {
            JSONArray jsonArray = jsonObject.getJSONArray(Fields.TAGS.getName());
            for (int i = 0; i < jsonArray.length(); i++) {
                Tag tag = new Tag(jsonArray.getJSONObject(i));
                tags.put(tag.getId(), tag);
            }
        }

    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }

    public int getDocumentIndex() {
        return documentIndex;
    }

    public int getParagraphIndex() {
        return paragraphIndex;
    }

    public HashMap<String, Tag> getTags() {
        return tags;
    }
    
    public void addTag(Tag tag) {
        tags.put(tag.getId(), tag);
    }
    
    public JSONObject getJSON() {
        
        JSONObject retVal = new JSONObject();
        
        retVal.put(Fields.TEXT.getName(), this.text);
        retVal.put(Fields.TYPE.getName(), this.type.name());
        
        retVal.put(Fields.DOCUMENT_INDEX.getName(), this.documentIndex);
        retVal.put(Fields.PARAGRAPH_INDEX.getName(), this.paragraphIndex);
        
        if (!this.tags.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            tags.values().forEach((tag) -> {
                jsonArray.put(tag.getJSON());
            });
            retVal.put(Fields.TAGS.getName(), jsonArray);
        }
        
        return retVal;
        
    }
    
    @Override
    public String toString() {
        StringBuilder retVal = new StringBuilder();
        retVal.append(text);
        if (!tags.isEmpty()) {
            StringBuilder tmp = new StringBuilder();
            for (Tag tag : tags.values()) {
                if (tmp.length() > 0) tmp.append("; ");
                tmp.append(tag.getId());
            }
            retVal.append(" (").append(tmp).append(")");
        }
        return text;
    }
    
    public void print() {
        Printer.print(this.toString());
    }
    
}
