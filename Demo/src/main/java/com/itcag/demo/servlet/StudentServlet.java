/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.itcag.demo.servlet;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.PrintWriter;


public class StudentServlet extends HttpServlet {
 private static final long serialVersionUID = 1L;
 
@Override
public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
doPost(request, response); 
}

protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
System.out.println("Student servlet called!");
    String userInput = request.getParameter("term");
    System.out.println(userInput);
 // We can get auxiliary parameters sent from select2 like below: 
 // request.getParameter("aux_variable"); 
String json = ""; 
List<String> studentList = getStudentList(); 
List<HashMap<String, String>> listOfMapForJSONConv = new ArrayList<HashMap<String,String>>();
for(String s : studentList) {
HashMap<String, String> map = new HashMap<String, String>();
map.put("text", s);
listOfMapForJSONConv.add(map);
}

Gson gson = new GsonBuilder().create();
json = gson.toJson(listOfMapForJSONConv);
response.setContentType("application/json");
PrintWriter out = response.getWriter();
out.print(json);
}

private List<String> getStudentList() {
List<String> studentList = new ArrayList<String>();
studentList.add("Abhijeet");
studentList.add("Abhishek");
studentList.add("Anurag");
studentList.add("Anuj");
studentList.add("Gaurav");
studentList.add("Subodh");
studentList.add("Phani");
return studentList;
} 
}