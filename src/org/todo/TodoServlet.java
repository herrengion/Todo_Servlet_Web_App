package org.todo;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedList;

@WebServlet("/todoFSM.do")
public class TodoServlet extends HttpServlet {
    //Credentials
    private String activeUserName = new String();
    private String activePassWord = new String();
    private LinkedList<TodoEntry> activeUserTodoList = new LinkedList();

    //Public get/set routines
    public String getActiveUserName(){return activeUserName;}
    public String getActivePassWord(){return activePassWord;}
    public LinkedList<TodoEntry> getActiveUserTodoList(){return activeUserTodoList;}


    //Users
    private ArrayList<TodoUser> todoUserList = new ArrayList<>();


    //HTTP Methods
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


        String activeRedirectPath = request.getParameter("redirect");
        request.setAttribute("name",activeUserName);
        request.setAttribute("pw", activePassWord);
        for(int i = 0; i<todoUserList.size(); i++){
            if(activeUserName == todoUserList.get(i).getUserName()){
                activePassWord = todoUserList.get(i).getPassWord();
                activeUserTodoList = todoUserList.get(i).getUserTodoList();
            }
        }

            switch (activeRedirectPath) {
                case "login":
                    loginRoutine(request, response);
                    break;
                case "newTodo":
                    newTodoRoutine(request, response);
                    break;

                case "toUpdateTodo":
                    toUpdateTodo(request, response);
                    break;

                case "fromUpdateTodo":
                    fromUpdateTodo(request, response);
                    break;

                case "discardTodo":
                    discardTodo(request, response);
                    break;

                case "todoCompletedToggle":
                    todoCompletedToggle(request, response);
                    break;

                default:
                    defaultRoutine(request, response);
                    break;
            }
    }

    //Auxiliary Methods
    //General login routine---------------------------------------------------------------------------------------------
    private void loginRoutine(HttpServletRequest request, HttpServletResponse response)
                    throws IOException,ServletException{
        activeUserName = request.getParameter("name");
        activePassWord = request.getParameter("pw");

        String loginMessage = "First Time Login";
        if ((activeUserName == null || activeUserName.isEmpty()) /*&& (activeTodoContent == null || activeTodoContent.isEmpty())*/) {
            request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
        }
        for (int i = 0; i < todoUserList.size(); i++) {
            if (todoUserList.get(i).getUserName().equals(activeUserName)) {
                if (todoUserList.get(i).getPassWord().equals(activePassWord)) {
                    loginMessage = "Hello Again";
                    activeUserTodoList = todoUserList.get(i).getUserTodoList();
                    request.setAttribute("todoList", activeUserTodoList);
                    loginSuccessful(request, response, loginMessage);
                } else {
                    loginFailed(request, response);
                }

            }
        }
        if (loginMessage.equals("First Time Login")) {
            initializeUser(activeUserName, activePassWord);
            loginSuccessful(request, response, loginMessage);
        }
    }
            //Routine when login successful
            private void loginSuccessful(HttpServletRequest request, HttpServletResponse response, String loginMessage)
                    throws IOException,ServletException{
                request.setAttribute("name", activeUserName);
                request.setAttribute("pw", activePassWord);
                request.setAttribute("loginMessage", loginMessage);
                //request.setAttribute("todoList", activeUserTodoList);
                request.getRequestDispatcher("/todolist.jsp").forward(request, response);
            }
            //Routine when login failed
            private void loginFailed(HttpServletRequest request, HttpServletResponse response)
                    throws IOException,ServletException{
                request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
            }
            //Routine when first time user
            private void initializeUser(String activeUserName, String activePassWord){
                todoUserList.add(new TodoUser(activeUserName, activePassWord));
            }
    //------------------------------------------------------------------------------------------------------------------
    private void newTodoRoutine(HttpServletRequest request, HttpServletResponse response){
        String newTodoString = request.getParameter("newTodo");

        String dueDateString = request.getParameter("dueDate");
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dueDate;
        if (dueDateString.isEmpty()) {
            dueDate = null;
        } else {
            dueDate = LocalDate.parse(dueDateString, f);
        }
        String categoryString = request.getParameter("category");
        String highPriority = request.getParameter("priority");
        activeUserTodoList.add(new TodoEntry(activeUserTodoList.size() + 1, newTodoString, categoryString, dueDate, highPriority.equals("high")));
        request.setAttribute("loginMessage", "Todo added!");
        request.setAttribute("todoList", activeUserTodoList);
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void toUpdateTodo(HttpServletRequest request, HttpServletResponse response){
        String updateTodoId = request.getParameter("todoID");
        int updateTodoIdIntZeroed = Integer.parseInt(updateTodoId) - 1;
        request.setAttribute("todoTitle", activeUserTodoList.get(updateTodoIdIntZeroed).getTitle());
        request.setAttribute("todoDate", activeUserTodoList.get(updateTodoIdIntZeroed).getDueDate());
        if (activeUserTodoList.get(updateTodoIdIntZeroed).isImportant()) {
            request.setAttribute("highPriority", "checked");
        } else {
            request.setAttribute("normalPriority", "checked");
        }
        request.setAttribute("category", activeUserTodoList.get(updateTodoIdIntZeroed).getCategory());
        request.setAttribute("todoId", updateTodoId);
        //request.getRequestDispatcher("/edittodo.jsp").forward(request, response);
        try {
            request.getRequestDispatcher("/edittodo.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void fromUpdateTodo(HttpServletRequest request, HttpServletResponse response){
        String updatedTodoId = request.getParameter("todoId");
        int updatedTodoIdIntZeroed = Integer.parseInt(updatedTodoId) - 1;

        String updatedTodoTitle = request.getParameter("newTodo");

        String updatedDueDateString = request.getParameter("dueDate");
        DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate updatedDueDate;
        if (updatedDueDateString.isEmpty()) {
            updatedDueDate = null;
        } else {
            updatedDueDate = LocalDate.parse(updatedDueDateString, frm);
        }
        String updatedCategoryString = request.getParameter("category");
        String updatedHighPriority = request.getParameter("priority");
        activeUserTodoList.get(updatedTodoIdIntZeroed).setTitle(updatedTodoTitle);
        activeUserTodoList.get(updatedTodoIdIntZeroed).setDueDate(updatedDueDate);
        activeUserTodoList.get(updatedTodoIdIntZeroed).setCategory(updatedCategoryString);
        if (updatedHighPriority.equals("high")) {
            activeUserTodoList.get(updatedTodoIdIntZeroed).setImportant(true);
        } else {
            activeUserTodoList.get(updatedTodoIdIntZeroed).setImportant(false);
        }
        request.setAttribute("loginMessage", "Todo : '" + updatedTodoTitle + "' updated!");
        request.setAttribute("todoList", activeUserTodoList);
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
        //request.getRequestDispatcher("/todolist.jsp").forward(request, response);
    }
    //------------------------------------------------------------------------------------------------------------------
    private void discardTodo(HttpServletRequest request, HttpServletResponse response){
        String discardTodoId = request.getParameter("todoID");
        int discardTodoIdIntZeroed = Integer.parseInt(discardTodoId) - 1;
        String discardedTodoTitle = activeUserTodoList.get(discardTodoIdIntZeroed).getTitle();
        activeUserTodoList.remove(discardTodoIdIntZeroed);
        for (int i = activeUserTodoList.size() - 1; i >= 0; i--) {
            activeUserTodoList.get(i).setId(i + 1);
        }
        request.setAttribute("loginMessage", "Todo Item: '" + discardedTodoTitle + "' discarded!");
        request.setAttribute("todoList", activeUserTodoList);
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
        //request.getRequestDispatcher("/todolist.jsp").forward(request, response);
    }
    //------------------------------------------------------------------------------------------------------------------
    private void todoCompletedToggle(HttpServletRequest request, HttpServletResponse response){
        String toggleTodoId = request.getParameter("todoID");
        int toggleTodoIdIntZeroed = Integer.parseInt(toggleTodoId) - 1;
        if (activeUserTodoList.get(toggleTodoIdIntZeroed).isCompleted()) {
            activeUserTodoList.get(toggleTodoIdIntZeroed).setCompleted(false);
        } else {
            activeUserTodoList.get(toggleTodoIdIntZeroed).setCompleted(true);
        }
        request.setAttribute("loginMessage", "Todo Item: '" + activeUserTodoList.get(toggleTodoIdIntZeroed).getTitle() + "' status changed!");
        request.setAttribute("todoList", activeUserTodoList);
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
        //request.getRequestDispatcher("/todolist.jsp").forward(request, response);
    }
    //------------------------------------------------------------------------------------------------------------------
    private void defaultRoutine(HttpServletRequest request, HttpServletResponse response){
        try {
            request.getRequestDispatcher("/oopserror.html").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
        //request.getRequestDispatcher("/oopserror.html").forward(request, response);
    }
    //------------------------------------------------------------------------------------------------------------------
}

