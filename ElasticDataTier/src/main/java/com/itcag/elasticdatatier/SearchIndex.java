package com.itcag.datatier;

import java.util.ArrayList;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONObject;

public class SearchIndex {

    public static ArrayList<JSONObject> searchIndex(String indexName, int from, int size, String fieldName, String fieldValue) throws Exception {
    
        ArrayList<JSONObject> retVal = new ArrayList<JSONObject>();
        
        SearchRequest searchRequest = new SearchRequest(indexName);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
        sourceBuilder.query(QueryBuilders.termQuery(fieldName, fieldValue));
        sourceBuilder.from(from); 
        sourceBuilder.size(size);

        SearchResponse searchResponse = ElasticsearchRestClient.getClient().search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        if( hits != null) {
            SearchHit[] searchHits = hits.getHits();
            for(int i = 0; i < searchHits.length; i++) {
                SearchHit hit = searchHits[i];
                retVal.add(new JSONObject(hit.getSourceAsString()));
            }
        }      

        return retVal;
   
    }
    
}
