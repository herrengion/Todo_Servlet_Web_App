package org.todo;
import com.sun.media.sound.InvalidDataException;
import org.todo.auxiliary.*;
import users.UserList;

//Done: create Classes for FSM functions, front controller paradigma (bis samstag)
//Todo: Sort after Category, sorted by default after due Date. Mark overdue Tasks in red, completed tasks in green. additional: implement sort after column
//Todo: REST interface, Data Persistence
//Todo: Exception handling, Http error pages
//Todo: Change UserList to LinkedList, possibility of deleting account
//Todo: Implement "forgot password" functionality

import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

@WebServlet("/todoFSM.do")
public class TodoServlet extends HttpServlet {

    //------------------------------------------------------------------------------------------------------------------
    //Fields:
    //------------------------------------------------------------------------------------------------------------------
    public static final String DATA_PATH_WEB_INF = "/WEB-INF";
    public static final String DATA_PATH_WEB_INF_DATA = DATA_PATH_WEB_INF + "/data";
    public static final String DATA_PATH_WEB_INF_USER_DATA = DATA_PATH_WEB_INF_DATA + "/UserData";
    //Active Session

    //All User Data
    /* TODO: Need to be persisted and still available after reboot of server -> store in File or XML too*/
    private LinkedList<TodoUser> todoUserList = new LinkedList<>();
    private UserList userDB = new UserList();

    //------------------------------------------------------------------------------------------------------------------
    //HTTP Methods:
    //------------------------------------------------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    //------------------------------------------------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TodoUser activeUser = new TodoUser();
        String errorMessge = "No error";
        HttpSession userSession;

        String activeRedirectPath = request.getParameter("redirect");
        ServletContext context = getServletContext();
        String contextPath = context.getRealPath("/");
        Map userSessionMap = new HashMap<String, String>();


        Enumeration<String> contextAttributeNames = context.getAttributeNames();
        while (contextAttributeNames.hasMoreElements()) {
            System.out.println("Context: " + contextAttributeNames.nextElement());
        }

        if (context.getAttribute("userSessionMap") != null) {
            userSessionMap = (Map) context.getAttribute("userSessionMap");
        }
        File xmlSchemaFile;
        File userToDoXmlFile;

        try {
            if (!activeRedirectPath.equals("login")) {

                activeUser.initializeUserSession(request);
                userSession = activeUser.getUserSession();
                if (null != context.getAttribute("userSessionMap")) {

                    if (userSessionMap.containsKey(userSession.getId())) {
                        activeUser.setUserName((String) userSessionMap.get(userSession.getId()));
                        userToDoXmlFile = new File(contextPath +
                                DATA_PATH_WEB_INF_USER_DATA +
                                "/" + activeUser.getUserName() + "/ToDo_list_" + activeUser.getUserName() + ".xml");
                        xmlSchemaFile = new File(contextPath + DATA_PATH_WEB_INF_DATA + "/ToDo.xsd");
                        activeUser.setUserTodoList(userToDoXmlFile, xmlSchemaFile);
                        System.out.println("Got valid session from user: " + userSessionMap.get(userSession.getId()));
                    } else {
                        throw new ServletException("Request is not login and no session is existing!");
                    }
                } else {
                    activeRedirectPath = "login";
                }

            }

            switch (activeRedirectPath) {

                case "login":
                    LoginRoutine loginRoutine = new LoginRoutine(request, response, userDB, context);
                    activeUser = loginRoutine.getActiveTodoUser();

                    userToDoXmlFile = new File(contextPath +
                            DATA_PATH_WEB_INF_USER_DATA +
                            "/" + activeUser.getUserName() + "/ToDo_list_" + activeUser.getUserName() + ".xml");
                    xmlSchemaFile = new File(contextPath + DATA_PATH_WEB_INF_DATA + "/ToDo.xsd");
                    activeUser.setUserTodoList(userToDoXmlFile, xmlSchemaFile);
                    activeUser.updateCategoryHashSet(activeUser.getUserTodoList());
                    userSession = activeUser.getUserSession();
                    if (!userSessionMap.containsKey(userSession.getId())) {
                        userSessionMap.put(userSession.getId(), activeUser.getUserName());
                        context.setAttribute("userSessionMap", userSessionMap);
                        System.out.println("Session would exist already");
                    }
                    request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
                    request.setAttribute("todoList", activeUser.getUserTodoList());
                    context.setAttribute("name", activeUser.getUserName());
                    request.getRequestDispatcher("/todolist.jsp").forward(request, response);

                    break;

                case "showTodos":
                    request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
                    request.setAttribute("todoList", activeUser.getUserTodoList());
                    context.setAttribute("name", activeUser.getUserName());
                    request.getRequestDispatcher("/todolist.jsp").forward(request, response);
                    break;

                case "newTodo":
                    NewTodo newTodo = new NewTodo(request, response, activeUser);

                    break;

                case "toUpdateTodo":
                    ToUpdateTodo toUpdateTodo = new ToUpdateTodo(request, response, activeUser);
                    break;

                case "fromUpdateTodo":
                    try {
                        FromUpdateTodo fromUpdateTodo = new FromUpdateTodo(request, response, activeUser);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;

                case "discardTodo":
                    DiscardTodo discardTodo = new DiscardTodo(request, response, activeUser);
                    break;

                case "todoCompletedToggle":
                    TodoCompletedToggle todoCompletedToggle = new TodoCompletedToggle(request, response, activeUser);
                    break;

                case "sortRoutine":
                    System.out.println("sort routine entered");
                    break;

                case "category":
                    String categoryName = request.getParameter("categoryList");
                    System.out.println("category sort routine entered. Value: " + categoryName);
                    activeUser.updateCategoryHashSet(activeUser.getUserTodoList());
                    activeUser.setSortedUserTodoList(categoryName);
                    request.setAttribute("todoList", activeUser.getSortedUserTodoList());
                    request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
                    request.getRequestDispatcher("/todolist.jsp").forward(request, response);
                    break;

                default:
                    errorMessge = "Switch passes default and no exception occurred!";
                    DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, errorMessge);
                    break;

            }
        } catch (LoginException e) {
            System.err.println("Login Error:" + e.getMessage());
            System.err.println(e.getStackTrace());
            errorMessge = "Login Error:" + e.getMessage();
            DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, errorMessge);
        } catch (ServletException e) {
            System.err.println("Servlet Error:" + e.getMessage());
            System.err.println(e.getStackTrace());
            errorMessge = "Servlet Error:" + e.getMessage();
            DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, errorMessge);
        } catch (InvalidDataException e) {
            System.err.println("Invalid Data Error:" + e.getMessage());
            System.err.println(e.getStackTrace());
            errorMessge = "Invalid Data Error:" + e.getMessage();
            DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, errorMessge);
        } catch (Exception e) {
            System.err.println("Got exception:" + e.getMessage());
            System.err.println(e.getStackTrace());
            errorMessge = "General Error:" + e.getMessage();
            DefaultRoutine defaultRoutine = new DefaultRoutine(request, response,errorMessge);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
}


