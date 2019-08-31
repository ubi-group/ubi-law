package com.itcag.demo.servlet;

import com.itcag.demo.WebConstants;
import com.itcag.demo.html.HTMLExamples;
import com.itcag.util.Printer;
import com.itcag.util.html.HTMLErrorPageGenerator;
import com.itcag.util.html.HTTPToolbox;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Examples extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {

            HTTPToolbox.prepareResponse(response);

            String html = HTMLExamples.get();
            try (PrintWriter out = response.getWriter()) {
                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }

        } catch (Exception ex) {
            
            Printer.printException(ex);

            try (PrintWriter out = response.getWriter()) {
                out.println(HTMLErrorPageGenerator.get(ex, WebConstants.GENERAL_TITLE, WebConstants.VERSION, WebConstants.CONTEXT_PATH));
            } catch (Exception unexp) {
                response.sendError(0, "Unexpected error: " + unexp.getMessage());
            }
            
        }

    }

}
