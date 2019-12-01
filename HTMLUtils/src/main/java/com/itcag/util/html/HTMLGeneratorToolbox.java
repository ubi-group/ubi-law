package com.itcag.util.html;

import com.itcag.util.txt.TextToolbox;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.TreeSet;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class HTMLGeneratorToolbox {

    public final static Element getHead(String title, String version, Document doc, ArrayList<String> scripts) {
        
        Element retVal = doc.createElement("head");

        {
            Element subElt = doc.createElement("title");
            subElt.setTextContent(title);
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("META");
            subElt.setAttribute("name", "Version");
            subElt.setAttribute("content", version);
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("META");
            subElt.setAttribute("charset", "UTF-8");
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("META");
            subElt.setAttribute("name", "viewport");
            subElt.setAttribute("content", "width=device-width, initial-scale=1.0");
            retVal.appendChild(subElt);
        }

        /**
         * Ensure that no caching takes place ANYWHERE.
         */
        {
            Element subElt = doc.createElement("META");
            subElt.setAttribute("content", "no-cache");
            subElt.setAttribute("http-equiv", "Pragma");
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("META");
            subElt.setAttribute("content", "-1");
            subElt.setAttribute("http-equiv", "Expires");
            retVal.appendChild(subElt);
        }
        
        if(scripts != null) {
            for(String scriptPath: scripts) {
                {
                    Element subElt = doc.createElement("script");
                    subElt.setAttribute("type", "text/javascript");
                    subElt.setAttribute("src", scriptPath);
                    retVal.appendChild(subElt);
                }
            }
        }
       
        {
            Element subElt = doc.createElement("link");
            subElt.setAttribute("rel", "stylesheet");
            subElt.setAttribute("href", "//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css");
            retVal.appendChild(subElt);
        }
/*        
        {
            Element subElt = doc.createElement("link");
            subElt.setAttribute("rel", "stylesheet");
            subElt.setAttribute("href", "//maxcdn.bootstrapcdn.com/bootstrap/4.1.1/css/bootstrap.min.css");
            subElt.setAttribute("id", "bootstrap-css");
            retVal.appendChild(subElt);
        }
*/
        return retVal;
        
    }

    public final static Element getHebrewFont(Document doc) {
        Element retVal = doc.createElement("link");
        retVal.setAttribute("href", "https://fonts.googleapis.com/css?family=Varela+Round&display=swap");
        retVal.setAttribute("rel", "stylesheet");
        return retVal;
    }
    
    public final static Element getBody(Document doc) {
        Element retVal = doc.createElement("body");
        retVal.setAttribute("style", "padding:0px 20px 20px 20px;");
        return retVal;
    }
    
    public final static Element getForm(String action, boolean narrow, Document doc) {
        
        Element retVal = doc.createElement("form");
        retVal.setAttribute("method", "POST");
        retVal.setAttribute("action", action);
        if (narrow) retVal.setAttribute("style", "width: 700px;");
        return retVal;
        
    }
    
    public final static Element getMultiPartForm(String action, boolean narrow, Document doc) {
        
        Element retVal = doc.createElement("form");
        retVal.setAttribute("enctype", "multipart/form-data");
        retVal.setAttribute("method", "POST");
        retVal.setAttribute("action", action);
        if (narrow) {
            retVal.setAttribute("style", "clear:both; width: 700px;");
        } else {
            retVal.setAttribute("style", "clear:both;");
        }

        return retVal;
        
    }
    
    public final static Element getFormReplacement(Document doc) {
        
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "width: 700px; margin:20px 50px 0px 50px");

        return retVal;
        
    }

    public final static Element getTitle(String title, Document doc) {
        
        Element retVal = doc.createElement("h1");
        retVal.setAttribute("style", "clear:both; float:left;");
        retVal.setTextContent(title);
        
        return retVal;
        
    }
    
    public final static Element getSubtitle(String title, Document doc) {
        
        Element retVal = doc.createElement("h2");
        retVal.setAttribute("style", "clear:both; float:left;");
        retVal.setTextContent(title);
        
        return retVal;
        
    }
    
    public final static Element getH3(String title, Document doc) {
        
        Element retVal = doc.createElement("h3");
        retVal.setAttribute("style", "clear:both; float:left;");
        retVal.setTextContent(title);
        
        return retVal;
        
    }
    
    public final static Element getH4(String title, Document doc) {
        
        Element retVal = doc.createElement("h4");
        retVal.setAttribute("style", "clear:both; float:left;");
        retVal.setTextContent(title);
        
        return retVal;
        
    }
    
    public final static Element getCheckbox(String label, boolean checked, String field, boolean readonly, Document doc) {

        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        {
            Element elt = doc.createElement("input");
            elt.setAttribute("type", "checkbox");
            if (!TextToolbox.isEmpty(field)) elt.setAttribute("name", field);
            if (!TextToolbox.isEmpty(field)) elt.setAttribute("id", field);
            if (readonly) elt.setAttribute("disabled", "disabled");
            if (checked) elt.setAttribute("checked", "checked");
            retVal.appendChild(elt);
        }

        {
            Element elt = doc.createElement("label");
            if (!TextToolbox.isEmpty(field)) elt.setAttribute("for", field);
            elt.setTextContent(label);
            retVal.appendChild(elt);
        }
        return retVal;

    }

    public final static Element getRadio(String label, String name, String value, boolean checked, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; clear:both; float:left");
        
        {
            Element subElt = doc.createElement("input");
            subElt.setAttribute("type", "radio");
            subElt.setAttribute("name", name);
            subElt.setAttribute("id", value);
            subElt.setAttribute("value", value);
            if (checked) subElt.setAttribute("checked", Boolean.toString(checked));
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("label");
            subElt.setAttribute("for", value);
            subElt.setTextContent(label);
            subElt.setAttribute("style", "margin-left:10px;");
            retVal.appendChild(subElt);
        }

        return retVal;
        
    }
    
    private static Element getLabel(String label, Document doc) {
        Element retVal = doc.createElement("span");
        retVal.setTextContent(label);
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left;");
        return retVal;
    }
    
    public final static Element getInput(String input, String field, boolean readonly, boolean autofocus, Document doc) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("name", field);
        retVal.setAttribute("type", "text");
        if (readonly) retVal.setAttribute("readonly", "readonly");
        if (!readonly && autofocus) retVal.setAttribute("autofocus", "autofocus");
        if (!TextToolbox.isReallyEmpty(input)) retVal.setAttribute("value", input);
        retVal.setAttribute("style", "width:100%");
        
        return retVal;

    }
 
    public final static Element getInput(String id, String name, String placeholder, Document doc) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("name", name);
        retVal.setAttribute("id", id);
        retVal.setAttribute("type", "text");
        retVal.setAttribute("class", "form-control");
        retVal.setAttribute("placeholder", placeholder);
        retVal.setAttribute("style", "width:100%");
        
        return retVal;

    }    
    
    public final static Element getLabeledInput(String label, String input, String field, boolean readonly, boolean autofocus, Document doc) {

        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("input");
            subElt.setAttribute("name", field);
            subElt.setAttribute("type", "text");
            if (readonly) subElt.setAttribute("readonly", "readonly");
            if (!readonly && autofocus) subElt.setAttribute("autofocus", "autofocus");
            if (!TextToolbox.isReallyEmpty(input)) subElt.setAttribute("value", input);
            subElt.setAttribute("style", "width:100%");
            retVal.appendChild(subElt);
        }
        
        return retVal;

    }
    
    public final static Element getLabeledInputWithInstruction(String label, String instruction, String input, String field, boolean readonly, boolean autofocus, Document doc) {

        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("span");
            subElt.setAttribute("style", "display:block; float:left; width:100%; color:#606060; font-style: italic; font-size: 80%");
            subElt.setTextContent(instruction);
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("input");
            subElt.setAttribute("name", field);
            subElt.setAttribute("type", "text");
            if (readonly) subElt.setAttribute("readonly", "readonly");
            if (!readonly && autofocus) subElt.setAttribute("autofocus", "autofocus");
            if (!TextToolbox.isEmpty(input)) subElt.setAttribute("value", input);
            subElt.setAttribute("style", "width:100%");
            retVal.appendChild(subElt);
        }
        
        return retVal;

    }
    
    public final static Element getHiddenInput(String input, String field, Document doc) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px; display:none;");
        retVal.setAttribute("name", field);
        retVal.setAttribute("type", "text");
        if (!TextToolbox.isEmpty(input)) retVal.setAttribute("value", input);

        return retVal;

    }
    
    public final static Element getTextArea(String input, String name, boolean readonly, boolean autofocus, int width, int topMargin, int bottomMargin, int leftMargin, int rightMargin, Document doc) {
        
        Element retVal = doc.createElement("textarea");
        retVal.setAttribute("name", name);
        retVal.setAttribute("rows", "10");
        retVal.setAttribute("cols", "100");
        if (autofocus) retVal.setAttribute("autofocus", "autofocus");
        if (readonly) retVal.setAttribute("readonly", "readonly");
        retVal.setAttribute("style", "display:block; float:left; width:" + width + "%; margin-top:" + topMargin + "px; margin-bottom:" + bottomMargin + " px; margin-left:" + leftMargin + "px; margin-right:" + rightMargin + "px;");
        retVal.setTextContent(input);
        return retVal;
    }
    
    public final static Element getTextArea(String input, String name, boolean readonly, boolean autofocus, Document doc) {
        
        Element retVal = doc.createElement("textarea");
        retVal.setAttribute("name", name);
        retVal.setAttribute("rows", "10");
        retVal.setAttribute("cols", "100");
        if (autofocus) retVal.setAttribute("autofocus", "autofocus");
        if (readonly) retVal.setAttribute("readonly", "readonly");
        retVal.setAttribute("style", "display:block; float:left; width:100%; margin-top:20px; margin-bottom:20px;");
        retVal.setTextContent(input);
        return retVal;
    }
    
    public final static Element getTextAreaForOutputDisplay(String input, String name, boolean readonly, boolean autofocus, Document doc) {
        
        Element retVal = doc.createElement("textarea");
        retVal.setAttribute("name", name);
        retVal.setAttribute("rows", "50");
        retVal.setAttribute("cols", "100");
        if (autofocus) retVal.setAttribute("autofocus", "autofocus");
        if (readonly) retVal.setAttribute("readonly", "readonly");
        retVal.setAttribute("style", "display:block; float:left; width:100%; margin-top:20px; margin-bottom:20px;");
        retVal.setTextContent(input);
        return retVal;
    }
    
    public final static Element getLabeledTextArea(String label, String input, String name, boolean readonly, boolean autofocus, int width, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("textarea");
            subElt.setAttribute("name", name);
            subElt.setAttribute("rows", "30");
            subElt.setAttribute("cols", "100");
            if (readonly) subElt.setAttribute("readonly", "readonly");
            if (!readonly && autofocus) subElt.setAttribute("autofocus", "autofocus");
            subElt.setAttribute("style", "display:block; width:" + width + "%; clear:both; float:left;");
            subElt.setTextContent(input);
            retVal.appendChild(subElt);
        }

        return retVal;
    }    
    
    public final static Element getLabeledTextArea(String label, String input, String name, boolean readonly, boolean autofocus, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("textarea");
            subElt.setAttribute("name", name);
            subElt.setAttribute("rows", "10");
            subElt.setAttribute("cols", "100");
            if (readonly) subElt.setAttribute("readonly", "readonly");
            if (!readonly && autofocus) subElt.setAttribute("autofocus", "autofocus");
            subElt.setAttribute("style", "display:block; width:100%; clear:both; float:left;");
            subElt.setTextContent(input);
            retVal.appendChild(subElt);
        }

        return retVal;
    }
    
    public final static Element getLabeledTextAreaWithInstruction(String label, String instruction, String input, String name, boolean readonly, boolean autofocus, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("span");
            subElt.setAttribute("style", "display:block; float:left; width:100%; color:#606060; font-style: italic; font-size: 80%");
            subElt.setTextContent(instruction);
            retVal.appendChild(subElt);
        }

        {
            Element subElt = doc.createElement("textarea");
            subElt.setAttribute("name", name);
            subElt.setAttribute("rows", "10");
            subElt.setAttribute("cols", "100");
            if (readonly) subElt.setAttribute("readonly", "readonly");
            if (!readonly && autofocus) subElt.setAttribute("autofocus", "autofocus");
            subElt.setAttribute("style", "display:block; width:100%; clear:both; float:left;");
            if (input != null) subElt.setTextContent(input);
            retVal.appendChild(subElt);
        }

        return retVal;
    }
    
    public final static Element getBlockLink(String href, String label, String align, Document doc) {

        Element retVal = doc.createElement("a");
        retVal.setAttribute("style", "display:block; clear:" + align + "; float:" + align + "; margin-bottom:30px;");
        retVal.setAttribute("href", href);
        retVal.setTextContent(label);

        return retVal;

    }
    
    public final static Element getInlineLink(String href, String label, int leftMargin, int rightMargin, Document doc) {

        Element retVal = doc.createElement("a");
        retVal.setAttribute("style", "margin-right: " + rightMargin + "px; margin-left:" + leftMargin + "px;");
        retVal.setTextContent(label);
        retVal.setAttribute("href", href);

        return retVal;

    }    

    public final static Element getInlineLink(String href, String label, Document doc) {

        Element retVal = doc.createElement("a");
        retVal.setAttribute("style", "margin-right: 20px;");
        retVal.setTextContent(label);
        retVal.setAttribute("href", href);

        return retVal;

    }

    public final static Element getTinyInlineLink(String href, String label, Document doc) {

        Element retVal = doc.createElement("a");
        retVal.setAttribute("style", "margin-left: 20px; font-size:75%;");
        retVal.setTextContent(label);
        retVal.setAttribute("href", href);

        return retVal;

    }
    

    public final static Element getDiv(Document doc) {
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "clear:both; display:block; float:left; width:100%; margin-bottom:30px;");
        return retVal;
    }

    public final static Element getSideBySideDiv(Document doc) {
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; float:left; max-width:250px; margin-right:40px;");
        return retVal;
    }

    public final static Element getSideBySideDiv(int maxWidth, Document doc) {
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; float:left; width:100%; max-width:" + maxWidth + "px; margin-right:20px;");
        return retVal;
    }

    public final static Element getMenuDiv(Document doc) {
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; float:left; max-width:250px; margin-left:20px; margin-right:40px;");
        return retVal;
    }

    /* LISTS */
    public final static Element getUl(Document doc) {
        Element retVal = doc.createElement("ul");
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin:0;");
        return retVal;
    }

    public final static Element getUlNoDiscs(Document doc) {
        Element retVal = doc.createElement("ul");
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin:0; list-style-type: none;");
        return retVal;
    }

    public final static Element getMenuUl(Document doc) {
        Element retVal = doc.createElement("ul");
        retVal.setAttribute("style", "display:block; clear:both; float:left; padding-left:10px; margin:0 0 0 10px;");
        return retVal;
    }

    public final static Element getListItem(String label, Document doc) {

        Element retVal = doc.createElement("li");
        retVal.setAttribute("style", "clear:both; margin-bottom:5px;");
        if (label != null) retVal.setTextContent(label);

        return retVal;

    }

    public final static Element getListLink(String href, String label, Document doc) {

        Element retVal = doc.createElement("li");
        retVal.setAttribute("style", "clear:both; margin-bottom:5px;");
        retVal.appendChild(getLink(href, label, doc));

        return retVal;

    }

    public final static Element getMenuItem(String href, String label, Document doc) {

        Element retVal = doc.createElement("li");
        retVal.setAttribute("style", "margin-bottom:5px;");
        retVal.appendChild(getLink(href, label, doc));

        return retVal;

    }

    public final static Element getLink(String href, String label, Document doc) {

        Element retVal = doc.createElement("a");
        retVal.setTextContent(label);
        retVal.setAttribute("href", href);

        return retVal;

    }

    public final static Element getBlockSpan(String text, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left;");
        
        return retVal;
    }
    
    public final static Element getInlineSpan(String text, int leftMargin, int rightMargin, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        retVal.setAttribute("style", "margin-left:" + leftMargin + "px; margin-right:" + rightMargin + "px;");
        
        return retVal;
    }    

    public final static Element getInlineSpan(String text, boolean leftMargin, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        if (leftMargin) retVal.setAttribute("style", "margin-left:10px;");
        
        return retVal;
    }
    
    public final static Element getTinyInlineSpan(String text, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        retVal.setAttribute("style", "margin-left:10px; font-size:75%;");
        
        return retVal;
    }
    
    public final static Element getIcon(String text, String popup, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        retVal.setAttribute("title", popup);
        retVal.setAttribute("style", "margin-left:10px; cursor:default");
        
        return retVal;
    }
    
    public final static Element getRedBullIcon(String text, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setTextContent(text);
        retVal.setAttribute("title", "RedBull Gives You Wings!");
        retVal.setAttribute("style", "margin-left:10px; cursor:default; color:red;");
        
        return retVal;
    }
    
    public final static Element getParagraph(String text, Document doc) {
        
        Element retVal = doc.createElement("p");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left;");
        retVal.setTextContent(text);
        
        return retVal;
    }
    
    public final static Element getDropdown(TreeSet<String> options, String selected, String name, boolean disabled, boolean allowNoSelection, Document doc) {
        
        Element retVal = doc.createElement("select");
        if (disabled) retVal.setAttribute("disabled", "disabled");
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");
        retVal.setAttribute("name", name);
        
        if (allowNoSelection) {
            Element subElt = doc.createElement("option");
            subElt.setAttribute("value", "");
            subElt.setTextContent("");
            if (selected == null) subElt.setAttribute("selected", "selected");
            retVal.appendChild(subElt);
        }
        
        for (String option : options) {
            Element subElt = doc.createElement("option");
            subElt.setAttribute("value", option);
            subElt.setTextContent(option);
            if (option.equals(selected)) subElt.setAttribute("selected", "selected");
            retVal.appendChild(subElt);
        }

        return retVal;

    }
    
    public final static Element getLabeledDropdown(String label, TreeSet<String> options, String selected, String name, boolean disabled, boolean allowNoSelection, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");
        
        retVal.appendChild(getLabel(label, doc));

        retVal.appendChild(getDropdown(options, selected, name, disabled, allowNoSelection, doc));

        return retVal;

    }
    
    public final static Element getDropdown(TreeMap<String, String> options, String selected, String name, boolean disabled, boolean allowNoSelection, Document doc) {
        
        Element retVal = doc.createElement("select");
        if (disabled) retVal.setAttribute("disabled", "disabled");
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");
        retVal.setAttribute("name", name);
        
        if (allowNoSelection) {
            Element subElt = doc.createElement("option");
            subElt.setAttribute("value", "");
            subElt.setTextContent("");
            if (TextToolbox.isReallyEmpty(selected)) subElt.setAttribute("selected", "selected");
            retVal.appendChild(subElt);
        }
        
        for (Entry<String, String> entry : options.entrySet()) {
            Element subElt = doc.createElement("option");
            subElt.setAttribute("value", entry.getValue());
            subElt.setTextContent(entry.getKey());
            if (entry.getValue().equals(selected)) subElt.setAttribute("selected", "selected");
            retVal.appendChild(subElt);
        }

        return retVal;

    }
    
    public final static Element getLabeledDropdown(String label, TreeMap<String, String> options, String selected, String name, boolean disabled, boolean allowNoSelection, Document doc) {
        
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");
        
        retVal.appendChild(getLabel(label, doc));

        retVal.appendChild(getDropdown(options, selected, name, disabled, allowNoSelection, doc));

        return retVal;

    }
    
    public final static Element getDatePicker(String date, String name, boolean disabled, Document doc) {
        Element retVal = doc.createElement("input");
        retVal.setAttribute("type", "date");
        retVal.setAttribute("name", name);
        if (disabled) retVal.setAttribute("disabled", "disabled");
        if (!TextToolbox.isReallyEmpty(date)) retVal.setAttribute("value", date);
        retVal.setAttribute("style", "display:block; clear:both; float:left;");
        return retVal;
    }
    
    public final static Element getLabeledDatePicker(String label, String date, String name, boolean disabled, Document doc) {
        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");
        
        retVal.appendChild(getLabel(label, doc));

        retVal.appendChild(getDatePicker(date, name, disabled, doc));

        return retVal;
    }
    
    public final static Element getLabeledFileInput(String label, String field, Document doc) {

        Element retVal = doc.createElement("span");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px");

        retVal.appendChild(getLabel(label, doc));
        
        {
            Element subElt = doc.createElement("input");
            subElt.setAttribute("name", field);
            subElt.setAttribute("type", "file");
            retVal.appendChild(subElt);
        }
        
        return retVal;

    }

    public final static Element getDetails(String summary, String paragraph, Document doc) {
        
        Element retVal = doc.createElement("details");

        Element subElt = doc.createElement("summary");
        subElt.setTextContent(summary);
        retVal.appendChild(subElt);
        
        if (paragraph != null) retVal.appendChild(getParagraph(paragraph, doc));
        
        return retVal;

    }
    
    public final static Element getSubmitButton(Document doc) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("type", "submit");
        retVal.setAttribute("value", "Submit");
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");

        return retVal;

    }
    
    public final static Element getSearchButton(Document doc, String displayName) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("type", "submit");
        retVal.setAttribute("value", displayName);
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");

        return retVal;

    }
    
    public final static Element getSearchButton(Document doc, String name, String displayName, String value) {

        Element retVal = doc.createElement("input");
        retVal.setAttribute("type", "submit");
        retVal.setAttribute("name", name);
        retVal.setAttribute("value", value);
        retVal.setTextContent(displayName);
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");

        return retVal;

    }
    
    public final static Element getButton(Document doc, String name, String displayName, String value) {

        Element retVal = doc.createElement("button");
        retVal.setAttribute("type", "submit");
        retVal.setAttribute("name", name);
        retVal.setAttribute("value", value);
        retVal.setTextContent(displayName);
        retVal.setAttribute("style", "display:block; clear:both; float:left; margin-bottom:20px;");

        return retVal;

    }    
        
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      EMBEDDED LIST
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/
    public final static Element getEmbeddingDiv(Document doc) {
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "clear:both;");
        return retVal;
    }

    public final static Element getEmbeddedUl(Document doc) {
        Element retVal = doc.createElement("ul");
        retVal.setAttribute("style", "clear:both; list-style-type: none; margin-bottom:10px;");
        return retVal;
    }

    public final static Element getEmbeddedIl(String label, Document doc) {
        Element retVal = doc.createElement("li");
        retVal.setAttribute("style", "clear:both;");
        if (label != null) retVal.setTextContent(label);
        return retVal;
    }



    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      TABLE
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public final static Element getTable(Document doc) {
        Element retVal = doc.createElement("table");
        retVal.setAttribute("style", "display:block; clear:both; float:left; width:100%; max-width:100%; border-collapse:collapse; table-layout:fixed;");
        return retVal;
    }
    
    public final static Element getTableHeaderRow(ArrayList<String> labels, Document doc) {

        Element retVal = doc.createElement("tr");

        for (int i = 0; i < labels.size(); i++) {
            Element cell = doc.createElement("th");
            if (i == 0) {
                cell.setAttribute("style", "width:100px; padding-right:10px; word-wrap:break-word;");
            } else if (i == labels.size() - 1) {
                cell.setAttribute("style", "width:100px; padding-left:10px; word-wrap:break-word;");
            } else {
                cell.setAttribute("style", "width:100px; padding-left:10px; padding-right:10px; word-wrap:break-word;");
            }
            cell.setTextContent(labels.get(i));
            retVal.appendChild(cell);
        }
        
        return retVal;
        
    }

    public final static Element getTableRow(ArrayList<Entry<String, String>> content, Document doc) {

        Element retVal = doc.createElement("tr");

        for (Entry<String, String> entry : content) {
            Element cell = doc.createElement("td");
            cell.setAttribute("style", "padding-bottom:4px; text-align: " + entry.getValue() + "; white-space:pre-line; word-wrap:break-word;");
            cell.setAttribute("valign", "top");
            cell.setTextContent(entry.getKey());
            retVal.appendChild(cell);
        }
        
        return retVal;
        
    }


    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      ACCESSORIES
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public final static Element getVerticalSpaceDiv(int height, Document doc) {
        
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; clear:both; float:left; width:100%; height:" + height + "px;");
        return retVal;

    }
    
    public final static Element getResetDiv(Document doc) {

        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; clear:both; float:left;");
        return retVal;
        
    }

    
    
    /*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
      COMPONENTS
     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

    public final static Element getWarning(String text, Document doc) {
        
        Element retVal = doc.createElement("p");
        retVal.setAttribute("style", "display:block; width:100%; clear:both; float:left; margin-bottom:20px; font-size:120%; color:red;");
        retVal.setTextContent(text);
        
        return retVal;

    }
    
    /*
     * Used only to create a menu that spans across the top of every page.
     */
    public final static Element getTopMenu(LinkedHashMap<String, String> links, Document doc) {
        
        Element retVal = doc.createElement("div");
        retVal.setAttribute("style", "display:block; clear:both; float:left; text-align:right; width:100%; padding:10px; background-color:#E6E6FA;");
        
        for (Entry<String, String> entry : links.entrySet()) {
            retVal.appendChild(getInlineLink(entry.getValue(), entry.getKey(), doc));
        }
        
        return retVal;

    }
    
    public final static Element getBreadcrumbs(Element wrapper, String label, String url, Document doc) {

        /*
         * Initialy there is no wrapper, and it must be first created.
         */
        if (wrapper == null) {
            wrapper = doc.createElement("span");
            wrapper.setAttribute("style", "display:block; clear:both; float:left;");
        } else {
            Element pointer = doc.createElement("span");
            pointer.setAttribute("style", "font-size:75%");
            pointer.setTextContent("  >  ");
            wrapper.appendChild(pointer);
        }
        
        Element breadcrumb = doc.createElement("a");
        breadcrumb.setAttribute("style", "font-size:75%");
        breadcrumb.setAttribute("href", url);
        breadcrumb.setTextContent(label);
        wrapper.appendChild(breadcrumb);
        
        return wrapper;

    }
    
}
