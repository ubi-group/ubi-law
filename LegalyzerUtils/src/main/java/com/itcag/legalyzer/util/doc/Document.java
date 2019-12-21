package com.itcag.legalyzer.util.doc;

import com.itcag.legalyzer.util.doc.extr.CourtRuling;
import com.itcag.legalyzer.util.doc.extr.Person;
import com.itcag.legalyzer.util.doc.extr.Law;
import com.itcag.legalyzer.util.cat.Category;
import com.itcag.legalyzer.util.doc.extr.penalty.Penalty;
import com.itcag.legalyzer.util.parse.Parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.TreeMap;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONObject;

public class Document extends Aggregator {

    private enum Fields {

        ID("id"),
        LINES("lines"),
        PARAGRAPHS("paragraphs"),
        
        ;
        
        private final String name;
        
        private Fields(String name) {
            this.name = name;
        }
        
        private String getName() {
            return this.name;
        }
        
    }
    
    public enum Type {
        HIGH_COURT_RULING,
        CRIMINAL_RULING,
    }
    
    private final String id;
    
    private final ArrayList<String> lines;
    
    private ArrayList<Paragraph> paragraphs = new ArrayList<>();
    
    private final ArrayList<Person> judges = new ArrayList<>();
    private final ArrayList<Person> plaintiffAttorneys = new ArrayList<>();
    private final ArrayList<Person> defendantAttorneys = new ArrayList<>();
    
    /**
     * Key = official name,
     * Value = law.
     */
    private final LinkedHashMap<String, Law> laws = new LinkedHashMap<>();
    private final LinkedHashMap<String, Law> unknownlaws = new LinkedHashMap<>();

    /**
     * Key =  code,
     * Value = ruling.
     */
    private final LinkedHashMap<String, CourtRuling> rulings = new LinkedHashMap<>();
    
    private final ArrayList<Penalty> penalties = new ArrayList<>();
    
    public Document(ArrayList<String> lines, Parser parser) throws Exception {
        this.id = UUID.randomUUID().toString().replace("-", "");
        this.lines = lines;
        this.paragraphs = parser.parse(this.lines);
    }

    public Document(String id, ArrayList<String> lines, Parser parser) throws Exception {
        this.id = id;
        this.lines = lines;
        this.paragraphs = parser.parse(this.lines);
    }

    public Document(JSONObject jsonObject) throws Exception {
        this.id = jsonObject.getString(Fields.ID.getName());
        
        {
            this.lines = new ArrayList<>();
            JSONArray jsonArray = jsonObject.getJSONArray(Fields.LINES.getName());
            for (int i = 0; i < jsonArray.length(); i++) {
                this.lines.add(jsonArray.getString(i));
            }
        }
        
        {
            JSONArray jsonArray = jsonObject.getJSONArray(Fields.PARAGRAPHS.getName());
            for (int i = 0; i < jsonArray.length(); i++) {
                Paragraph paragraph = new Paragraph(jsonArray.getJSONObject(i));
                this.paragraphs.add(paragraph);
            }
        }
        
    }
    
    public String getId() {
        return this.id;
    }
    
    public ArrayList<Paragraph> getParagraphs() {
        return this.paragraphs;
    }
    
    public ArrayList<Person> getJudges() {
        return judges;
    }

    public void addJudge(Person judge) {
        this.judges.add(judge);
    }

    public ArrayList<Person> getPlaintiffAttorneys() {
        return plaintiffAttorneys;
    }

    public void addPlaintiffAttorney(Person plaintiffAttorney) {
        this.plaintiffAttorneys.add(plaintiffAttorney);
    }

    public ArrayList<Person> getDefendantAttorneys() {
        return defendantAttorneys;
    }

    public void addDefendantAttorney(Person defendantAttorney) {
        this.defendantAttorneys.add(defendantAttorney);
    }
    
    public LinkedHashMap<String, Law> getLaws() {
        return this.laws;
    }
    
    public void addLaw(Law law) {
        if (law.getOfficialName() == null) {
            /**
             * This is a guess, and guesses are kept separate,
             * even if they have the same name.
             * Therefore, they get GUIDs just to keep them separate.
             */
            this.laws.put(UUID.randomUUID().toString().replace("-", ""), law);
        } else {
            this.laws.put(law.getOfficialName(), law);
        }
    }
    
    public LinkedHashMap<String, Law> getUnknownLaws() {
        return this.unknownlaws;
    }
    
    public void addUnknownLaw(Law law) {
        this.unknownlaws.put(law.getName(), law);
    }
    
    public LinkedHashMap<String, CourtRuling> getRulings() {
        return this.rulings;
    }
    
    public void addRuling(CourtRuling ruling) {
        this.rulings.put(ruling.getCode(), ruling);
    }
    
    public ArrayList<Penalty> getPenalties() {
        return this.penalties;
    }
    
    public void addPenalty(Penalty penalty) {
        this.penalties.add(penalty);
    }
    
    public LinkedHashMap<Integer, Category> getEvaluation(TreeMap<Integer, Category> categories) {
        return this.aggregate(new ArrayList<>(this.paragraphs), categories);
    }
    
    public JSONObject getJSON() {
        
        JSONObject retVal = new JSONObject();
        
        retVal.put(Fields.ID.getName(), this.id);
        
        {
            JSONArray jsonArray = new JSONArray();
            for (String line : getLines()) {
                jsonArray.put(line);
            }
            retVal.put(Fields.LINES.getName(), jsonArray);
        }
        
        {
            JSONArray jsonArray = new JSONArray();
            for (Paragraph paragraph : paragraphs) {
                jsonArray.put(paragraph.getJSON());
            }
            retVal.put(Fields.PARAGRAPHS.getName(), jsonArray);
        }
        
        return retVal;
        
    }

    /**
     * @return the lines
     */
    public ArrayList<String> getLines() {
        return lines;
    }
    
}
