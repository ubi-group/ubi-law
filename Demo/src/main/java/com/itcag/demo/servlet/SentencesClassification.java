package com.itcag.demo.servlet;

import com.itcag.demo.DataTierAPI;
import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.html.HTMLSentencesClassification;
import com.itcag.util.Encoder;
import com.itcag.util.html.HTTPToolbox;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SentencesClassification extends HttpServlet {
      
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            Map params = request.getParameterMap();
            Iterator i = params.keySet().iterator();
            while(i.hasNext()) {
                String key = (String) i.next();

                System.out.println("key=" + key);
            }
                    
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName()); 
            String categoryId = request.getParameter("search"); 

            String strRemove = request.getParameter(FormFields.REMOVE_TAG.getName()); 
            String strReplace = request.getParameter(FormFields.REPLACE_TAG.getName()); 
            String strAdd = request.getParameter(FormFields.ADD_TAG.getName()); 
System.out.println("strRemove:" + strRemove);
System.out.println("strReplace:" + strReplace);
System.out.println("strAdd:" + strAdd);
System.out.println("categoryId:" + categoryId);
            if(strAdd != null) {                 
                    DataTierAPI.indexClassification(strAdd, categoryId);
                    Thread.sleep(1000);

            } else if(strReplace != null) {                   
                    DataTierAPI.rejectClassification(strReplace);
                    Thread.sleep(1000);
                    DataTierAPI.indexClassification(strReplace, categoryId);
                    Thread.sleep(1000);               
            } else if(strRemove != null) {
 System.out.println("sentenceText:" + sentenceText);               
                if(sentenceText != null) {
                    String decodeSentence = Encoder.decodeText(sentenceText);
 System.out.println("decodeSentence:" + decodeSentence);                     
                    DataTierAPI.rejectClassification(decodeSentence);
                    Thread.sleep(1000);
                }
            }

                       
            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());
            if (TextToolbox.isReallyEmpty(strParagraphIndex)) throw new IllegalArgumentException("Field is missing: " + FormFields.PARAGRAPH_INDEX.getName());
            
            int paragraphIndex = Integer.parseInt(strParagraphIndex);
            
            com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(id);
            
            HTTPToolbox.prepareResponse(response);
            
            String html = HTMLSentencesClassification.get(doc, id, paragraphIndex);
           
            try (PrintWriter out = response.getWriter()) {
System.out.println(html);
                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }

        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }

    } 
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName()); 
            String categoryId = request.getParameter("search"); 

            String strRemove = request.getParameter(FormFields.REMOVE_TAG.getName()); 
            String strReplace = request.getParameter(FormFields.REPLACE_TAG.getName()); 
            String strAdd = request.getParameter(FormFields.ADD_TAG.getName()); 
System.out.println("strRemove:" + strRemove);
System.out.println("strReplace:" + strReplace);
System.out.println("strAdd:" + strAdd);
System.out.println("categoryId:" + categoryId);
            if(strAdd != null) {
                if(sentenceText != null) {                    
                    String sentence = Encoder.decodeText(sentenceText);
                    System.out.println("sentence:" + sentence);
                    DataTierAPI.indexClassification(sentenceText, categoryId);
                    Thread.sleep(1000);
                }
            } else if(strReplace != null) {
               if(sentenceText != null) {                    
                    String sentence = Encoder.decodeText(sentenceText);
                    System.out.println("sentence:" + sentence);
                    DataTierAPI.rejectClassification(sentenceText);
                    Thread.sleep(1000);
                    DataTierAPI.indexClassification(sentenceText, categoryId);
                    Thread.sleep(1000);
                }                
            } else if(strRemove != null) {
                DataTierAPI.rejectClassification(sentenceText);
                Thread.sleep(1000);
            }
 
            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());
            if (TextToolbox.isReallyEmpty(strParagraphIndex)) throw new IllegalArgumentException("Field is missing: " + FormFields.PARAGRAPH_INDEX.getName());
            
            int paragraphIndex = Integer.parseInt(strParagraphIndex);
            
            com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(id);
            
            HTTPToolbox.prepareResponse(response);
            
            String html = HTMLSentencesClassification.get(doc, id, paragraphIndex);
           
            try (PrintWriter out = response.getWriter()) {

                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }

        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }  
    }    
    
}
