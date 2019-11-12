package com.itcag.datatier;

import com.itcag.datatier.meta.Constants;
import org.json.JSONException;
import org.json.JSONObject;

public class Queries {

    public final static JSONObject getScrollRequest(String scroll_id) throws JSONException {
        
        JSONObject obj = new JSONObject();
                
        obj.put(Constants.SCROLL_ID, scroll_id);
        obj.put(Constants.SCROLL, Constants.SCROLL_VALUE);
                
        return obj;
        
    }    
    
}
