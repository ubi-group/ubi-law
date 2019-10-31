/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.datatier;

import com.itcag.datatier.schema.CorrectionsSchema;
import com.itcag.datatier.meta.Indices;

public class CorrectionsCleaner {  
     
    public static void cleanIndex() throws Exception {
        
        Reindex.reindex(Indices.CORRECTIONS.getFieldName(), CorrectionsSchema.get().toString());
        
    }        
    
}
