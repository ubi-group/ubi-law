package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.tagger.html.HTMLAddTag;
import com.itcag.tagger.lang.Tag;
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

public class AddTag extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        try {

            HTTPToolbox.prepareResponse(response);

            String html = HTMLAddTag.get();
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
            
            String id = request.getParameter(FormFields.ID.getName());
            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Missing data: " + FormFields.ID.getName());
            id = id.trim();
            
            String label = request.getParameter(FormFields.LABEL.getName());
            if (TextToolbox.isReallyEmpty(id)) throw new IllegalArgumentException("Missing data: " + FormFields.LABEL.getName());
            label = label.trim();

            Tag tag = new Tag(id, label);
            
            DataTierAPI.addTag(tag);
            
            Thread.sleep(WebConstants.UPDATE_DELAY);
            
            response.sendRedirect(Targets.TAGS.getUrl());

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