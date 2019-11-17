package com.itcag.demo;

import com.itcag.datatier.IndexDocument;
import com.itcag.datatier.SearchIndex;
import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.SentenceFields;
import java.util.ArrayList;
import org.json.JSONObject;

public class DataTierAPI {
    
    public static void rejectClassification(String sentenceText, String id) throws Exception {
        
        ArrayList<JSONObject> arrSentences = SearchIndex.searchIndex(Indices.CORRECTIONS.getFieldName(), 0, 1, SentenceFields.sentence.getFieldName(), sentenceText);
        if(arrSentences.size() > 0) {
            JSONObject firstResult = arrSentences.get(0);
 System.out.println(firstResult);            
            String _id = firstResult.getString("_id");
           
        } else {
            JSONObject jsonSentence = new JSONObject();
            jsonSentence.put(SentenceFields.sentence.getFieldName(), sentenceText);
            jsonSentence.put(SentenceFields.categoryId.getFieldName(), "");
            IndexDocument.indexDocument(Indices.CORRECTIONS.getFieldName(), jsonSentence.toString());
        }
       
    }
    
}
