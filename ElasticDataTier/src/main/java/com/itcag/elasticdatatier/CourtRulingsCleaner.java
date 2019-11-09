package com.itcag.datatier;

import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.CourtRulingsSchema;

public class CourtRulingsCleaner {

    public static void cleanIndex() throws Exception {
        
        Reindex.reindex(Indices.COURT_RULINGS.getFieldName(), CourtRulingsSchema.get().toString());
        
    }        
    
    
}
