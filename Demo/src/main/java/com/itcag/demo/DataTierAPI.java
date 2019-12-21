package com.itcag.demo;

import com.itcag.datatier.DeleteDocument;
import com.itcag.datatier.DeleteIndex;
import com.itcag.datatier.IndexDocument;
import com.itcag.datatier.SearchIndex;
import com.itcag.datatier.meta.Constants;
import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.SentenceFields;
import java.util.ArrayList;
import org.json.JSONObject;

public class DataTierAPI {
   
    public static void rejectClassification(String sentenceText) throws Exception {
        
        ArrayList<JSONObject> arrSentences = SearchIndex.searchIndex(Indices.CORRECTIONS.getFieldName(), 0, 1, SentenceFields.sentence.getFieldName(), sentenceText);
      
        if(arrSentences != null && arrSentences.size() > 0) {
             for(JSONObject obj: arrSentences) {
                String _id = obj.getString(Constants._ID);
                DeleteDocument.deleteIndex(Indices.CORRECTIONS.getFieldName(), _id);
            }
        }
       
    }
    
    public static JSONObject getCorrection(String sentenceText) throws Exception {
        
        ArrayList<JSONObject> arrSentences = SearchIndex.searchIndex(Indices.CORRECTIONS.getFieldName(), 0, 1, SentenceFields.sentence.getFieldName(), sentenceText);
        if(arrSentences  != null && arrSentences.size() > 0) {
            return arrSentences.get(0);
        } else {
            return null;
        }
        
    }
    
    public static void indexClassification(String sentenceText, String categoryId) throws Exception {
        
        ArrayList<JSONObject> arrSentences = SearchIndex.searchIndex(Indices.CORRECTIONS.getFieldName(), 0, 1, SentenceFields.sentence.getFieldName(), sentenceText);
      
        if(arrSentences.size() > 0) {
            JSONObject firstResult = arrSentences.get(0); 
            String _id = firstResult.getString(Constants._ID);
            DeleteDocument.deleteIndex(Indices.CORRECTIONS.getFieldName(), _id);
            JSONObject newSentence = new JSONObject();
            newSentence.put(SentenceFields.sentence.getFieldName(), sentenceText);
            newSentence.put(SentenceFields.categoryId.getFieldName(), categoryId);
            IndexDocument.indexDocument(Indices.CORRECTIONS.getFieldName(), newSentence.toString());
           
        } else {
            JSONObject jsonSentence = new JSONObject();
            jsonSentence.put(SentenceFields.sentence.getFieldName(), sentenceText);
            jsonSentence.put(SentenceFields.categoryId.getFieldName(), categoryId);
            IndexDocument.indexDocument(Indices.CORRECTIONS.getFieldName(), jsonSentence.toString());
        }
       
    }    

}
