package org.todo.auxiliary;

import data.TodoList;
import users.ObjectFactory;
import users.UserList;
import org.todo.*;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.XMLConstants;
import javax.xml.bind.*;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedList;


public class LoginRoutine {
    public static final String DATA_PATH_WEB_INF_DATA = "/WEB-INF/data/";
    public static final String DATA_PATH_WEB_INF_USER_DATA = DATA_PATH_WEB_INF_DATA+"/UserData";

    public static final String SRC_PATH = "/src/data/";

    private File templateUserToDoXml;
    //Fields
    private TodoUser activeTodoUser = new TodoUser();
    private String enteredUserName;
    private String enteredPassWord;
    private boolean firstTimeLogin = true;
    private boolean invalidLogin = true;
    private String servletContextPath;
    private ServletContext servletContext;
    private File userToDoXmlFile;


    //Constructor
    public LoginRoutine(HttpServletRequest request, HttpServletResponse response, UserList userDB, LinkedList<TodoUser> todoUserList, ServletContext servletContext){

        //Load entered username and pw
        enteredUserName = request.getParameter("name");
        enteredPassWord = request.getParameter("pw");
        this.servletContext  = servletContext;
        this.servletContextPath = servletContext.getRealPath("/");

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
        for (int i = 0; i<userDB.getUser().size(); i++){
            if(userDB.getUser().get(i).getUsername().equals(enteredUserName)){
                if(userDB.getUser().get(i).getPassword().equals(enteredPassWord)){
                    loginSuccessful(userDB, todoUserList, i,request);
                    activeTodoUser.initializeUserSession(request);
                    invalidLogin = false;
                    firstTimeLogin = false;
                    request.setAttribute("loginMessage", "Hello Again!");
                }
            }
        }
        /*
        for (int i = 0; i < todoUserList.size(); i++)
        {

            if (todoUserList.get(i).getUserName().equals(enteredUserName)) {
                if (todoUserList.get(i).getPassWord().equals(enteredPassWord)) {


                    loginSuccessful(todoUserList, i, request);
                    invalidLogin = false;
                    firstTimeLogin = false;
                    request.setAttribute("loginMessage", "Hello Again!");

                }
                else{
                    //password incorrect
                    firstTimeLogin = false;
                }
            }
        }
        */

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
            activeTodoUser = initializeUser(todoUserList, userDB, request);

            request.setAttribute("loginMessage", "First Time Login!");



        }
    }


    //Methods
    private void loginSuccessful(UserList userList, LinkedList<TodoUser> todoUserList, int id, HttpServletRequest request){
        activeTodoUser.setUserName(userList.getUser().get(id).getUsername());
        activeTodoUser.setPassWord(userList.getUser().get(id).getPassword());
        activeTodoUser.initializeUserSession(request);
    }
    //Exception handling Todo
    private void loginFailed(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        request.getRequestDispatcher("/incorrectlogin.html").forward(request, response);
    }

    private void setGeneralXmlFileParameters()
    {
        templateUserToDoXml = new File(this.servletContextPath + DATA_PATH_WEB_INF_USER_DATA + "/ToDoTemplate.xml");
    }

    private TodoUser initializeUser(LinkedList<TodoUser> todoUserList, UserList userDB, HttpServletRequest request){
        UserList.User newXMLUser = new UserList.User();
        newXMLUser.setUsername(enteredUserName);
        newXMLUser.setPassword(enteredPassWord);
        File newUserToDoXml = null;

        //here new user xml persistence
        File userList = new File(servletContextPath+DATA_PATH_WEB_INF_DATA+"/UserList.xml");
        File userListSchema = new File(servletContextPath+DATA_PATH_WEB_INF_DATA+"/UserList.xsd");
        try{
            JAXBContext jaxbContext = JAXBContext.newInstance(UserList.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(userListSchema);
            unmarshaller.setSchema(schema);
            userDB = (UserList) unmarshaller.unmarshal(userList);
            newXMLUser.setId((long) userDB.getUser().size());
            userDB.getUser().add(newXMLUser);

            Marshaller marshaller = jaxbContext.createMarshaller();
            marshaller.setSchema(schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            marshaller.marshal(userDB, userList);
        }
        catch (Exception e){
            System.err.println("initializeUser() Exception thrown");
        }


        //end new user xml persistence

        TodoUser newUser = new TodoUser(servletContext, request);
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

    private String getEnteredUserName()
            {
        return enteredUserName;
    }
    private String getEnteredPassWord(){
        return enteredPassWord;
    }


    public boolean isFirstTimeLogin()
    {
        return firstTimeLogin;
    }

    public TodoUser getActiveTodoUser()
    {
        return activeTodoUser;
    }
}
