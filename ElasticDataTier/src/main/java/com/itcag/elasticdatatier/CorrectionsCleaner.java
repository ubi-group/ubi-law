package com.itcag.datatier;

import com.itcag.datatier.schema.CorrectionsSchema;
import com.itcag.datatier.meta.Indices;

public class CorrectionsCleaner {  
     
    public static void cleanIndex() throws Exception {
        
        Reindex.reindex(Indices.CORRECTIONS.getFieldName(), CorrectionsSchema.get().toString());
        
    }        
    
}
