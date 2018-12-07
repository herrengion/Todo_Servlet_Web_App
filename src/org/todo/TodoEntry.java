package org.todo;


import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.LinkedList;

//@WebServlet("/todoEntry.do")
public class TodoEntry /*extends HttpServlet*/ {
   /* public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {

            out.println("<html><body><h1>'TodoEntry'</h1></body></html>");
        }
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }*/
    //Fields
    private int id = 0;
    private String title = "blank Todo";
    private String category = "Generic";
    private LocalDate dueDate;
    private boolean important = false;
    private boolean completed = false;

    //Constructor
    public TodoEntry(int id, String title, String category, LocalDate dueDate, boolean important){
        this.id = id;
        this.title = title;
        this.category = category;
        this.dueDate = dueDate;
        this.important = important;
        this.completed = false;
    }
    //Get Methods
    public int getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getCategory(){return category;}
    public LocalDate getDueDate(){return dueDate;}
    public boolean isCompleted() {return completed;}
    public boolean isImportant() {return important;}

    //Set Methods

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setImportant(boolean important) {
        this.important = important;
    }
}
