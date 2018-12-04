package org.todo;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;

@WebServlet("/todoUser.do")
public class TodoUser extends HttpServlet {
    //Credentials
    private String activeUserName = new String();
    private String activePassWord = new String();
    private LinkedList<TodoEntry> activeUserTodoList = new LinkedList();

    //User/pw List - List<ArrayList>
    private ArrayList<String> userList = new ArrayList<>();
    private ArrayList<String> pwList = new ArrayList<>();
    private ArrayList<LinkedList<TodoEntry>> todoList = new ArrayList<>();

    //Todos
    private String activeTodoContent = null;
    //private String activeRedirectPath = null;

    //HTTP Methods
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String activeRedirectPath = request.getParameter("redirect");
        response.setContentType("text/html");
        if(activeRedirectPath.equals("login")) {
            try (PrintWriter out = response.getWriter()) {

                out.println("<html><body><h1>" + activeRedirectPath + "</h1></body></html>");
            }
        }
        loginRoutine(request, response);
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //String name = request.getParameter("name");
        loginRoutine(request, response);
            //case "newtodo":
    }

    //Auxiliary Methods
    //General login routine
    private void loginRoutine(HttpServletRequest request, HttpServletResponse response)throws IOException,ServletException{
        activeUserName = request.getParameter("name");
        activePassWord = request.getParameter("pw");
        //get request from newtodo.jsp
        //activeTodoContent = request.getParameter("Todo");

        String loginMessage = "First Time Login";
        if ((activeUserName == null || activeUserName.isEmpty()) && (activeTodoContent == null || activeTodoContent.isEmpty())) {
            request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
        }
        for (int i = 0; i < userList.size(); i++) {
            if (userList.get(i).equals(activeUserName)) {
                if (pwList.get(i).equals(activePassWord)) {
                    loginMessage = "Hello Again";
                    loginSuccessful(request, response, loginMessage);
                } else {
                    loginFailed(request, response);
                }

            }
        }
        if (loginMessage.equals("First Time Login")) {
            /*userList.add(activeUserName);
            pwList.add(activePassWord);
            todoList.add(activeUserTodoList);*/
            initializeUser(activeUserName, activePassWord, activeUserTodoList);
            loginSuccessful(request, response, loginMessage);
        }
    }
    //Routine when login successful
    private void loginSuccessful(HttpServletRequest request, HttpServletResponse response, String loginMessage) throws IOException,ServletException{
        request.setAttribute("name", activeUserName);
        request.setAttribute("pw", activePassWord);
        request.setAttribute("loginMessage", loginMessage);
        request.getRequestDispatcher("/todolist.jsp").forward(request, response);
    }
    //Routine when login failed
    private void loginFailed(HttpServletRequest request, HttpServletResponse response) throws IOException,ServletException{
        request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
    }
    //Routine when first time user
    private void initializeUser(String activeUserName, String activePassWord, LinkedList<TodoEntry> activeUserTodoList){
        userList.add(activeUserName);
        pwList.add(activePassWord);
        todoList.add(activeUserTodoList);
    }

    //Public get/set routines
    public String getActiveUserName(){return activeUserName;}

    public String getActivePassWord(){return activePassWord;}

    public LinkedList<TodoEntry> getActiveUserTodoList(){return activeUserTodoList;}

}
