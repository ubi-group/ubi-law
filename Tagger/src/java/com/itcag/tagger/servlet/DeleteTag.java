package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.util.Encoder;
import com.itcag.util.Printer;
import com.itcag.util.txt.TextToolbox;
import com.itcag.utils.api.ErrorMessage;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteTag extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            request.setCharacterEncoding("UTF-8");
            
            String _id = request.getParameter(FormFields._ID.getName());
            if (TextToolbox.isReallyEmpty(_id)) throw new IllegalArgumentException("Missing argument: " + FormFields._ID.getName());
            _id = Encoder.decodeText(_id);            

            DataTierAPI.deleteTag(_id);
            
            Thread.sleep(WebConstants.UPDATE_DELAY);
            
            response.sendRedirect(Targets.TAGS.getUrl());
            
        } catch (Exception ex) {
            
            Printer.printException(ex);
            response.sendError(418, ErrorMessage.getStandardMessage(ex.getMessage()));
            
        }
    
    }

}
