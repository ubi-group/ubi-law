package com.itcag.demo.servlet;

import com.itcag.demo.FormFields;
import com.itcag.demo.WebConstants;
import com.itcag.demo.html.HTMLTest;
import com.itcag.legalyzer.test.Result;
import com.itcag.legalyzer.test.Tester;
import com.itcag.util.Encoder;
import com.itcag.util.Printer;
import com.itcag.util.txt.TextToolbox;
import com.itcag.utils.html.HTMLErrorPageGenerator;
import com.itcag.utils.html.HTTPToolbox;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Test extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {

            HTTPToolbox.prepareResponse(response);

            String html = HTMLTest.get(null, null);
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

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {

            request.setCharacterEncoding("UTF-8");
            
            String query = request.getParameter(FormFields.QUERY.getName());
            if (TextToolbox.isReallyEmpty(query)) throw new IllegalArgumentException("Missing argument: " + FormFields.QUERY.getName());
            query = Encoder.decodeText(query);
            
            Result result = new Result(WebConstants.CATEGORIES_PATH);
            
            Tester tester = Tester.getInstance();
            tester.test(query, result);
            
            HTTPToolbox.prepareResponse(response);

            String html = HTMLTest.get(query, result);
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
