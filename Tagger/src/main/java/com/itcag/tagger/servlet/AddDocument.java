package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.tagger.doc.Extractor;
import com.itcag.doc.lang.Document;
import com.itcag.util.Printer;
import com.itcag.util.html.HTMLErrorPageGenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

public class AddDocument extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            
            Part filePart = request.getPart(FormFields.UPLOAD.getName());
            if (filePart != null) {
                
                String extension = null;
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                int pos = fileName.indexOf(".");
                if (pos > -1)  extension = fileName.substring(pos);
                
                String id = UUID.randomUUID().toString().replace("-", "");
                String filePath = WebConstants.UPLOAD_FOLDER + id;
                if (extension != null) filePath += extension;

                Path path = Paths.get(filePath);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, path);
                }

                Extractor extractor = new Extractor();
                Document doc = extractor.getDocument(id, extension, filePath);
                DataTierAPI.addDocument(doc);
                
            }

            String url = Targets.DOCUMENTS.getUrl();
            response.sendRedirect(url);

        } catch (Exception ex) {

            Printer.printException(ex);

            try (PrintWriter out = response.getWriter()) {
                out.println(HTMLErrorPageGenerator.get(ex, Targets.ERROR.getTitle(), WebConstants.VERSION, WebConstants.CONTEXT_PATH));
            } catch (Exception unexp) {
                response.sendError(0, "Unexepcted error: " + unexp.getMessage());
            }

        }

    }

}
