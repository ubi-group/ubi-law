package com.itcag.datatier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

public class Tester {

    public static void main(String[] args) throws Exception {
        
//        CorrectionsCleaner.cleanIndex();
//        DataTrainingCleaner.cleanIndex();
//        CategoriesCleaner.cleanIndex();
       CourtRulingsCleaner.cleanIndex();
       
//Map<String, String> propertyValues = new HashMap<String, String>();
//propertyValues.put("paragraphs.sentences.paragraphIndex", 0+"");
//propertyValues.put("paragraphs.sentences.index", 1+"");

//NestedQueryBuilder nested = SearchIndex.nestedBoolQuery(propertyValues, "paragraphs.sentences");

//NestedQueryBuilder outside = QueryBuilders.nestedQuery("paragraphs", nested, ScoreMode.None);


 
//        ArrayList<JSONObject> arr = SearchIndex.searchIndex2("court_rulings", 0,10, propertyValues,"paragraphs.sentences", "paragraphs", "id", "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\17\\320\\084\\g16&fileName=17084320.G16&type=2");
//        System.out.println(arr.size());
        
//IndexDocument.indexDocument("court_rulings","{ \"id\": \"xxx\"}");
        

//            ArrayList<JSONObject> results = SearchIndex.searchIndex(Indices.COURT_RULINGS.getFieldName(), 0, 1, DocumentFields.id.getFieldName(), "/Home/Download?path=HebrewVerdicts\\18\\400\\077\\z04&fileName=18077400.Z04&type=2");
//System.out.println(results);


    }   
    
    /*
{
    "query" : {
        "nested" : {
            "path" : "paragraphs",
            "query" : {
                "nested" : {
                    "path" :  "paragraphs.sentences",
                    "query" :  {
                        "bool" : {
                            "must" : [
                                { "match" : { "paragraphs.sentences.paragraphIndex" : 1 } },
                                { "match" : { "paragraphs.sentences.index" : 3 } }
                            ]
                        }
                    }
                }
            }
        }
    }
}    
    */
    
}
