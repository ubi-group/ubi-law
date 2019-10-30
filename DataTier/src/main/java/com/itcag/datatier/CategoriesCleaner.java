package com.itcag.datatier;

import com.itcag.datatier.meta.Indices;
import com.itcag.datatier.schema.CategoriesSchema;

public class CategoriesCleaner {

    public static void cleanIndex() throws Exception {
        
        Reindex.reindex(Indices.CATEGORIES.getFieldName(), CategoriesSchema.get().toString());
        
    }     
    
}
