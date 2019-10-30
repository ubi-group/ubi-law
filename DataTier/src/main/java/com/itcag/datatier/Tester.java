package com.itcag.datatier;

public class Tester {

    public static void main(String[] args) throws Exception {
        
        CorrectionsCleaner.cleanIndex();
        DataTrainingCleaner.cleanIndex();
        CategoriesCleaner.cleanIndex();

    }   
    
}
