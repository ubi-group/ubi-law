package com.itcag.split;

import com.google.common.base.Optional;

import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.util.List;

public final class Langdetor {

    private static Langdetor langdetor = new Langdetor();
    
    private final LanguageDetector languageDetector;
    private final TextObjectFactory textObjectFactory;

    private Langdetor() {
        
        try {
            
            List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

            languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
            .withProfiles(languageProfiles)
            .build();

            textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        } catch (Exception ex) {
            
            throw new RuntimeException(ex);
        }
    
    }
    
    public final static Langdetor getInstance() {
        return langdetor;
    }
    
    public final synchronized boolean isEnglish(String text) {
        
        TextObject textObject = textObjectFactory.forText(text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if (!lang.isPresent()) return false;
        return lang.get().getLanguage().equalsIgnoreCase("en");
        
    }
    
    public final synchronized String getLanguage(String text) {

        TextObject textObject = textObjectFactory.forText(text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        if (!lang.isPresent()) return null;

        return lang.get().getLanguage();

    }

}