package com.itcag.datatier;

import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

public class DeleteIndex {

    public static boolean deleteIndex(RestHighLevelClient client, String indexName) throws Exception {
    
        DeleteIndexRequest request = new DeleteIndexRequest(indexName); 
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);   
        return deleteIndexResponse.isAcknowledged();
        
    }    
    
}
