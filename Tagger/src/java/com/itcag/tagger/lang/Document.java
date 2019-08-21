package com.itcag.tagger.lang;

import com.itcag.util.Printer;

import java.util.Map;
import java.util.TreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

public class Document {


    public enum Fields {
        
        _ID("_id"),
        ID("id"),
        TEXT("text"),
        SENTENCES("sentences"),
        STATUS("status"),
        
        ;
        
        private final String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        public String getName() {
            return this.name;
        }
        
    }
    
    public enum Status {
        VIRGIN, TAGGED, COMPLETED, APPROVED
    }
    
    private String _id = null;
    
    private final String id; //also the file name of the original document on the upload server
    
    private final String text;
    
    private final TreeMap<Integer, Sentence> sentences = new TreeMap<>();
    
    private Status status = Status.VIRGIN;
    
    public Document(String id, String text) {
        this.id = id;
        this.text = text;
    }
    
    public Document(JSONObject jsonObject) {
        
        if (jsonObject.has(Fields._ID.getName())) _id = jsonObject.getString(Fields._ID.getName());

        id = jsonObject.getString(Fields.ID.getName());
        text = jsonObject.getString(Fields.TEXT.getName());
        
        if (jsonObject.has(Fields.SENTENCES.getName())) {
            JSONArray jsonArray = new JSONArray();
            for (int i  = 0; i < jsonArray.length(); i++) {
                Sentence sentence = new Sentence(jsonArray.getJSONObject(i));
                sentences.put(sentence.getDocumentIndex(), sentence);
            }
        }
        
        status = Status.valueOf(jsonObject.getString(Fields.STATUS.getName()));
        
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }
    
    public String getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public TreeMap<Integer, Sentence> getSentences() {
        return sentences;
    }
    
    public void addSentence(Sentence sentence) {
        this.sentences.put(sentence.getDocumentIndex(), sentence);
    }
    
    
    public JSONObject getJSON() {
        
        JSONObject retVal = new JSONObject();
        
        if (_id != null) retVal.put(Fields._ID.getName(), _id);

        retVal.put(Fields.ID.getName(), id);
        retVal.put(Fields.TEXT.getName(), text);

        if (!sentences.isEmpty()) {
            JSONArray jsonArray = new JSONArray();
            for (Map.Entry<Integer, Sentence> entry : sentences.entrySet()) {
                jsonArray.put(entry.getValue().getJSON());
            }
            retVal.put(Fields.SENTENCES.getName(), jsonArray);
        }
        
        retVal.put(Fields.STATUS.getName(), status.name());
        
        return retVal;
        
    }
    
    @Override
    public String toString() {
        return id + "\t" + status.name() + System.lineSeparator() + text + System.lineSeparator();
    }
    
    public void print() {
        Printer.print(this.toString());
    }
    
}
