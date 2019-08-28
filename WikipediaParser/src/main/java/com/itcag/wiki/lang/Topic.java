package com.itcag.wiki.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
public class Topic {

    private final String id;
    private final String name;
    private final String wikiName;

    /**
     * This topic represented as an instance of the Page class.
     */
    private final Page topicPage;

    /**
     * Collection of the Wikipedia navboxes used on the pages
     * belonging to this topic, and the list of links containes in those navboxes.
     * These links are used to filter the collection of links
     * contained on a page, so that the page is not erroneously
     * associated with the links in the navboxes.
     */
    private final ArrayList<Page> navboxes =  new ArrayList<>();
    private final HashMap<String, Page> navboxlinks = new HashMap<>();

    /**
     * Collection of the first generation sub-categories
     * that must be recursively processed without termination
     * after the third generation (in contrast to the regular
     * sub-categories).
     */
    private final HashMap<String, Page> usefulLists = new HashMap<>();
    
    private final HashMap<String, Page> pages = new HashMap<>();
    private final HashMap<String, HashMap<String, Link>> links = new HashMap<>();
    
    public Topic(String id, String wikiName) {
        this.id = id;
        this.name = wikiName.replace("_", " ");
        this.wikiName = wikiName;
        this.topicPage = new Page(id, name, Page.Type.CATEGORY);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWikiName() {
        return wikiName;
    }

    public Page getTopicPage() {
        return topicPage;
    }
    
    public ArrayList<Page> getNavboxes() {
        return navboxes;
    }
    
    public void addNavbox(String id, String title) {
        navboxes.add(new Page(id, title, Page.Type.UTIL));
    }
    
    public HashMap<String, Page> getNavboxLinks() {
        return navboxlinks;
    }
    
    public boolean isNavboxLink(String link) {
        return navboxlinks.containsKey(link);
    }
    
    public void addNavboxLink(Page link) {
        navboxlinks.put(link.getId(), link);
    }

    public HashMap<String, Page> getUsefulLists() {
        return usefulLists;
    }
    
    public void addUsefulList(Page list) {
        usefulLists.put(list.getId(), list);
    }
    
    public HashMap<String, Page> getPages() {
        return pages;
    }

    public void addPage(Page page) {
        if (!pages.containsKey(page.getId())) pages.put(page.getId(), page);
    }
    
    public HashMap<String, HashMap<String, Link>> getLinks() {
        return links;
    }
    
    public HashMap<String, Link> getLinksById(String id) {
        return links.get(id);
    }

    public void addLink(Link link) {
        addHead(link);
        addTail(link);
    }
    
    private void addHead(Link link) {
        if (links.containsKey(link.getHead().getId())) {
            HashMap<String, Link> tmp = links.get(link.getHead().getId());
            if (!tmp.containsKey(link.getTail().getId())) {
                tmp.put(link.getTail().getId(), link);
            } else if (tmp.get(link.getTail().getId()).getWeight() < link.getWeight()) {
                tmp.put(link.getTail().getId(), link);
            }
        } else {
            HashMap<String, Link> tmp = new HashMap<>();
            tmp.put(link.getTail().getId(), link);
            links.put(link.getHead().getId(), tmp);
        }
    }
    
    private void addTail(Link link) {
        if (links.containsKey(link.getTail().getId())) {
            HashMap<String, Link> tmp = links.get(link.getTail().getId());
            if (!tmp.containsKey(link.getHead().getId())) {
                tmp.put(link.getHead().getId(), link);
            } else if (tmp.get(link.getHead().getId()).getWeight() < link.getWeight()) {
                tmp.put(link.getHead().getId(), link);
            }
        } else {
            HashMap<String, Link> tmp = new HashMap<>();
            tmp.put(link.getHead().getId(), link);
            links.put(link.getTail().getId(), tmp);
        }
    }
    
    public final void print() throws Exception {
        
        HashMap<String, HashSet<String>> index = new HashMap<>();
        
        links.entrySet().forEach((outer) -> {
            outer.getValue().entrySet().forEach((inner) -> {
                String head = inner.getValue().getHead().getTitle();
                String tail = inner.getValue().getTail().getId() + " " + inner.getValue().getTail().getTitle();
                if (index.containsKey(head)) {
                    index.get(head).add(tail);
                } else {
                    index.put(head, new HashSet<>(Arrays.asList(tail)));
                }
            });
        });
        
        index.entrySet().stream().forEach((entry) -> {
            System.out.println(entry.getKey());
            entry.getValue().stream().forEach((tail) -> {
                System.out.println("\t" + tail);
            });
            System.out.println();
        });
        
    }
    
}
