package com.itcag.demo.html;

import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.demo.WebConstants;
import com.itcag.util.Encoder;
import com.itcag.util.XMLProcessor;
import com.itcag.util.html.HTMLGeneratorToolbox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class HTMLProcessDocumentInput {

    public final static String get() throws Exception {
        
        Document doc = XMLProcessor.getDocument("html");
        
        Element root = doc.getDocumentElement();
        root.setAttribute("dir", "rtl");
        root.setAttribute("lang", "he");
        
        root.appendChild(HTMLGeneratorToolbox.getHead(Targets.PROCESS_DOCUMENT_INPUT.getTitle(), WebConstants.VERSION, doc, HTMLHeader.getScripts(), HTMLHeader.getStyles()));

        root.appendChild(getBody(doc));

        return XMLProcessor.convertDocumentToString(doc);
        
    }

    private static Element getBody(Document doc) throws Exception {
        
        Element elt = HTMLGeneratorToolbox.getBody(doc);
        
        Element subElt = HTMLGeneratorToolbox.getForm(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl(), true, doc);

        subElt.appendChild(HTMLGeneratorToolbox.getTitle(Targets.PROCESS_DOCUMENT_INPUT.getTitle(), doc));

        Element para = HTMLGeneratorToolbox.getParagraph("\u23EC "+"תבחרו פסק דין מאתר בית המשפט העליון (https://supremedecisions.court.gov.il), תעתיקו את הכתובת למטה ותלחצו על", doc);

        subElt.appendChild(para);
        
         Element divInput = doc.createElement("div");
              
        Element input = doc.createElement("input");
        input.setAttribute("name", FormFields.ID.getName());
        input.setAttribute("type", "text");
        input.setAttribute("autofocus", "autofocus");
        input.setAttribute("width", "95%");
        input.setAttribute("style", "display:block; clear:both; float:right; width:95%");
        Element rightInputDiv = doc.createElement("div");
        rightInputDiv.setAttribute("style", "display:block; float:left; width:95%");
        rightInputDiv.appendChild(input);
        divInput.appendChild(rightInputDiv);
      
        Element buttonText = doc.createElement("button");
        buttonText.setTextContent("\u23EC");
        buttonText.setAttribute("class", "link");  
        buttonText.setAttribute("style", "display:block; clear:both; float:right;");
        Element rightInputDiv2 = doc.createElement("div");
        rightInputDiv2.setAttribute("style", "display:block; float:left; width:5%");
        rightInputDiv2.appendChild(buttonText);
        divInput.appendChild(rightInputDiv2);
        
        subElt.appendChild(divInput);
        
        Element AdditonalLinksPara = doc.createElement("p");
        
        AdditonalLinksPara.setAttribute("style", "display:block; width:100%; clear:both; float:right;");
        AdditonalLinksPara.setTextContent("להלן מספר פסקי דין לדוגמא:");

        StringBuilder redirectURL1 = new StringBuilder();
        redirectURL1.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL1.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\14\\530\\049\\t06&fileName=14049530_t06.txt&type=2"));        
        Element link1 = HTMLGeneratorToolbox.getBlockLink(redirectURL1.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\14\\530\\049\\t06&fileName=14049530_t06.txt&type=2", "right", doc);
        
        StringBuilder redirectURL2 = new StringBuilder();
        redirectURL2.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL2.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\11\\250\\076\\e07&fileName=11076250_e07.txt&type=2"));                
        Element link2 = HTMLGeneratorToolbox.getBlockLink(redirectURL2.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\11\\250\\076\\e07&fileName=11076250_e07.txt&type=2", "right", doc);

        StringBuilder redirectURL3 = new StringBuilder();
        redirectURL3.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL3.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\15\\640\\084\\z06&fileName=15084640.Z06&type=2"));                
        Element link3 = HTMLGeneratorToolbox.getBlockLink(redirectURL3.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\15\\640\\084\\z06&fileName=15084640.Z06&type=2","right" , doc);
        
        StringBuilder redirectURL4 = new StringBuilder();
        redirectURL4.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL4.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\11\\200\\076\\w16&fileName=11076200_w16.txt&type=2"));                
        Element link4 = HTMLGeneratorToolbox.getBlockLink(redirectURL4.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\11\\200\\076\\w16&fileName=11076200_w16.txt&type=2", "right",doc);
        
        StringBuilder redirectURL5 = new StringBuilder();
        redirectURL5.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL5.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\16\\620\\070\\e02&fileName=16070620_e02.txt&type=2"));                
        Element link5 = HTMLGeneratorToolbox.getBlockLink(redirectURL5.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\16\\620\\070\\e02&fileName=16070620_e02.txt&type=2", "right",doc);
        
        StringBuilder redirectURL6 = new StringBuilder();
        redirectURL6.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL6.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts%5C%5C%5C%5C%5C%5C%5C%5C16%5C%5C%5C%5C%5C%5C%5C%5C540%5C%5C%5C%5C%5C%5C%5C%5C070%5C%5C%5C%5C%5C%5C%5C%5Cd13&fileName=16070540.D13&type=2"));                
        Element link6 = HTMLGeneratorToolbox.getBlockLink(redirectURL6.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts%5C%5C%5C%5C%5C%5C%5C%5C16%5C%5C%5C%5C%5C%5C%5C%5C540%5C%5C%5C%5C%5C%5C%5C%5C070%5C%5C%5C%5C%5C%5C%5C%5Cd13&fileName=16070540.D13&type=2", "right",doc);

        StringBuilder redirectURL7 = new StringBuilder();
        redirectURL7.append(Targets.PROCESS_DOCUMENT_OUTPUT.getUrl());
        redirectURL7.append("?").append(FormFields.ID.getName()).append("=").append(Encoder.encodeText("https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\16\\540\\070\\d13&fileName=16070540.D13&type=2"));                
        Element link7 = HTMLGeneratorToolbox.getBlockLink(redirectURL6.toString(), "https://supremedecisions.court.gov.il/Home/Download?path=HebrewVerdicts\\16\\540\\070\\d13&fileName=16070540.D13&type=2", "right",doc);
        
        elt.appendChild(subElt);
        
        elt.appendChild(AdditonalLinksPara);
        elt.appendChild(link1);
        elt.appendChild(link2);
        elt.appendChild(link3);
        elt.appendChild(link4);
        elt.appendChild(link5);
        elt.appendChild(link6);
        
        return elt;
        
    }
    
}
