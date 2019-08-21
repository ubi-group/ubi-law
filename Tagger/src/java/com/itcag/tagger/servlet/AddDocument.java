package com.itcag.tagger.servlet;

import com.itcag.tagger.DataTierAPI;
import com.itcag.tagger.FormFields;
import com.itcag.tagger.Targets;
import com.itcag.tagger.WebConstants;
import com.itcag.tagger.doc.Extractor;
import com.itcag.tagger.lang.Document;
import com.itcag.util.Printer;
import com.itcag.utils.html.HTMLErrorPageGenerator;

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
                
                String ext = null;
                String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
                int pos = fileName.indexOf(".");
                if (pos > -1)  ext = fileName.substring(pos);
                
                String id = UUID.randomUUID().toString().replace("-", "");
                String location = WebConstants.UPLOAD_FOLDER + id;
                if (ext != null) location += ext;

                Path path = Paths.get(location);
                try (InputStream input = filePart.getInputStream()) {
                    Files.copy(input, path);
                }

                File file = path.toFile();
                
                Extractor extractor = new Extractor();
                Document doc = extractor.getDocument(id, file);
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
