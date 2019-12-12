package com.itcag.demo.servlet;

import com.itcag.demo.html.HTMLProcessDocumentInput;
import com.itcag.util.html.HTTPToolbox;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProcessDocumentInput extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            String html = HTMLProcessDocumentInput.get();
            
            HTTPToolbox.prepareResponse(response);

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
    
}
