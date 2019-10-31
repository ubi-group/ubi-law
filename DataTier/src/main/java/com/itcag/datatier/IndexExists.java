package com.itcag.datatier;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.GetIndexRequest;

public class IndexExists {

    public static boolean existsIndex(RestHighLevelClient client, String indexName) throws Exception {
    
        GetIndexRequest request = new GetIndexRequest(indexName); 
        boolean exists = client.indices().exists(request, RequestOptions.DEFAULT);
        return exists;
    }    
    
}
