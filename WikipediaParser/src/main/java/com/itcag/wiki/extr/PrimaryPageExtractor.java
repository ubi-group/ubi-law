package com.itcag.wiki.extr;

import com.itcag.wiki.Config;
import com.itcag.wiki.lang.Link;
import com.itcag.wiki.lang.Page;
import com.itcag.wiki.lang.Topic;
import com.itcag.wiki.pedia.WikiCategoryPages;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Extracts regular pages that belong to the topic.
 * Pages that belong to the sub-categories are not extracted.
 */
public final class PrimaryPageExtractor {
    
    private final Topic topic;
    
    public PrimaryPageExtractor(Topic topic) {
        this.topic = topic;
    }
    
    public final void insertPages() throws Exception {
        
        Double weight = Config.PAGE_BASIC_WEIGHT;

        HashMap<String, Page> pages = getPages(topic.getId());
        filter(pages);
        insertIntoTopic(pages, weight);
        
    }
    
    private HashMap<String, Page> getPages(String pageId) throws Exception {
        
        HashMap<String, Page> retVal = new HashMap<>();
        
        WikiCategoryPages collector = new WikiCategoryPages();
        collector.download(pageId, false);
        for (Map.Entry<String, Page> entry : collector.getPages().entrySet()) {
            if (!retVal.containsKey(entry.getKey())) retVal.put(entry.getKey(), entry.getValue());
        }
        
        return retVal;
    
    }

    private void filter(HashMap<String, Page> pages) {
        
        Iterator<Map.Entry<String, Page>> pageIterator = pages.entrySet().iterator();
        while (pageIterator.hasNext()) {
            Map.Entry<String, Page> entry =  pageIterator.next();
            String title = entry.getValue().getTitle().toLowerCase();
            if (title.startsWith("draft:")) {
                pageIterator.remove();
            } else if (title.contains(" in ")) {
                pageIterator.remove();
            }
        }
        
    }
    
    private void insertIntoTopic(HashMap<String, Page> pages, Double weight) throws Exception {
        for (Map.Entry<String, Page> entry : pages.entrySet()) {
            topic.addPage(entry.getValue());
            Link link = new Link(topic.getTopicPage(), entry.getValue(), weight);
            topic.addLink(link);
        }
    }
    
}
