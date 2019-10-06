package com.itcag.tagger;

import com.itcag.doc.lang.Document;
import com.itcag.doc.lang.Tag;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public final class DataTierAPI {
 
    public static void addTag(Tag tag) {

    }
    
    public static void updateTag(Tag tag) {

    }
    
    public static void deleteTag(String _id) {

    }
    
    public static Tag getTag(String _id) {
        return null;
    }

    public static ArrayList<Tag> getTags() {
        return null;
    }

    public static ArrayList<Tag> getTags(String query) {
        return null;
    }

    public static void addDocument(Document document) {

    }

    public static void deleteDocument(String _id) {

    }

    public static Document getDocument(String _id) {
        return null;
    }

    public static Document getDocument(Document.Status status) {
        return null;
    }

    public static ArrayList<Document> getDocuments(Document.Status status) {
        return null;
    }

    public static ArrayList<Document> getDocuments(String query) {
        return null;
    }

}