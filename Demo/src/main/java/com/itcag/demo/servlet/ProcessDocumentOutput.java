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
import org.w3c.dom.Document;

public class ProcessDocumentOutput extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String url = request.getParameter(FormFields.ID.getName());        
           
            if (TextToolbox.isReallyEmpty(url)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());
            
            com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(url);
            
            HTTPToolbox.prepareResponse(response);
            
            String html = HTMLProcessDocumentOutput.get(doc);

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
