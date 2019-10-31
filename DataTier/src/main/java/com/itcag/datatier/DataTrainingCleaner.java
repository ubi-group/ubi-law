package com.itcag.datatier;

import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.CorrectionsSchema;

public class DataTrainingCleaner {
 
    public static void cleanIndex() throws Exception {
        
        Reindex.reindex(Indices.DATA_TRAINING.getFieldName(), CorrectionsSchema.get().toString());
        
    }       
}
