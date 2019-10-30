package com.itcag.datatier;

import java.util.ArrayList;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

public class Bulk {
    
    public static boolean index(RestHighLevelClient client, String indexName, ArrayList<String> contents) throws Exception {
    
        BulkRequest request = new BulkRequest();
        
        for(String content: contents) {
            IndexRequest indexRequest = new IndexRequest(indexName); 
            indexRequest.source(content, XContentType.JSON);
        
        }
        
        BulkResponse bulkResponse = client.bulk(request, RequestOptions.DEFAULT);
        
        return bulkResponse.hasFailures();
    }       
    
}
