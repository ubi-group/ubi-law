package com.itcag.demo.servlet;

import com.itcag.demo.DataTierAPI;
import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.demo.html.HTMLEditSentencesClassification;
import com.itcag.util.html.HTTPToolbox;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RejectClassification extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            
System.out.println("id=" + id);
System.out.println("strParagraphIndex=" + strParagraphIndex);

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
/*
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {           
            
            request.setCharacterEncoding("UTF-8");
            
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName());     	
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
System.out.println("id=" + id);
System.out.println("strParagraphIndex=" + strParagraphIndex);
System.out.println("sentenceText=" + sentenceText);

            if (TextToolbox.isReallyEmpty(sentenceText)) throw new IllegalArgumentException("Field is missing: " + FormFields.SENTENCE_TEXT.getName());
          
            DataTierAPI.rejectClassification(sentenceText);

            
            request.setAttribute(FormFields.ID.getName(), id);
            request.setAttribute(FormFields.PARAGRAPH_INDEX.getName(), strParagraphIndex);
            RequestDispatcher dispatcher = getServletContext().getRequestDispatcher(request.getContextPath() + "/" + Targets.EDIT_CLASSIFICATION_RESULT.getUrl());
            dispatcher.forward(request, response);      
            

        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }

    }
*/    
}
