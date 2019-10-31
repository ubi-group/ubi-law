package com.itcag.datatier;

import com.itcag.datatier.meta.Objects;
import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.meta.HTTPMethods;
import com.itcag.datatier.meta.Constants;
import org.json.JSONArray;
import org.json.JSONObject;

public class Toolbox {

    public JSONObject retrieve(JSONObject query, Indices index) throws Exception {
        String url = "/" + index.getFieldName() +"/" + Constants._SEARCH; 
        String strRetVal = Transporter.sendJson(query.toString(), url, HTTPMethods.POST.getFieldValue());
        JSONObject objRetVal = new JSONObject(strRetVal);
        DataToolbox.checkIfContainsError(objRetVal);
        return objRetVal;
    }   
    
    public JSONObject retrieve(JSONObject query, Indices index, Objects object) throws Exception {
        String url = "/" + index.getFieldName() +"/" + object.getFieldName() + "/" + Constants._SEARCH;   
        String strRetVal = Transporter.sendJson(query.toString(), url, HTTPMethods.POST.getFieldValue());
        JSONObject objRetVal = new JSONObject(strRetVal);
        DataToolbox.checkIfContainsError(objRetVal);
        return objRetVal;
    }  
    
    public JSONArray getAll(String url) throws Exception {
        JSONObject json;
        String strResponse = Transporter.sendJson(null, url, HTTPMethods.GET.getFieldValue());
        json = new JSONObject(strResponse);
        DataToolbox.checkIfContainsError(json);
        return DataToolbox.processHits(json);
    }    
    
    public JSONArray getAll(String str, String url) throws Exception {
        JSONObject json;
        String strResponse = Transporter.sendJson(str, url, HTTPMethods.GET.getFieldValue());
        json = new JSONObject(strResponse);
        DataToolbox.checkIfContainsError(json);
        return DataToolbox.processHits(json);
    }   
    
    public static JSONObject removeID(JSONObject obj) {
        if (obj.has(Constants._ID)) 
            obj.remove(Constants._ID);
        return obj;
    }

    public String delete(String url) throws Exception {
        
        String response = Transporter.sendJson(null, url, HTTPMethods.DELETE.getFieldValue());
        DataToolbox.checkIfContainsError(new JSONObject(response)); 
        return response;
        
    }
    
    public String getScrollObject(String scrollId) throws Exception {
        String url = "/" + Constants._SEARCH + "/" + Constants.SCROLL;  
        String strRetVal = Transporter.sendJson(Queries.getScrollRequest(scrollId).toString(), url, HTTPMethods.GET.getFieldValue());
        DataToolbox.checkIfContainsError(new JSONObject(strRetVal));
        return strRetVal;
    }     
    
    public JSONArray concatArray(JSONArray arr1, JSONArray arr2) throws Exception {
        JSONArray result = new JSONArray();
        for (int i = 0; i < arr1.length(); i++) {
            result.put(arr1.get(i));
        }
        for (int i = 0; i < arr2.length(); i++) {
            result.put(arr2.get(i));
        }
        return result;
    }  
    
    public JSONObject retrieveByPOST(JSONObject query, String url) throws Exception {
  
        String strRetVal = Transporter.sendJson(query.toString(), url, HTTPMethods.POST.getFieldValue());
        JSONObject retVal = new JSONObject(strRetVal);
        DataToolbox.checkIfContainsError(retVal);
        return retVal;

    } 
    
    public String deleteMapping(Indices index) throws Exception {
        
        if(index == null)
            throw new NullPointerException("Index provided for deletion is null.");   
        
        String url = "/" + index.getFieldName() + "/";   
        String response = Transporter.sendJson("", url, "DELETE");  
        DataToolbox.checkIfContainsError(new JSONObject(response));  
        return response;
      
    }  
    
    public String putMapping(String mapping, Indices index) throws Exception {
       String url = "/" + index.getFieldName() + "/";   
       String response = Transporter.sendJson(mapping, url, "PUT");        
       DataToolbox.checkIfContainsError(new JSONObject(response));
       return response;
    }    

}