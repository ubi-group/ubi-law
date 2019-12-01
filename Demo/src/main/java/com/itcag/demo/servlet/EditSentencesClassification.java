package com.itcag.demo.servlet;

import com.itcag.demo.DataTierAPI;
import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.html.HTMLEditSentencesClassification;
import com.itcag.util.html.HTTPToolbox;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditSentencesClassification extends HttpServlet {
      
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName()); 
            String categoryId = request.getParameter("search"); 
            String strIsAdditon = request.getParameter(FormFields.IS_CATEGORY_ADDITION.getName()); 
            boolean isAddition = Boolean.parseBoolean(strIsAdditon);

            if(isAddition) {
                if(sentenceText != null && !sentenceText.isEmpty()) {
                    DataTierAPI.indexClassification(sentenceText, categoryId);
                }                
            } else {
                if(sentenceText != null && !sentenceText.isEmpty()) {
                    DataTierAPI.rejectClassification(sentenceText, id);
                }                
            }

            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());
            if (TextToolbox.isReallyEmpty(strParagraphIndex)) throw new IllegalArgumentException("Field is missing: " + FormFields.PARAGRAPH_INDEX.getName());
            
            int paragraphIndex = Integer.parseInt(strParagraphIndex);
            
            com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(id);
            
            HTTPToolbox.prepareResponse(response);
            
            String html = HTMLEditSentencesClassification.get(doc, id, paragraphIndex);
           
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
