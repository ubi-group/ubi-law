package com.itcag.datatier;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.common.xcontent.XContentType;

public class IndexDocument {

    public static String indexDocument(String indexName, String content) throws Exception {
    
        IndexRequest request = new IndexRequest(indexName); 
        request.source(content, XContentType.JSON); 
        
        IndexResponse indexResponse = ElasticsearchRestClient.getClient().index(request, RequestOptions.DEFAULT);

        return indexResponse.getId();
   
    }    
    
}
