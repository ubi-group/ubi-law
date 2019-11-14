package com.itcag.datatier;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.xcontent.XContentType;

public class CreateIndex {
    
    public static String createIndex(RestHighLevelClient client, String indexName, String mapping) throws Exception {
    
        CreateIndexRequest request = new CreateIndexRequest(indexName);
      
        request.mapping(mapping, XContentType.JSON);
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT); 
        
        return createIndexResponse.index();
   
    }
    
}
