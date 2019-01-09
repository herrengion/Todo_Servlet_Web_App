package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

public class LoginRoutine {

    //Fields
    private HttpSession userSession;
    private TodoUser activeTodoUser = new TodoUser();
    private String enteredUserName;
    private String enteredPassWord;
    private boolean firstTimeLogin = true;
    private boolean invalidLogin = true;


    //Constructor
    public LoginRoutine(HttpServletRequest request, HttpServletResponse response, ArrayList<TodoUser> todoUserList){

        //Load entered username and pw
        enteredUserName = request.getParameter("name");
        enteredPassWord = request.getParameter("pw");

        //if username is empty or not valid
        if ((enteredUserName == null || enteredUserName.isEmpty())) {
            try {
                loginFailed(request, response);
            }
            catch (IOException e){
                System.err.println("IOException Error: "+e);
            }
            catch(ServletException e){
                System.err.println("ServletException Error: "+e);
            }
        }

        //Check for user in database
        for (int i = 0; i < todoUserList.size(); i++) {
            if (todoUserList.get(i).getUserName().equals(enteredUserName)) {
                if (todoUserList.get(i).getPassWord().equals(enteredPassWord)) {
                    loginSuccessful(todoUserList, i);
                    initializeUserSession(request, response);
                    invalidLogin = false;
                    try {
                        request.setAttribute("loginMessage", "Hello Again!");
                        request.getRequestDispatcher("/todolist.jsp").forward(request, response);
                    }
                    catch (IOException e){

                    }
                    catch (ServletException e){

                    }
                }
                else{
                    //password incorrect
                    firstTimeLogin = false;
                }
            }
        }

        if(invalidLogin && (!firstTimeLogin)){
            try {
                loginFailed(request, response);
            }
            catch (IOException e){
                System.err.println("IOException Error: e");
            }
            catch (ServletException e){
                System.err.println("ServletException Error: e");
            }
        }

        if(firstTimeLogin){
            activeTodoUser = initializeUser(todoUserList);
            initializeUserSession(request, response);
            try {
                request.setAttribute("loginMessage", "First Time Login!");
                request.getRequestDispatcher("/todolist.jsp").forward(request, response);
            }
            catch (IOException e){

            }
            catch (ServletException e){

            }
        }
    }


    //Methods
    private void loginSuccessful(ArrayList<TodoUser> todoUserList, int id){
        activeTodoUser.setUserName(todoUserList.get(id).getUserName());
        activeTodoUser.setPassWord(todoUserList.get(id).getPassWord());
        activeTodoUser.setUserTodoList(todoUserList.get(id).getUserTodoList());
    }
    //Exception handling Todo
    private void loginFailed(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
    }

    private TodoUser initializeUser(ArrayList<TodoUser> todoUserList){
        TodoUser newUser = new TodoUser(enteredUserName, enteredPassWord);
        todoUserList.add(newUser);
        return newUser;
    }

    private void initializeUserSession(HttpServletRequest request, HttpServletResponse response){
            //Lock active user in HttpSession attributes
            userSession = request.getSession();
            userSession.setAttribute("name", activeTodoUser.getUserName());
            userSession.setAttribute("pw", activeTodoUser.getPassWord());
            userSession.setAttribute("todoList", activeTodoUser.getUserTodoList());
    }
    private String getEnteredUserName(){
        return enteredUserName;
    }
    private String getEnteredPassWord(){
        return enteredPassWord;
    }

    public HttpSession getUserSession() {
        return userSession;
    }

    public boolean isFirstTimeLogin() {
        return firstTimeLogin;
    }

    public TodoUser getActiveTodoUser() {
        return activeTodoUser;
    }
}
