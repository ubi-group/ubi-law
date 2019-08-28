package com.itcag.wiki.extr;

import com.itcag.wiki.Config;
import com.itcag.wiki.lang.Page;
import com.itcag.wiki.lang.Topic;
import com.itcag.wiki.pedia.WikiCategoryPages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * Retrieves all sub-categories recursively without termination.
 * The retrieved categories are lists of brands, companies, ingredients, people, etc.
 */
public final class UsefulListExtractor {

    private final Topic topic;
    
    public UsefulListExtractor(Topic topic) {
        this.topic = topic;
    }
    
    public final void insertUsefulLists() throws Exception {
        
        Double weight = Config.INITIAL_SUBCATEGORY_WEIGHT;

        HashSet<String> processedCategory = new HashSet<>();
        processedCategory.add(topic.getId());

        ArrayList<Page> lists = new ArrayList<>();
        
        for (Page list : topic.getUsefulLists().values()) {
            
            ArrayList<Page> results = getSubcategories(list);

            while (!results.isEmpty()) {

                lists.addAll(results);
            
                ArrayList<Page> tmp = results; 
                results = new ArrayList<>();
                for (Page sublist : tmp) {
                    if (!Page.Type.CATEGORY.equals(sublist.getType())) continue;
                    if (processedCategory.contains(sublist.getId())) continue;
                    processedCategory.add(sublist.getId());
                    lists.addAll(getSubcategories(sublist));
                }
            
            }
            
        }

        insertIntoTopic(lists);

    } 
    
    private ArrayList<Page> getSubcategories(Page parent) throws Exception {
        
        ArrayList<Page> retVal = new ArrayList<>();
        
        WikiCategoryPages collector = new WikiCategoryPages();
        collector.download(parent.getId(), true);
        for (Entry<String, Page> entry : collector.getPages().entrySet()) {
            retVal.add(entry.getValue());
        }
        
        return retVal;
    
    }

    private void insertIntoTopic(ArrayList<Page> lists) throws Exception {
        for (Page list : lists) {
            topic.addUsefulList(list);
        }
    }
    
}
