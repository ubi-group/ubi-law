package com.itcag.demo.servlet;

import com.itcag.demo.DataTierAPI;
import com.itcag.demo.FormFields;
import com.itcag.demo.Targets;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
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
            
            String sentenceText = request.getParameter(FormFields.SENTENCE_TEXT.getName());     	
            String id = request.getParameter(FormFields.ID.getName());     
            String strParagraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
            
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
    
}
