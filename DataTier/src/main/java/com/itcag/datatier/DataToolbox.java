package com.itcag.datatier;

import com.itcag.datatier.meta.Objects;
import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.meta.Constants;
import com.itcag.datatier.meta.BulkOperations;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.ProtocolException;

/**
 * Auxiliary methods for Elasticsearch JSON formatted responses
 * @author IT Consulting Alicja Grużdź
 */
public final class DataToolbox {
    
    public static JSONObject getResultSingle(JSONArray arrJSON, String msg) throws Exception  {

        if(arrJSON == null) {
            throw new NullPointerException("The document array is null. " + msg);
        }

        if(arrJSON.length() == 0) {
            throw new Exception("The result is empty. " + msg);
        }

        if(arrJSON.length() > 1) {
            throw new Exception("There is more than one document matching where single result is expected. " + msg);   
        }
        
        return arrJSON.getJSONObject(0);
    }
    
    private static JSONObject getInnerBulkObject(Indices index, Objects object, String _id) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(Constants._INDEX, index.getFieldName());
        obj.put(Constants._TYPE, object.getFieldName());
        obj.put(Constants._ID, _id);
        return obj;
    }
    
    private static JSONObject getBulkObject(String action, Indices index, Objects object, String _id) throws JSONException {
        JSONObject obj = new JSONObject();
        obj.put(action, getInnerBulkObject(index, object, _id));
        return obj;
    }
    
    public static String getBulkDeleteObjects(JSONArray array, Indices index, Objects object) throws Exception {
        if(array == null)
            throw new NullPointerException("Array provided for _bulk insertion is null.");         
        
        if(array.length() == 0)
            throw new Exception("Empty data provided in _bulk deletion request.");
            
        String strBulk = "";
        for(int i = 0; i < array.length(); i++) {
            String _id = array.getJSONObject(i).getString(Constants._ID);
            JSONObject objIndex = getBulkObject(Constants.DELETE, index, object, _id);
            strBulk += objIndex + System.getProperty("line.separator");            
        }
        return strBulk;
    }
    
    public static String getBulkInsert(JSONArray array, String index, String type) throws Exception {

        if(array == null)
            throw new NullPointerException("Array provided for _bulk insertion is null.");         
        
        String strBulk = "";
        for(int i = 0; i < array.length(); i++) {
            JSONObject objIndex = new JSONObject();
            objIndex.put("index", new JSONObject()
                    .put("_index", index)
                    .put("_type", type));
            JSONObject obj = array.getJSONObject(i);
            if(obj.has("_id")) {
                obj.remove("_id");
            }            
            strBulk += objIndex.toString() + System.getProperty("line.separator") + obj.toString();
            if (i != array.length() -1)
                strBulk += System.getProperty("line.separator");      
            
        }
        return strBulk;
    }      
    
    public static String getBulkUpdate(JSONArray array, String index, String type) throws Exception {

        if(array == null)
            throw new NullPointerException("Array provided for _bulk insertion is null.");         
        
        String strBulk = "";
        for(int i = 0; i < array.length(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String _id = "";
            if(obj.has("_id")) {
                _id = obj.getString("_id");
            }
            JSONObject objIndex = new JSONObject();
            objIndex.put("index", new JSONObject()
                    .put("_index", index)
                    .put("_id", _id)
                    .put("_type", type));
            
            obj.remove("_id");
            strBulk += objIndex.toString() + System.getProperty("line.separator") + obj.toString();
            if (i != array.length() -1)
                strBulk += System.getProperty("line.separator");      
            
        }
        
        return strBulk;
    }          
    
    public static JSONArray getHitsArray(JSONObject json) throws Exception {

        if (!json.has(Constants.HITS)) throw new Exception("No hits retrieved.");
        
        JSONArray retVal = json.optJSONArray(Constants.HITS);
        if (retVal == null) throw new Exception("No inner hits retrieved.");

        return retVal;
        
    }  
    
    public static void checkIfContainsError(JSONObject json) throws Exception {
        if(json.has(Constants.ERROR))
            throw new Exception("Elasticsearch threw an exception: " + json);     
    }
    
    public static JSONObject getSource(JSONObject json) throws Exception {
        if(!json.has(Constants._SOURCE))
            throw new Exception("Elasticsearch response does not contain field: " + Constants._SOURCE); 
        
        return json.getJSONObject(Constants._SOURCE);
    }
    
    public static boolean isFound(JSONObject json) throws Exception {
        if(!json.has(Constants.FOUND))
            throw new Exception("Elasticsearch response does not contain field: " + Constants.FOUND);     
    
        return json.getBoolean(Constants.FOUND);
    }
    
    public static JSONObject getHits(JSONObject json) throws Exception {
        checkIfContainsError(json);
        
        if(!json.has(Constants.HITS))
            throw new Exception("Elasticsearch response does not contain " + Constants.HITS + ": " + json);
        
        JSONObject objHits = json.optJSONObject(Constants.HITS);
        return objHits;
    }
    
    public static JSONArray processHits(JSONObject json) throws Exception {
        
        JSONObject objHits = getHits(json);
        
        if(!objHits.has(Constants.TOTAL))
            throw new Exception("Elasticsearch does not contain " + Constants.TOTAL + ": " + json);

        int total = objHits.getInt(Constants.TOTAL);
        if(total == 0)
            throw new Exception("Elasticsearch returned an empty result set.");
                    
        JSONArray hits = DataToolbox.getHitsArray(objHits);       
        JSONArray retVal = new JSONArray();
        for (int i = 0; i < hits.length(); i++) {
            JSONObject hit = hits.optJSONObject(i);

            if (hit == null) throw new Exception("Invalid JSONArray element (not JSONObject).");
            
            JSONObject resource = getSource(hit);
            
            if(!hit.has(Constants._ID))
                throw new Exception("Elasticsearch response does not contain " + Constants._ID + ": " + json);
                
            String _id = hit.getString(Constants._ID);
            resource.put(Constants._ID, _id);

            retVal.put(resource);        
        }
        return retVal;
    }
    
    public static JSONArray processHitsFromScroll(JSONObject json) throws Exception {
       
        JSONObject objHits = getHits(json);
        
        if(!objHits.has(Constants.TOTAL))
            throw new Exception("Elasticsearch does not contain " + Constants.TOTAL + ": " + json);

        int total = objHits.getInt(Constants.TOTAL);
        if(total == 0)
            throw new Exception("Elasticsearch returned an empty result set.");
                    
        JSONArray hits = DataToolbox.getHitsArray(objHits);       
        JSONArray retVal = new JSONArray();
        for (int i = 0; i < hits.length(); i++) {
            JSONObject hit = hits.optJSONObject(i);

            if (hit == null) throw new Exception("Invalid JSONArray element (not JSONObject).");

            retVal.put(hit);        
        }
        return retVal;
    }   

    public static JSONArray processFullHitsFromScroll(JSONObject json) throws Exception {
       
        JSONObject objHits = getHits(json);
        
        if(!objHits.has(Constants.TOTAL))
            throw new Exception("Elasticsearch does not contain " + Constants.TOTAL + ": " + json);

        int total = objHits.getInt(Constants.TOTAL);
        if(total == 0)
            throw new Exception("Elasticsearch returned an empty result set.");
                    
        JSONArray hits = DataToolbox.getHitsArray(objHits);       
        JSONArray retVal = new JSONArray();
        for (int i = 0; i < hits.length(); i++) {
            JSONObject hit = hits.optJSONObject(i);

            if (hit == null) throw new Exception("Invalid JSONArray element (not JSONObject).");
            
            JSONObject resource = getSource(hit);
            
            if(!hit.has(Constants._ID))
                throw new Exception("Elasticsearch response does not contain " + Constants._ID + ": " + json);
                
            String _id = hit.getString(Constants._ID);
            resource.put(Constants._ID, _id);

            retVal.put(resource);        
        }
        return retVal;

    }   
    
    public final static String indexesAsPrettyString() {
        String retVal = null;
        for (Indices index : Indices.values()) {
            if (retVal == null) {
                retVal = "'" + index.name() + "'";
            } else {
                retVal += ", '" + index.name() + "'";
            }
        }
        return retVal;
    }
    
    public final static int getSuccessfulCount(JSONObject bulkResponse, BulkOperations operation) throws Exception {
        if(bulkResponse == null) throw new NullPointerException("Bulk response is null.");
        
        int successful = 0;
        if(!bulkResponse.has(Constants.ITEMS)) throw new Exception("Bulk response does not contain '" + Constants.ITEMS + "'");
        JSONArray items = bulkResponse.getJSONArray(Constants.ITEMS);       
        for(int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            if(item.has(operation.getFieldName())) {
                item = item.getJSONObject(operation.getFieldName());
                if(!item.has(Constants.SHARDS)) throw new Exception("Bulk response does not contain '" + Constants.SHARDS + "'");
                JSONObject _shards = item.getJSONObject(Constants.SHARDS);
                if(!_shards.has(Constants.SUCCESSFUL)) throw new Exception("Bulk response does not contain '" + Constants.SUCCESSFUL + "'");
                int item_successful = _shards.getInt(Constants.SUCCESSFUL);
                successful = successful + item_successful;
            }
        }
        
        return successful;
    }
}