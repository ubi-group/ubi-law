package com.itcag.datatier;

import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

public class Tester {

    public static void main(String[] args) throws Exception {
        
//        CorrectionsCleaner.cleanIndex();
//        DataTrainingCleaner.cleanIndex();
//        CategoriesCleaner.cleanIndex();
//        CourtRulingsCleaner.cleanIndex();

SearchRequest searchRequest = new SearchRequest("court_rulings");

SearchSourceBuilder sourceBuilder = new SearchSourceBuilder(); 
sourceBuilder.query(QueryBuilders.termQuery("id", "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\18\\400\\077\\z04&fileName=18077400.Z04&type=2"));
sourceBuilder.from(0); 
sourceBuilder.size(5);

SearchResponse searchResponse = ElasticsearchRestClient.getClient().search(searchRequest, RequestOptions.DEFAULT);

SearchHits hits = searchResponse.getHits();
TotalHits totalHits = hits.getTotalHits();
// the total number of hits, must be interpreted in the context of totalHits.relation
long numHits = totalHits.value;
SearchHit[] searchHits = hits.getHits();

System.out.println("WYNIKI:");
for (SearchHit hit : searchHits) {
String sourceAsString = hit.getSourceAsString();
System.out.println(sourceAsString);
}
System.out.println("~~~~~~~~~~~~~~~~");

    }   
    
}
