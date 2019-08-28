package com.itcag.demo.servlet;

import com.itcag.demo.WebConstants;
import com.itcag.legalyzer.ConfigurationFields;
import com.itcag.legalyzer.test.Tester;

import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class Initiator extends HttpServlet {
    
     @Override
     public void init() throws ServletException {

        Properties config = new Properties();
        
        config.setProperty(ConfigurationFields.WORD_VECTOR_PATH.getName(), WebConstants.WORD_VECTOR_PATH);
        config.setProperty(ConfigurationFields.MODEL_PATH.getName(), WebConstants.MODEL_PATH);
        config.setProperty(ConfigurationFields.CATEGORIES_PATH.getName(), WebConstants.CATEGORIES_PATH);
        
        try {
            Tester.getInstance(config);
            System.out.println("Word embedding and model successfully loaded.");
        } catch (Exception ex) {
            throw new ServletException(ex);
        }
         
     }

}
