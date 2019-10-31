/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.datatier.meta;

import com.itcag.datatier.meta.Constants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StemMapping {
    
    public static JSONObject getMappingSettings() throws JSONException {
        JSONArray jsonArrayFilter = new JSONArray();
        jsonArrayFilter.put(Constants.STANDARD);
        jsonArrayFilter.put(Constants.LOWERCASE);
        jsonArrayFilter.put(Constants.STOP);
        jsonArrayFilter.put(Constants.PORTER_STEM);

        JSONObject jsonQuerySettings = new JSONObject()
                        .put(Constants.ANALYZER, new JSONObject()
                                .put(Constants.STEM, new JSONObject()
                                        .put(Constants.TOKENIZER, Constants.STANDARD)
                                        .put(Constants.FILTER, jsonArrayFilter)
                                )
                        );

        return jsonQuerySettings;
    }
    
}
