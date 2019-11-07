package com.itcag.legalyzer.util.extract;

import com.itcag.legalyzer.util.doc.Document;
import com.itcag.legalyzer.util.doc.Paragraph;
import com.itcag.legalyzer.util.doc.extr.Person;
import com.itcag.legalyzer.util.doc.Sentence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 *
 */
public class PersonnelExtractor {

    private final HashMap<String, Person.Type> paragraphTriggers = new HashMap<>();
    private final HashSet<String> paragraphSkippers = new HashSet<>();
    private final HashSet<String> paragraphStoppers = new HashSet<>();
    
    private final HashMap<String, String> sentenceTriggers = new HashMap<>();
    
    public PersonnelExtractor(Document.Type documentType) {
        
        switch (documentType) {
            case HIGH_COURT_RULING:
                
                paragraphTriggers.put("בפני:", Person.Type.JUDGE);
                paragraphTriggers.put("לפני:", Person.Type.JUDGE);
                paragraphTriggers.put("בשם המערער", Person.Type.ATTORNEY_PLAINTIFF);
                paragraphTriggers.put("בשם העותר", Person.Type.ATTORNEY_PLAINTIFF);
                paragraphTriggers.put("בשם המבקש", Person.Type.ATTORNEY_PLAINTIFF);
                paragraphTriggers.put("בשם המשיב", Person.Type.ATTORNEY_DEFENDANT);
            
                paragraphSkippers.add("המערער");
                paragraphSkippers.add("העותר");
                paragraphSkippers.add("המבקש");
                paragraphSkippers.add("נ ג ד");
                paragraphSkippers.add("המשיב");
                paragraphSkippers.add("תאריך הישיבה:");
//                paragraphSkippers.add("");
                
                paragraphStoppers.add("פסק דין");
                paragraphStoppers.add("פסק -דין");
                paragraphStoppers.add("פסק - דין");
                paragraphStoppers.add("פסק-דין");
                paragraphStoppers.add("פסק- דין");
                paragraphStoppers.add("גזר דין");
                paragraphStoppers.add("גזר -דין");
                paragraphStoppers.add("גזר - דין");
                paragraphStoppers.add("גזר-דין");
                paragraphStoppers.add("גזר- דין");
//                paragraphStoppers.add("");
                
                break;
            
            case CRIMINAL_RULING:

                paragraphTriggers.put("בפני", Person.Type.JUDGE);
                paragraphTriggers.put("לפני", Person.Type.JUDGE);
                paragraphTriggers.put("המאשימה", Person.Type.ATTORNEY_PLAINTIFF);
                paragraphTriggers.put("נגד", Person.Type.ATTORNEY_DEFENDANT);

                paragraphStoppers.add("הכרעת דין");
                paragraphStoppers.add("הכרעת -דין");
                paragraphStoppers.add("הכרעת - דין");
                paragraphStoppers.add("הכרעת-דין");
                paragraphStoppers.add("הכרעת- דין");
                paragraphStoppers.add("גזר דין");
                paragraphStoppers.add("גזר -דין");
                paragraphStoppers.add("גזר - דין");
                paragraphStoppers.add("גזר-דין");
                paragraphStoppers.add("גזר- דין");
                
                break;

        }
        
        sentenceTriggers.put("כבוד השופטת", "JUDGE");
        sentenceTriggers.put("כבוד השופט", "JUDGE");
        sentenceTriggers.put("כבוד המשנה לנשיאה", "JUDGE");
        sentenceTriggers.put("כבוד המשנה לנשיא", "JUDGE");
        sentenceTriggers.put("כבוד הנשיאה", "JUDGE");
        sentenceTriggers.put("כבוד הנשיא", "JUDGE");
        sentenceTriggers.put("כבוד המשנָה לנשיאה", "JUDGE");
        sentenceTriggers.put("כבוד המשנָה לנשיא", "JUDGE");
        sentenceTriggers.put("כב השופטת", "JUDGE");
        sentenceTriggers.put("כב השופט", "JUDGE");
        sentenceTriggers.put("כב המשנה לנשיאה", "JUDGE");
        sentenceTriggers.put("כב המשנה לנשיא", "JUDGE");
        sentenceTriggers.put("כב הנשיאה", "JUDGE");
        sentenceTriggers.put("כב הנשיא", "JUDGE");
        sentenceTriggers.put("כב המשנָה לנשיאה", "JUDGE");
        sentenceTriggers.put("כב המשנָה לנשיא", "JUDGE");
        sentenceTriggers.put("עו\"ד", "ATTORNEY");
        sentenceTriggers.put("עוה\"ד", "ATTORNEY");
//        triggers.put("", "ATTORNEY");
//        triggers.put("", "ATTORNEY");
//        triggers.put("", "ATTORNEY");
        
    }
    
    public void extract(Document document) {
        
        Person.Type type = null;
        
        for (Paragraph paragraph : document.getParagraphs()) {
            for (Sentence sentence : paragraph.getSentences()) {

                String text = sentence.getText();

                for (String skipper : this.paragraphSkippers) {
                    if (text.contains(skipper)) {
                        type = null;
                        break;
                    }
                }

                for (Map.Entry<String, Person.Type> entry : this.paragraphTriggers.entrySet()) {
                    if (text.contains(entry.getKey())) {
                        type = entry.getValue();
                        break;
                    }
                }

                for (String stopper : this.paragraphStoppers) {
                    if (text.contains(stopper)) {
                        return;
                    }
                }

                if (type == null) continue;
                
                insertPersonnel(sentence, type, document);
                
            }

        }
    
    }

    private void insertPersonnel(Sentence sentence, Person.Type type, Document document) {
        
        String text = sentence.getText();
        
        /**
         * Correction of the type necessitated by the inconsistent document structure.
         */
        if (text.startsWith("ב\"כ המאשי")) type = Person.Type.ATTORNEY_PLAINTIFF;
        if (text.startsWith("ב\"כ הנאש")) type = Person.Type.ATTORNEY_DEFENDANT;
        
        for (Map.Entry<String , String> entry : this.sentenceTriggers.entrySet()) {
            
            String trigger = entry.getKey();
            
            int pos = text.indexOf(trigger);
            
            if (pos == -1) continue;
            
            String name = text.substring(pos);
            name = name.replace(trigger, "").trim();
            
            if (name.contains(",") || name.contains(";") || name.contains(" ו ")) {
                insertMutipleNames(name, type, document);
            } else {
                insertPerson(new Person(name, type), document);
            }
            
            break;
            
        }
        
    }
    
    private void insertMutipleNames(String name, Person.Type type, Document document) {

        name = name.replace(",", "|");
        name = name.replace(";", "|");
        name = name.replace(" ו ", "|");
        
        String[] elts = name.split("\\|");
        
        for (String elt : elts) {
            
            elt = elt.trim();
            if (elt.isEmpty()) continue;
            
            /**
             * Titles added after a comma.
             */
            if (elt.startsWith("סגן הנשיאה")) continue;
            if (elt.startsWith("סגן הנשיא")) continue;
            if (elt.startsWith("הנשיאה")) continue;
            if (elt.startsWith("נשיאת")) continue;
            if (elt.startsWith("הנשיא")) continue;
            
            insertPerson(new Person(elt, type), document);
        
        }
    
    }
        private void insertPerson(Person person, Document document) {
        
        switch (person.getType()) {
            case JUDGE:
                document.addJudge(person);
                break;
            case ATTORNEY_PLAINTIFF:
                document.addPlaintiffAttorney(person);
                break;
            case ATTORNEY_DEFENDANT:
                document.addDefendantAttorney(person);
                break;
        }
        
    }
    
}
