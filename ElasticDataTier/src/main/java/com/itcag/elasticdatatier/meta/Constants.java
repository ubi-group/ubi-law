package com.itcag.datatier.meta;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Elasticsearch parameters
 * @author IT Consulting Alicja Grużdź
 */
public final class Constants {

    public final static String SETTINGS = "settings";
    public final static String PROPERTIES = "properties";
    public final static String MAPPINGS = "mappings";
    public final static String TYPE = "type";
    public final static String INDEX = "index";
    public final static String ANALYZER = "analyzer";
    public final static String ANALYSIS = "analysis";
    public final static String FROM = "from";
    public final static String SIZE = "size";
    public final static String VERSION = "version";
    public final static String FILTERED = "filtered";
    public final static String EXISTS = "exists";
    public final static String MATCH = "match";
    public final static String MINIMUM_SHOULD_MATCH = "minimum_should_match";
    public final static String QUERY = "query";
    public final static String BUCKETS = "buckets";    
    public final static String KEY = "key"; 
    public final static String MATCH_ALL = "match_all";
    public final static String FIELDS = "fields";
    public final static String FIELD = "field";
    public final static String TERMS = "terms";
    public final static String TERM = "term";
    public final static String AGGREGATIONS = "aggregations";
    public final static String AGGS = "aggs";
    public final static String SORT = "sort";
    public final static String ORDER = "order";
    public final static String MODE = "mode";
    public final static String DESC = "desc";
    public final static String ASC = "asc";
    public final static String NESTED = "nested";
    public final static String PATH = "path";
    public final static String SCORE_MODE = "score_mode";
    public final static String MAX = "max";
    public final static String CTX_SOURCE = "ctx._source";
    public final static String SCRIPT = "script";
    public final static String INLINE = "inline";
    public final static String BOOL = "bool";
    public final static String MUST = "must";
    public final static String MUST_NOT = "must_not";
    public final static String SHOULD = "should";
    public final static String RANGE = "range";
    public final static String LT = "lt";
    public final static String GT = "gt";
    public final static String SHARDS = "_shards";
    public final static String SUCCESSFUL = "successful";   
    public final static String STANDARD = "standard";
    public final static String LOWERCASE = "lowercase";
    public final static String STOP = "stop";
    public final static String PORTER_STEM = "porter_stem";
    public final static String STEM = "stem";
    public final static String TOKENIZER = "tokenizer";
    public final static String FILTER = "filter";
    public final static String ITEMS = "items";
    public final static String _SOURCE = "_source";
    public final static String STATUS = "status";
    public final static String _ID = "_id";
    public final static String _VERSION = "_version";
    public final static String _BULK = "_bulk";
    public final static String _TYPE = "_type";
    public final static String _INDEX = "_index";
    public final static String _SEARCH = "_search";
    public final static String _COUNT = "_count";
    public final static String _UPDATE_BY_QUERY = "_update_by_query";
    public final static String _DELETE_BY_QUERY = "_delete_by_query";
    public final static String _UPDATE = "_update";
    public final static String _CLUSTER = "_cluster";
    public final static String _CAT = "_cat";
    public final static String HEALTH = "health";
    public final static String COUNT = "count";
    public final static String DOC = "doc";
    public final static String DOC_COUNT = "doc_count";
    public final static String CLUSTER_STATE_YELLOW = "yellow";
    public final static String CLUSTER_STATE_GREEN = "green";
    public final static String HITS = "hits";
    public final static String SCROLL_VALUE = "20m";
    public final static String SCROLL = "scroll";
    public final static String SCROLL_ID = "scroll_id";
    public final static String _SCROLL_ID = "_scroll_id";                                
    public final static String TOTAL = "total";
    public final static String FOUND = "found";
    public final static String DELETE = "delete";
    public final static String _DEFAULT_ = "_default_";
    public final static String _ALL = "_all";
    public final static String ENABLED = "enabled";
    public final static String INCLUDE_IN_ALL = "include_in_all";
    public final static String DATE_DETECTION = "date_detection";   
    public final static String QUERY_STRING = "query_string"; 
    
    /** Elasticsearch error mapping fields **/
    public final static String ERROR = "error";
    
    public final static String NUMBER_OF_SHARDS = "number_of_shards";
    public final static String NUMBER_OF_REPLICAS = "number_of_replicas";
    
/** Maximal batch size allowed by Elasticsearch */
    public final static int MAX_BATCH_SIZE = 1000;
    public final static int DEFAULT_BATCH_SIZE = 100;
    public final static int DEFAULT_FROM = 0;
    
/** Aggregation names **/
    public final static String AGG_PROTOCOL = "agg_protocol";
    public final static String AGGREGATION_TYPES = "types";
    public final static String AGGREGATION_PROTOCOL_AVG = "protocolAvg";
    public final static String AGGREGATION_PROTOCOL_STAGE_EXEC_TIME_AVG = "protocolStageExecTimeAvg";
    public final static String AGGREGATION_PROTOCOL_STAGE_EXEC_TIME_PER_TEXT_AVG = "protocolStageExecTimePerTextAvg";
    public final static String SUB_AGGREGATION_PROTOCOL_AVG = "subProtocolAvg";
    public final static String SUB_AGGREGATION_PROTOCOL_STAGE_EXEC_TIME_AVG = "subProtocolStageExecTimeAvg";
    public final static String SUB_AGGREGATION_PROTOCOL_STAGE_EXEC_TIME_PER_TEXT_AVG = "subProtocolStageExecTimePerTextAvg";
    public final static String AGGREGATION_UNIQUE_SEARCH_RESULT_AVG = "uniqueSearchResultAvg";
    public final static String AGGREGATION_SEARCH_RESULT_AVG = "searchResultAvg";
    public final static String AGGREGATION_EXEC_TIME_AVG = "execTimeAvg";
    public final static String SUB_AGGREGATION_UNIQUE_SEARCH_RESULT_AVG = "subUniqueSearchResultAvg";
    public final static String SUB_AGGREGATION_SEARCH_RESULT_AVG = "subSearchResultAvg";
    public final static String SUB_AGGREGATION_EXEC_TIME_AVG = "subExecTimeAvg";
    public final static String SUB_AGGREGATION_STATS_PROTOCOL_NAME = "subStatsProtocolName";
    public final static String SUB_AGGREGATION_STATS_PROTOCOL_STAGE_NAME = "subStatsProtocolStageName";    
    public final static String AGGREGATION_MAX_TIME = "maxTime";    

    
}