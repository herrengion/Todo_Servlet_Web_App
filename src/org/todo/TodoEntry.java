package org.todo;



import java.time.LocalDate;

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
    private int id;
    private String title;
    private String category;
    private LocalDate dueDate;
    private boolean important;
    private String priority = "low";
    private boolean completed;
    private String status = "Todo";

    //Constructor
    public TodoEntry(int id, String title, String category, LocalDate dueDate, boolean important) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.dueDate = dueDate;
        this.important = important;
        this.completed = false;
        if (this.important) {
            priority = "high";
        }
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
    public String getPriority() {return priority;}
    public String getStatus() {return status;}

    //Set Methods
    public void setId(int newId){
        this.id = newId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
        if(completed){
            this.status = "Done";
        }
        else {
            this.status = "Todo";
        }

    }
    public void setCompleted() {
        if(completed){
            this.status = "Done";
        }
        else {
            this.status = "Todo";
        }

    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void setImportant(boolean important) {
        this.important = important;
        this.priority = "low";
        if(this.important){
            this.priority = "high";
        }
    }
}
