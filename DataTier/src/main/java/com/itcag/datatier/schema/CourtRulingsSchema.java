package com.itcag.datatier.schema;

import com.itcag.datatier.SchemaToolbox;
import com.itcag.datatier.meta.Constants;
import com.itcag.datatier.meta.ESDataTypes;
import com.itcag.datatier.meta.IndexOptions;
import org.json.JSONException;
import org.json.JSONObject;

public class CourtRulingsSchema {

    public final static JSONObject get() throws JSONException {

        JSONObject retVal = new JSONObject();

        JSONObject properties = new JSONObject();
        properties.put(DocumentFields.id.getFieldName(), SchemaToolbox.getFieldSettings(ESDataTypes.KEYWORD, IndexOptions.TRUE));
 
        retVal.put(Constants.PROPERTIES, properties);

        return retVal;

    }
    
}
