package com.itcag.demo.servlet;

import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.html.HTMLProcessDocumentOutput;
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
            String paragraphIndex = request.getParameter(FormFields.PARAGRAPH_INDEX.getName()); 
System.out.println(id);
System.out.println("paragraphIndex:" + paragraphIndex);

            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());
            if (TextToolbox.isReallyEmpty(paragraphIndex)) throw new IllegalArgumentException("Field is missing: " + FormFields.PARAGRAPH_INDEX.getName());
            
            com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(id);
            
            HTTPToolbox.prepareResponse(response);
           
//            String html = HTMLProcessDocumentOutput.get(doc);
            String html = "";
            try (PrintWriter out = response.getWriter()) {
                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }
            
        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }

    }
    
    @Override
    public void destroy() {


    }
    
    
}
