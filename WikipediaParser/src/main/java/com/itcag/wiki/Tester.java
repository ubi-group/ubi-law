package com.itcag.wiki;

import com.itcag.wiki.extr.LinkExtractor;
import com.itcag.wiki.extr.NavboxExtractor;
import com.itcag.wiki.extr.NavboxLinkExtractor;
import com.itcag.wiki.extr.SubcategoryExtractor;
import com.itcag.wiki.extr.PrimaryPageExtractor;
import com.itcag.wiki.extr.UsefulListExtractor;
import com.itcag.wiki.extr.UsefulListPageExtractor;
import com.itcag.wiki.lang.Link;
import com.itcag.wiki.lang.Topic;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Tester {

    public static void main(String[] args) throws Exception {

//        WikiCategories.download();

/*
To retrieve templates for a category use this:
https://en.wikipedia.org/w/api.php?action=query&titles=[ insert title here ]&generator=templates&gtlnamespace=10&format=json&gtllimit=max
*/
        
//        Topic topic = new Topic("906645", "Renewable_energy");

        Topic topic = new Topic("1952489", "Cosmetics");
        topic.addNavbox("11105147", "Template:Cosmetics");
        
        populateTopic(topic);
        printForHumans(topic);
        printToSave(topic);
//        topic.print();

    }
    
    private static void populateTopic(Topic topic) throws Exception {

        {
            NavboxExtractor extractor = new NavboxExtractor(topic);
            extractor.insertLinks();
        }
        
        {
            SubcategoryExtractor extractor = new SubcategoryExtractor(topic);
            extractor.insertSubcategories();
        }
//        topic.print();
        
        {
            PrimaryPageExtractor extractor = new PrimaryPageExtractor(topic);
            extractor.insertPages();
        }
//        topic.print();

        {
            {
                UsefulListExtractor extractor = new UsefulListExtractor(topic);
                extractor.insertUsefulLists();
            }
            {
                UsefulListPageExtractor extractor = new UsefulListPageExtractor(topic);
                extractor.insertPages();
            }
        }
//        topic.print();

        {
            LinkExtractor extractor = new LinkExtractor(topic);
            extractor.insertLinks();
        }
//        topic.print();

        {
            NavboxLinkExtractor extractor = new NavboxLinkExtractor(topic);
            extractor.insertLinks();
        }
//        topic.print();

        
    }
    
    private static void printForHumans(Topic topic) {

        TreeMap<String, TreeMap<Double, TreeMap<String, Link>>> sorted = new TreeMap<>();
        for (Entry<String, HashMap<String, Link>> entry : topic.getLinks().entrySet()) {
            for (Link link : entry.getValue().values()) {
                if (sorted.containsKey(link.getHead().getTitle())) {
                    TreeMap<Double, TreeMap<String, Link>> weight = sorted.get(link.getHead().getTitle());
                    if (weight.containsKey(link.getWeight())) {
                        TreeMap<String, Link> tail = weight.get(link.getWeight());
                        tail.put(link.getTail().getTitle(), link);
                    } else {
                        TreeMap<String, Link> tail = new TreeMap<>();
                        tail.put(link.getTail().getTitle(), link);
                        weight.put(link.getWeight(), tail);
                    }
                } else {
                    TreeMap<String, Link> tail = new TreeMap<>();
                    tail.put(link.getTail().getTitle(), link);
                    TreeMap<Double, TreeMap<String, Link>> weight = new TreeMap<>(Collections.reverseOrder());
                    weight.put(link.getWeight(), tail);
                    sorted.put(link.getHead().getTitle(), weight);
                }
            } 
        }

        for (Entry<String, TreeMap<Double, TreeMap<String, Link>>> headEntry : sorted.entrySet()) {
            System.out.println(headEntry.getKey());
            for (Entry<Double, TreeMap<String, Link>> weightEntry : headEntry.getValue().entrySet()) {
                for (Entry<String, Link> tailEntry : weightEntry.getValue().entrySet()) {
                    System.out.println("\t" + weightEntry.getKey() + "\t" + tailEntry.getKey());
                }
            }
        }
        
    }

    private static void printToSave(Topic topic) {
        
        for (Entry<String, HashMap<String, Link>> entry : topic.getLinks().entrySet()) {
            for (Link link : entry.getValue().values()) {
                System.out.println(link.toString());
            }
        }
        
    }
    
}

