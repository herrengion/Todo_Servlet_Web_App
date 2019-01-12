package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

public class LoginRoutine {
    public static final String DATA_PATH_WEB_INF_DATA = "/WEB-INF/data/";
    public static final String DATA_PATH_WEB_INF_USER_DATA = DATA_PATH_WEB_INF_DATA+"/UserData";
    private File templateUserToDoXml;
    //Fields
    private HttpSession userSession;
    private TodoUser activeTodoUser = new TodoUser();
    private String enteredUserName;
    private String enteredPassWord;
    private boolean firstTimeLogin = true;
    private boolean invalidLogin = true;
    private String servletContextPath;
    private File userToDoXmlFile;


    //Constructor
    public LoginRoutine(HttpServletRequest request, HttpServletResponse response, ArrayList<TodoUser> todoUserList, String servletContextPath){

        //Load entered username and pw
        enteredUserName = request.getParameter("name");
        enteredPassWord = request.getParameter("pw");
        this.servletContextPath = servletContextPath;

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
        for (int i = 0; i < todoUserList.size(); i++)
        {
            enteredUserName="test";
            enteredPassWord="123";
            if (todoUserList.get(i).getUserName().equals(enteredUserName)) {
                if (todoUserList.get(i).getPassWord().equals(enteredPassWord)) {


                    loginSuccessful(todoUserList, i);
                    initializeUserSession(request, response);
                    invalidLogin = false;
                        request.setAttribute("loginMessage", "Hello Again!");

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
            setGeneralXmlFileParameters();
            activeTodoUser = initializeUser(todoUserList);

            initializeUserSession(request, response);
            request.setAttribute("loginMessage", "First Time Login!");

        }
    }


    //Methods
    private void loginSuccessful(ArrayList<TodoUser> todoUserList, int id){
        activeTodoUser.setUserName(todoUserList.get(id).getUserName());
        activeTodoUser.setPassWord(todoUserList.get(id).getPassWord());
    }
    //Exception handling Todo
    private void loginFailed(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
    }

    private void setGeneralXmlFileParameters()
    {
        templateUserToDoXml = new File(this.servletContextPath + DATA_PATH_WEB_INF_USER_DATA + "/ToDoTemplate.xml");
    }

    private TodoUser initializeUser(ArrayList<TodoUser> todoUserList){
        TodoUser newUser = new TodoUser(enteredUserName, enteredPassWord);
        File newUserToDoXml = null;
        todoUserList.add(newUser);
        // Create new empty template to persist his todos
        System.out.println("New User is: "+enteredUserName);
        System.out.println("Initial context path: "+ servletContextPath);
        File newDirUserData = new File(servletContextPath + DATA_PATH_WEB_INF_USER_DATA + "/" + enteredUserName);
        if(newDirUserData.mkdirs())
        {
            try {
                newUserToDoXml = new File(servletContextPath +
                        DATA_PATH_WEB_INF_USER_DATA +
                        "/" + enteredUserName + "/ToDo_list_" + enteredUserName+".xml");
                Files.copy(templateUserToDoXml.toPath(),newUserToDoXml.toPath());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            userToDoXmlFile = newUserToDoXml;

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
            //userSession.setAttribute("todoList", activeTodoUser.getUserTodoList());
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
