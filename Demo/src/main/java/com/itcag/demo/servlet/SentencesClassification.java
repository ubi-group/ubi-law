package com.itcag.demo.servlet;

import com.itcag.demo.DataTierAPI;
import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.html.HTMLSentencesClassification;
import com.itcag.util.html.HTTPToolbox;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SentencesClassification extends HttpServlet {
      
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName()); 
            String categoryId = request.getParameter("search"); 
            String strIsBeingModified = request.getParameter(FormFields.IS_BEING_MODIFIED.getName()); 
            boolean isBeingModifed = Boolean.parseBoolean(strIsBeingModified);

            if(isBeingModifed) {
                
                DataTierAPI.rejectClassification(sentenceText);
                Thread.sleep(500);
                DataTierAPI.indexClassification(sentenceText, categoryId);
                Thread.sleep(500);
                
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
