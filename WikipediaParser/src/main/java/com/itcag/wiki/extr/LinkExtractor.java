package com.itcag.wiki.extr;

import com.itcag.util.Encoder;
import com.itcag.wiki.Blacklist;
import com.itcag.wiki.Config;
import com.itcag.wiki.lang.Link;
import com.itcag.wiki.lang.Page;
import com.itcag.wiki.lang.Topic;
import com.itcag.wiki.pedia.WikiPageLinks;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * Extracts all links from the primary pages.
 * These links are then inspected, in order to determine whether
 * they occur frequently, and if so they are used to either increase
 * the weight of the existing links, or to create new links.
 */
public final class LinkExtractor {
    
    private final Topic topic;
    
    private final ArrayList<Link> index = new ArrayList<>();
    private final HashMap<String, Integer> count = new HashMap<>();
        
    private final HashMap<String, Integer> rejects = new HashMap<>();
    
    public LinkExtractor(Topic topic) {
        this.topic = topic;
    }
    
    public final void insertLinks() throws Exception {

        collectLinks();
        selectLinks();
        filter();
        
    }

    private void collectLinks() throws Exception {

        for (Page page : topic.getPages().values()) {
            if (!Page.Type.PAGE.equals(page.getType())) continue;
            HashMap<String, Page> links = getLinks(page.getId());
            for (Page linkedPage : links.values()) {
                if (Blacklist.exists(linkedPage.getId())) continue;
                if (topic.isNavboxLink(linkedPage.getId())) continue;
                index.add(new Link(page, linkedPage, Config.LINK_BASIC_WEIGHT));
                if (count.containsKey(linkedPage.getId())) {
                    count.put(linkedPage.getId(), count.get(linkedPage.getId()) + 1);
                } else {
                    count.put(linkedPage.getId(), 1);
                }
            }
        }
        
    }
    
    private HashMap<String, Page> getLinks(String pageId) throws Exception {
        
        WikiPageLinks collector = new WikiPageLinks();
        collector.download(Encoder.encodeText(pageId));
        
        return collector.getLinks();
    
    }

    private void selectLinks() throws Exception {

        Iterator<Link> indexIterator = index.iterator();
        while (indexIterator.hasNext()) {
            
            Link link = indexIterator.next();
            Page linkedPage = link.getTail();
            int pageCount = count.get(linkedPage.getId());
            
            if (topic.getLinksById(linkedPage.getId()) != null) {
                
                if (pageCount > Config.MIN_LINK_FOO_KNOWN_THRESHOLD) {

                    /**
                     * Increase the weight of the existing links - proportionally
                     * to the frequency of the occurrence of the linked page in the
                     * primary pages.
                     */
                    increaseWeight(linkedPage, pageCount);
                    
                    /**
                     * Add all pages containing this link to the list of links.
                     * The linked page is the tail, while the pages containing are heads.
                     */
                    insertLink(link, Config.LINK_BASIC_WEIGHT);

                } else {
                    
//                    if (!rejects.containsKey(link.getTail().getTitle())) rejects.put(link.getTail().getTitle(), pageCount);
                    
                    /**
                     * Decrease the weight of the existing links.
                     * If these links were not encountered in the primary
                     * pages, then they are probably less relevant.
                     */
                    decreaseWeight(linkedPage);
                    insertLink(link, Config.LINK_MIN_WEIGHT);

                }
            
                indexIterator.remove();
                
            } else {
                if (pageCount > Config.MIN_LINK_FOO_UNKNOWN_THRESHOLD) {
//                    if (!rejects.containsKey(link.getTail().getTitle())) rejects.put(link.getTail().getTitle(), pageCount);
                    insertLink(link, Config.LINK_MIN_WEIGHT);
                }
            }
            
        }

    }
    
    private void increaseWeight(Page linkedPage, int foo) {
        for (Link existing : topic.getLinksById(linkedPage.getId()).values()) {
            if (existing.getWeight() < 1.0) {
                Double newWeight = existing.getWeight() + foo * Config.LINK_FOO_WEIGHT_INCREASE_FACTOR;
                if (newWeight > 1.0) newWeight = 1.0;
                existing.setWeight(newWeight);
            }
        }
    }
    
    private void insertLink(Link link, Double weight) {
        topic.addLink(link);
        /**
         * All links must be also linked to the topic.
         */
        topic.addLink(new Link(topic.getTopicPage(), link.getTail(), 0.4 * weight));
    }
    
    private void decreaseWeight(Page linkedPage) {
        for (Link existing : topic.getLinksById(linkedPage.getId()).values()) {
            Double newWeight = existing.getWeight() - Config.LINK_FOO_WEIGHT_DECREASE;
            if (newWeight < 0.1) newWeight = 0.1;
            existing.setWeight(newWeight);
        }
    }
    
    private void filter() {
        
        /**
         * This is the final filter that should remove all extracted concepts
         * that are known to be useless.
         * Some of these concepts were preserved, in order to extract
         * their sub-categories or links, and thus increase the vocabulary.
         */
        Iterator<Entry<String, HashMap<String, Link>>> headIterator = topic.getLinks().entrySet().iterator();
        while (headIterator.hasNext()) {
            
            Entry<String, HashMap<String, Link>> headEntry =  headIterator.next();
            
            boolean validated = false;
            Iterator<Entry<String, Link>> tailIterator = headEntry.getValue().entrySet().iterator();
            while (tailIterator.hasNext()) {
            
                Entry<String, Link> tailEntry = tailIterator.next();

                if (!validated) {
                    String title = tailEntry.getValue().getHead().getTitle().toLowerCase();
                    if (!Blacklist.isAceptableConceptLabel(title)) {
                        headIterator.remove();
                        break;
                    }
                    validated = true;
                }
                
                String title = tailEntry.getValue().getTail().getTitle().toLowerCase();
                if (!Blacklist.isAceptableConceptLabel(title)) {
                    tailIterator.remove();
                }
            
            }
            
            if (headEntry.getValue().isEmpty()) headIterator.remove();
        
        }
        
    }
 
    public final void printRejects() {
        System.out.println();
        System.out.println("LinkExtractor rejects (" + rejects.size() + "):");
        rejects.entrySet().forEach((entry) -> { System.out.println(entry.getValue() + "\t" + entry.getKey()); });
        System.out.println();
    }
    
}
