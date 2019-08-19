package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.html.HTMLTags;
import com.itcag.tagger.lang.Tag;
import com.itcag.util.Printer;
import com.itcag.utils.api.ErrorMessage;
import com.itcag.utils.html.HTTPToolbox;

import java.io.IOException;
import java.io.PrintWriter;

import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Tags extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            ArrayList<Tag> tags = DataTierAPI.getTags();
        
            HTTPToolbox.prepareResponse(response);

            String html = HTMLTags.get(tags);            
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
