package com.itcag.dlutil.parse;

public class CourtRulingParser extends Parser {
    
    public CourtRulingParser(int maxNumberOfParagraphs, int maxTextSize) {
        
        super(maxNumberOfParagraphs, maxTextSize);
    
        setTriggers();
        setSkippers();
        setStoppers();
        
    }
    
    private void setTriggers() {
        super.triggers.add("פסק-דין");
        super.triggers.add("פסק - דין");
        super.triggers.add("פסק- דין");
        super.triggers.add("פסק דין");
    }
    
    private void setSkippers() {
        super.skippers.add("פתח דבר");
        super.skippers.add("תוכן עניינים");
        super.skippers.add("השופט");
        super.skippers.add("שופט");
        super.skippers.add("המשנה ל");
        super.skippers.add("משנה ל");
        super.skippers.add("הנשיא");
        super.skippers.add("נשיא");
    }
    
    private void setStoppers() {
        super.stoppers.add("ניתן ביום");
        super.stoppers.add("ניתן היום");
        super.stoppers.add("ניתן והודע ביום");
        super.stoppers.add("ניתן ושומע בתאריך");
        super.stoppers.add("ניתן ותוקן היום");
        super.stoppers.add("ניתנה ביום");
        super.stoppers.add("ניתנה היום");
        super.stoppers.add("תוקן ביום");
        super.stoppers.add("תוקן היום");
        super.stoppers.add("תוקנה ביום");
        super.stoppers.add("תוקנה היום");
        super.stoppers.add("תוקן על פי");
        super.stoppers.add("ה ש ו פ ט");
        super.stoppers.add("ש ו פ ט");
        super.stoppers.add("ה מ ש נ ה");
        super.stoppers.add("מ ש נ ה");
        super.stoppers.add("ה נ ש י א");
        super.stoppers.add("נ ש י א");
        super.stoppers.add("__________");
    }
    
}
