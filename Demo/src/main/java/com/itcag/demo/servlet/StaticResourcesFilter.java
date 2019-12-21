package com.itcag.demo.servlet;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

    public class StaticResourcesFilter implements Filter {
    
    private ServletContext context;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        
        HttpServletRequest httprequest = (HttpServletRequest) request;
        
        String requestURI = httprequest.getRequestURI();

        if(requestURI.equalsIgnoreCase("/Demo/")) {
            request.getRequestDispatcher("/ProcessDocumentInput").forward(request, response);;            
        } else {
            chain.doFilter(request, response);
            return;            
        }
              
    }
    
    @Override
    public void destroy() {
        
    }
    
    @Override
    public void init(FilterConfig fConfig) throws ServletException  {
        this.context =fConfig.getServletContext();
    }
}
