package com.itcag.demo;

import com.itcag.legalyzer.Legalyzer;
import com.itcag.legalyzer.util.cat.Categories;

public class LegalyzerFactory {


    private static Categories categories;
    private static Legalyzer legalyzer;   
    private static LegalyzerFactory legalyzerFactory;
  
    private LegalyzerFactory() throws Exception { 
        
        categories = new Categories(Config.CATEGORIES_PATH);
        legalyzer = new Legalyzer(getCategories());
    } 
  
    public static LegalyzerFactory getInstance() throws Exception {
        
        if (legalyzerFactory == null) 
            legalyzerFactory = new LegalyzerFactory(); 
  
        return legalyzerFactory; 
    } 

    public Legalyzer getLegalyzer() {
        
        return legalyzer;
    }

    /**
     * @return the categories
     */
    public static Categories getCategories() {
        return categories;
    }
    
}
