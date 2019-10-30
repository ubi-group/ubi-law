package com.itcag.datatier;

import com.itcag.datatier.meta.IndexOptions;
import com.itcag.datatier.meta.StemMapping;
import com.itcag.datatier.meta.ESDataTypes;
import com.itcag.datatier.meta.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public final class SchemaToolbox {

    public final static JSONObject getFieldSettings(ESDataTypes type, IndexOptions index) throws JSONException {

        JSONObject retVal = new JSONObject();

        retVal.put(Constants.TYPE, type.getFieldValue());
        
        retVal.put(Constants.INDEX, index.getFieldValue());
        
        if(type == ESDataTypes.TEXT && index == IndexOptions.TRUE)
            retVal.put("analyzer", "stem");
                     
        return retVal;

    }

    public final static JSONObject getFieldSettings(ESDataTypes type, boolean index) throws JSONException {

        JSONObject retVal = new JSONObject();

        retVal.put(Constants.TYPE, type.getFieldValue());
        
        retVal.put(Constants.INDEX, index);
        
        return retVal;

    }
      
    public final static JSONObject getNestedSettings(JSONObject properties) throws JSONException {

        JSONObject retVal = new JSONObject();

        retVal.put(Constants.TYPE, ESDataTypes.NESTED.getFieldValue());
        retVal.put(Constants.PROPERTIES, properties);

        return retVal;

    }
    
    public final static JSONObject addNestedField(JSONObject obj) throws JSONException {


        obj.put(Constants.TYPE, ESDataTypes.NESTED.getFieldValue());

        return obj;

    }    

    public final static JSONObject getSettings() throws JSONException {

        JSONObject retVal = new JSONObject();

        retVal.put(Constants.NUMBER_OF_SHARDS, "2"); 
        retVal.put(Constants.NUMBER_OF_REPLICAS, "1"); 
        retVal.put(Constants.ANALYSIS, StemMapping.getMappingSettings());

        return retVal;

    }

}