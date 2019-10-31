package com.itcag.datatier;

import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.CorrectionsSchema;
import org.elasticsearch.client.RestHighLevelClient;

public class Reindex {

    public static void reindex(String indexName, String mapping) throws Exception {
        
        RestHighLevelClient client = ElasticsearchRestClient.getClient();
        boolean indexExists = IndexExists.existsIndex(client, indexName);
        
        if(indexExists) {
            DeleteIndex.deleteIndex(client, indexName);
        }
        
        String retVal = CreateIndex.createIndex(client, indexName, mapping);
        System.out.println(retVal);
        
        client.close();

    }      
    
}
