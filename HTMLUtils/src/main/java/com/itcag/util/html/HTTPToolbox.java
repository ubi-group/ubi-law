package com.itcag.util.html;

import javax.servlet.http.HttpServletResponse;

public final class HTTPToolbox {
    
    public final static void prepareResponse(HttpServletResponse response) {
        
        response.setContentType("text/html;charset=UTF-8");

        /**
         * Ensure that no caching takes place ANYWHERE.
         */
        response.setHeader("Cache-Control", "no-cache, no-store, no-transform, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);            

    }
    
}
