package com.itcag.demo.servlet;

import com.itcag.demo.DocumentProcessor;
import com.itcag.demo.FormFields;
import com.itcag.demo.WebConstants;
import com.itcag.demo.html.HTMLErrorPageGenerator;
import com.itcag.demo.html.HTMLProcessDocumentOutput;
import com.itcag.util.html.HTTPToolbox;
import com.itcag.util.txt.TextToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessDocumentOutput extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String url = request.getParameter(FormFields.ID.getName());  
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
                        
            if(url == null || url.isEmpty() || !url.startsWith("https://supremedecisions.court.gov.il/")) {
                
                String msg = "הלינק שהכנסתם אינו מוביל למסד הנתונים המכיל פסקי דין של בית המשפט העליון. נסו שוב. הלינק חייב להשתייך לכתובת זו: https://supremedecisions.court.gov.il/";
                
                String errorPage = HTMLErrorPageGenerator.get(msg, "קלט שגוי", WebConstants.VERSION, null);
                
                 try (PrintWriter out = response.getWriter()) {
                    out.println(errorPage);
                } catch (Exception ex) {
                    throw ex;
                }
                 
            } else {
           
                if (TextToolbox.isReallyEmpty(url)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());

                com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(url);

                HTTPToolbox.prepareResponse(response);

                String html = HTMLProcessDocumentOutput.get(doc);

                try (PrintWriter out = response.getWriter()) {

                    out.println(html);
                } catch (Exception ex) {
                    throw ex;
                }
            }

        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }

    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            request.setCharacterEncoding("UTF-8");
            
            String url = request.getParameter(FormFields.ID.getName());  
            response.setContentType("text/html; charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
                        
            if(url == null || url.isEmpty() || !url.startsWith("https://supremedecisions.court.gov.il/")) {
                
                String msg = "הלינק שהכנסתם אינו מוביל למסד הנתונים המכיל פסקי דין של בית המשפט העליון. נסו שוב. הלינק חייב להשתייך לכתובת זו: https://supremedecisions.court.gov.il/";
                
                String errorPage = HTMLErrorPageGenerator.get(msg, "קלט שגוי", WebConstants.VERSION, null);
                
                 try (PrintWriter out = response.getWriter()) {
                    out.println(errorPage);
                } catch (Exception ex) {
                    throw ex;
                }
                 
            } else {
           
                if (TextToolbox.isReallyEmpty(url)) throw new IllegalArgumentException("Field is missing: " + FormFields.ID.getName());

                com.itcag.legalyzer.util.doc.Document doc = DocumentProcessor.classify(url);
System.out.println("com.itcag.legalyzer.util.doc.Document: " + doc);
                HTTPToolbox.prepareResponse(response);

                String html = HTMLProcessDocumentOutput.get(doc);

                try (PrintWriter out = response.getWriter()) {

                    out.println(html);
                } catch (Exception ex) {
                    throw ex;
                }
            }

        } catch (Exception ex) {
            
            throw new ServletException(ex);
            
        }

    }
}
