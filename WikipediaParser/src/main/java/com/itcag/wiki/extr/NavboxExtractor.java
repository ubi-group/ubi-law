package com.itcag.wiki.extr;

import com.itcag.util.Encoder;
import com.itcag.wiki.lang.Page;
import com.itcag.wiki.lang.Topic;
import com.itcag.wiki.pedia.WikiPageLinks;

import java.util.HashMap;

/**
 * Extracts all links from the Wikipedia navboxes.
 */
public final class NavboxExtractor {
    
    private final Topic topic;
    
    public NavboxExtractor(Topic topic) {
        this.topic = topic;
    }
    
    public final void insertLinks() throws Exception {

        for (Page navbox : topic.getNavboxes()) {
            HashMap<String, Page> links = getLinks(navbox.getId());
            for (Page linkedPage : links.values()) {
                topic.addNavboxLink(linkedPage);
            }
        }
        
    }

    private HashMap<String, Page> getLinks(String pageId) throws Exception {
        
        WikiPageLinks collector = new WikiPageLinks();
        collector.download(Encoder.encodeText(pageId));
        
        return collector.getLinks();
    
    }

}
