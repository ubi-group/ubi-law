package com.itcag.datatier;

import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

public class DeleteDocument {

    public static void deleteIndex(String indexName, String _id) throws Exception {
        
        RestHighLevelClient client = ElasticsearchRestClient.getClient();
        
        DeleteRequest request = new DeleteRequest(indexName,  _id); 
        DeleteResponse deleteResponse = client.delete(request, RequestOptions.DEFAULT);
        
        client.close();
        
    } 
    
}
