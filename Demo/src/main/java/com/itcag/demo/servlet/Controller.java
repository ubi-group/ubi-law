package com.itcag.demo.servlet;

import com.google.gson.Gson;
import com.itcag.demo.AutocompletionCategories;
import com.itcag.demo.Config;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Controller extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
System.out.println("!!!!!!!!!!!!!!");
		response.setContentType("application/json");
                
		try {
                    
			String term = request.getParameter("term");
System.out.println("Data from ajax call " + term);
                        
                        ArrayList autoCompletion = new ArrayList();
                        
System.out.println(AutocompletionCategories.getInstance(Config.ALL_CATEGORIES).getAllCategories());                          
                        term = term.toLowerCase();
                        for(String cat: AutocompletionCategories.getInstance(Config.ALL_CATEGORIES).getAllCategories()) {
                            cat = cat.toLowerCase();
System.out.println("cat:" + cat);                            
                            if(cat.contains(term)){
                                autoCompletion.add(cat);
System.out.println("Match: " + cat);
                            }
                        }

			String searchList = new Gson().toJson(autoCompletion);
			response.getWriter().write(searchList);
                        
		} catch (Exception e) {
                        System.err.println(e.getMessage());
		}
	}
    
}
