package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class LoginRoutine {
    public static final String DATA_PATH_WEB_INF_USER_DATA = "/WEB-INF/data/UserData";
    private File templateUserToDoXml;
    //Fields
    private HttpSession userSession;
    private TodoUser activeTodoUser = new TodoUser();
    private String enteredUserName;
    private String enteredPassWord;
    private boolean firstTimeLogin = true;
    private boolean invalidLogin = true;
    private String serveletContextPath;


    //Constructor
    public LoginRoutine(HttpServletRequest request, HttpServletResponse response, ArrayList<TodoUser> todoUserList, String servletContextPath){

        //Load entered username and pw
        enteredUserName = request.getParameter("name");
        enteredPassWord = request.getParameter("pw");
        this.serveletContextPath = servletContextPath;

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
            templateUserToDoXml = new File(serveletContextPath + DATA_PATH_WEB_INF_USER_DATA + "/ToDoTemplate.xml");
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
        // Create new empty template to persist his todos
        System.out.println("New User is: "+enteredUserName);
        System.out.println("Initial context path: "+serveletContextPath);
        File newDirUserData = new File(serveletContextPath + DATA_PATH_WEB_INF_USER_DATA + "/" + enteredUserName);
        if(newDirUserData.mkdirs())
        {
            try {
                File newUserToDoXml = new File(serveletContextPath +
                        DATA_PATH_WEB_INF_USER_DATA +
                        "/" + enteredUserName + "/ToDo_list_" + enteredUserName+".xml");
                Files.copy(templateUserToDoXml.toPath(),newUserToDoXml.toPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if(newDirUserData.isDirectory())
        {
            System.err.println("Directory exists already for user: "+enteredUserName);
        }
        else
        {
            System.err.println("Not able to create directory for user: "+enteredUserName);
        }

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
