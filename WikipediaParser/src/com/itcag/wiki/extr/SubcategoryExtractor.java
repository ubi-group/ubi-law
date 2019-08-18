package com.itcag.wiki.extr;

import com.itcag.wiki.Config;
import com.itcag.wiki.lang.Link;
import com.itcag.wiki.lang.Page;
import com.itcag.wiki.lang.Topic;
import com.itcag.wiki.pedia.WikiCategoryPages;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Retrieves all sub-categories recursively for up to three generations.
 */
public final class SubcategoryExtractor {

    private final Topic topic;
    
    public SubcategoryExtractor(Topic topic) {
        this.topic = topic;
    }
    
    public final void insertSubcategories() throws Exception {
        
        Double weight = Config.INITIAL_SUBCATEGORY_WEIGHT;

        HashSet<String> processedCategory = new HashSet<>();
        processedCategory.add(topic.getId());
        
        ArrayList<Link> links = getSubcategories(topic.getTopicPage());
        filter(links);
        while (!links.isEmpty()) {
            
            insertIntoTopic(links, weight);
            
            weight -= Config.SUBCATEGORY_WEIGHT_DECREASE;
            if (weight < Config.SUBCATEGORY_THRESHOLD) break;
            
            ArrayList<Link> tmp = links; 
            links = new ArrayList<>();
            for (Link link : tmp) {
                if (!Page.Type.CATEGORY.equals(link.getTail().getType())) continue;
                if (processedCategory.contains(link.getTail().getId())) continue;
                processedCategory.add(link.getTail().getId());
                links.addAll(getSubcategories(link.getTail()));
            }
            
            filter(links);
        
        }

    } 
    
    private ArrayList<Link> getSubcategories(Page parent) throws Exception {
        
        ArrayList<Link> retVal = new ArrayList<>();
        
        WikiCategoryPages collector = new WikiCategoryPages();
        collector.download(parent.getId(), true);
        for (Entry<String, Page> entry : collector.getPages().entrySet()) {
            Link link = new Link(parent, entry.getValue(), 1.0);
            retVal.add(link);
        }
        
        return retVal;
    
    }

    private void filter(ArrayList<Link> links) {
        
        /**
         * Some sub-categories produce useless sub-categories,
         * and are better ignored.
         */
        Iterator<Link> pageIterator = links.iterator();
        while (pageIterator.hasNext()) {
            Link link =  pageIterator.next();
            String title = link.getTail().getTitle().toLowerCase();
            if (title.contains(" in ")) {
                pageIterator.remove();
            } else if (title.endsWith(" by country")) {
                pageIterator.remove();
            } else if (title.endsWith(" by region")) {
                pageIterator.remove();
            } else if (title.endsWith(" by continent")) {
                pageIterator.remove();
            } else if (title.endsWith(" by nationality")) {
                pageIterator.remove();
            } else if (title.startsWith("lists of ")) {
                pageIterator.remove();
            } else if (title.startsWith("lists related to ")) {
                pageIterator.remove();
            } else if (title.startsWith("documentary films about ")) {
                pageIterator.remove();
            } else if (title.startsWith("television programs about ")) {
                pageIterator.remove();
            } else if (title.endsWith(" stubs")) {
                pageIterator.remove();
            } else if (title.startsWith("fictional ")) {
                pageIterator.remove();
            } else if (title.endsWith(" ingredients")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" organizations")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" associations")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" companies")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" unions")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" brands")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" manufacturers")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.startsWith("people associated with ")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" artists")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            } else if (title.endsWith(" businesspeople")) {
                topic.addUsefulList(link.getTail());
                pageIterator.remove();
            }
        }
        
    }
    
    private void insertIntoTopic(ArrayList<Link> links, Double weight) throws Exception {
        for (Link link : links) {
            topic.addPage(link.getTail());
            topic.addLink(link);
            /**
             * All sub-categories must be linked to the topic.
             * This includes sub-categories that are not directly
             * under the topic. 
             */
            topic.addLink(new Link(topic.getTopicPage(), link.getTail(), weight));
        }
    }
    
}
