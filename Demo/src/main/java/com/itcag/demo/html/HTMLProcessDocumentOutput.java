package com.itcag.demo.html;

public class HTMLProcessDocumentOutput {
/*
    public final static String get(ArrayList<StatMacro> searchResults) throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        Element root = doc.getDocumentElement();
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.MAINTAIN_INPUT.getTitle(), WebConstants.VERSION, doc));

        root.appendChild(getBody(searchResults, doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(ArrayList<StatMacro> searchResults, Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element breadcrumbs = HTMLGeneratorToolbox.getBreadcrumbs(null, "Home", WebConstants.CONTEXT_PATH, doc);
        elt.appendChild(HTMLGeneratorToolbox.getBreadcrumbs(breadcrumbs, Targets.SEARCH_INPUT.getTitle(), Targets.SEARCH_INPUT.getUrl(), doc));

        elt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.SEARCH_OUTPUT.getTitle(), doc));

        elt.appendChild(getList(searchResults, doc));
        
        return elt;
        
    }

    private static Element getList(ArrayList<StatMacro> searchResults, Document doc) {

        Element retVal = HTMLGeneratorToolbox.getUlNoDiscs(doc);
        
        for (StatMacro statMacro : searchResults) {
            retVal.appendChild(getListItem(statMacro, doc));
        }
        
        return retVal;
        
    }
    
    private static Element getListItem(StatMacro statMacro, Document doc) {
        
        Element retVal = HTMLGeneratorToolbox.getListItem(null, doc);

        StringBuilder url = new StringBuilder();
        url.append(Targets.SEARCH_EDIT.getUrl());
        url.append("?").append(FormFields._ID.getFieldName()).append("=").append(statMacro.get_id());

        StringBuilder label = new StringBuilder();
        label.append(statMacro.getTaxonomyValueName());
        label.append(" (").append(statMacro.getCategory()).append(")");
        
        retVal.appendChild(HTMLGeneratorToolbox.getBlockLink(url.toString(), label.toString(), "left", doc));

        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(statMacro.getSscript(), doc));
        retVal.appendChild(HTMLGeneratorToolbox.getBlockSpan(statMacro.getSentence(), doc));
        
        return retVal;
        
    }    
*/    
}
