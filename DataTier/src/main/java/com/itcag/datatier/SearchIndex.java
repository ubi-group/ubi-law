package com.itcag.datatier;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
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
        
        searchRequest.indices(indexName);
        searchRequest.source(sourceBuilder);
        
        RestHighLevelClient client = ElasticsearchRestClient.getClient();

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits hits = searchResponse.getHits();
        if( hits != null) {
            SearchHit[] searchHits = hits.getHits();
            for(int i = 0; i < searchHits.length; i++) {
                SearchHit hit = searchHits[i];
                retVal.add(new JSONObject(hit.getSourceAsString()));
            }
        }      
        
        client.close();

        return retVal;
   
    }
    
    public static ArrayList<JSONObject> searchIndex(String indexName, int from, int size, final Map<String, String> propertyValues, final String nestedPath, final String nestedPathOutside) throws Exception {
    
        ArrayList<JSONObject> retVal = new ArrayList();
        
        SearchRequest searchRequest = new SearchRequest(indexName);

        NestedQueryBuilder nestedInside = SearchIndex.nestedBoolQuery(propertyValues, nestedPath);

        NestedQueryBuilder outsideOutside = QueryBuilders.nestedQuery(nestedPathOutside, nestedInside, ScoreMode.None);        
        
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
        sourceBuilder.query(outsideOutside);
        sourceBuilder.from(from); 
        sourceBuilder.size(size);
        
        searchRequest.indices(indexName);
        searchRequest.source(sourceBuilder);

        RestHighLevelClient client = ElasticsearchRestClient.getClient();
        
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println(searchResponse.toString());
        System.out.println("~~~~~~~~~~~~~~~~~~~~`");
        SearchHits hits = searchResponse.getHits();
        if( hits != null) {
            SearchHit[] searchHits = hits.getHits();
            for(int i = 0; i < searchHits.length; i++) {
                SearchHit hit = searchHits[i];
                retVal.add(new JSONObject(hit.getSourceAsString()));
            }
        }      
        
        client.close();
        
        return retVal;
   
    }    
    
    public static ArrayList<JSONObject> searchIndex2(String indexName, int from, int size, final Map<String, String> propertyValues, final String nestedPath, final String nestedPathOutside, String fieldName, String fieldValue) throws Exception {
    
        ArrayList<JSONObject> retVal = new ArrayList();
        
        SearchRequest searchRequest = new SearchRequest(indexName);

        NestedQueryBuilder nestedInside = SearchIndex.nestedBoolQuery(propertyValues, nestedPath);
        NestedQueryBuilder outsideOutside = QueryBuilders.nestedQuery(nestedPathOutside, nestedInside, ScoreMode.None);  
        
        BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();
        boolBuilder.must(QueryBuilders.termQuery(fieldName, fieldValue));
        boolBuilder.must(outsideOutside);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
        sourceBuilder.query(boolBuilder);
        sourceBuilder.from(from); 
        sourceBuilder.size(size);
        
        searchRequest.indices(indexName);
        searchRequest.source(sourceBuilder);
        
        RestHighLevelClient client = ElasticsearchRestClient.getClient();

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
    System.out.println(searchResponse.toString());

        SearchHits hits = searchResponse.getHits();
        if( hits != null) {
            SearchHit[] searchHits = hits.getHits();
            for(int i = 0; i < searchHits.length; i++) {
                SearchHit hit = searchHits[i];
                retVal.add(new JSONObject(hit.getSourceAsString()));
            }
        }    
        
        client.close();

        return retVal;
   
    }     
    
    public static NestedQueryBuilder nestedBoolQuery(final Map<String, String> propertyValues, final String nestedPath) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Iterator<String> iterator = propertyValues.keySet().iterator();

        while (iterator.hasNext()) {
            String propertyName = iterator.next();
            String propertValue = propertyValues.get(propertyName);
            MatchQueryBuilder matchQuery = QueryBuilders.matchQuery(propertyName, propertValue);
            boolQueryBuilder.must(matchQuery);
        }

        return QueryBuilders.nestedQuery(nestedPath, boolQueryBuilder, ScoreMode.None);
    }    
    
}
