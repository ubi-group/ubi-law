package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.FormFields;
import com.itcag.tagger.html.HTMLDocuments;
import com.itcag.util.Encoder;
import com.itcag.util.Printer;
import com.itcag.util.txt.TextToolbox;
import com.itcag.utils.api.ErrorMessage;
import com.itcag.utils.html.HTTPToolbox;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Documents extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            HTTPToolbox.prepareResponse(response);

            String html = HTMLDocuments.get(new ArrayList<>());
            try (PrintWriter out = response.getWriter()) {
                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }

        } catch (Exception ex) {
            
            Printer.printException(ex);
            response.sendError(418, ErrorMessage.getStandardMessage(ex.getMessage()));
            
        }
    
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            request.setCharacterEncoding("UTF-8");
            
            String query = request.getParameter(FormFields.QUERY.getName());
            if (TextToolbox.isReallyEmpty(query)) throw new IllegalArgumentException("Missing argument: " + FormFields.QUERY.getName());
            query = Encoder.decodeText(query);            

            ArrayList<com.itcag.tagger.lang.Document> documents = DataTierAPI.getDocuments(query);
        
            HTTPToolbox.prepareResponse(response);

            String html = HTMLDocuments.get(documents);            
            try (PrintWriter out = response.getWriter()) {
                out.println(html);
            } catch (Exception ex) {
                throw ex;
            }

        } catch (Exception ex) {
            
            Printer.printException(ex);
            response.sendError(418, ErrorMessage.getStandardMessage(ex.getMessage()));
            
        }
    
    }

}
