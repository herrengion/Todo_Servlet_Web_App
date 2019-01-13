package org.todo;
import data.TodoList;
import org.todo.auxiliary.*;


//Done: create Classes for FSM functions, front controller paradigma (bis samstag)
//Todo: Sort after Category, sorted by default after due Date. Mark overdue Tasks in red, completed tasks in green. additional: implement sort after column
//Todo: REST interface, Data Persistence
//Todo: Exception handling, Http error pages
//Todo: Change UserList to LinkedList, possibility of deleting account
//Todo: Implement "forgot password" functionality

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;

@WebServlet("/todoFSM.do")
public class TodoServlet extends HttpServlet {

    //------------------------------------------------------------------------------------------------------------------
    //Fields:
    //------------------------------------------------------------------------------------------------------------------
    public static final String DATA_PATH_WEB_INF = "/WEB-INF";
    public static final String DATA_PATH_WEB_INF_DATA = DATA_PATH_WEB_INF+"/data";
    public static final String DATA_PATH_WEB_INF_USER_DATA = DATA_PATH_WEB_INF_DATA+"/UserData";
    //Active Session
    HttpSession userSession;

    //Active User
    /* TODO: Multiple users is not working and logged in user can be high chaked and inserting new todo causes troubles
    * when other page is reloaded. Adding Todos from other user to both xml files. Probably there are other problems when
     * with the class data and shall be initialized in method each time it will pass.*/
//    private
    private File xmlSchemaFile;
    private File userToDoXmlFile;
    //All User Data
    /* TODO: Need to be persisted and still available after reboot of server -> store in File or XML too*/
    private ArrayList<TodoUser> todoUserList = new ArrayList<>();

    //------------------------------------------------------------------------------------------------------------------
    //HTTP Methods:
    //------------------------------------------------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    //------------------------------------------------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        TodoUser activeUser = new TodoUser();

        String activeRedirectPath = request.getParameter("redirect");
        ServletContext context = getServletContext();
        String contextPath = context.getRealPath("/");

        Enumeration<String> contextAttributeNames = context.getAttributeNames();
        while(contextAttributeNames.hasMoreElements()){
            System.out.println("Context: "+contextAttributeNames.nextElement());
        }

        if(context.getAttribute("name") != null){
            activeUser.setUserName(context.getAttribute("name").toString());
        }
        if(!activeRedirectPath.equals("login"))
        {
            userToDoXmlFile = new File(contextPath +
                    DATA_PATH_WEB_INF_USER_DATA +
                    "/" + activeUser.getUserName() + "/ToDo_list_" + activeUser.getUserName()+".xml");
            xmlSchemaFile = new File(contextPath + DATA_PATH_WEB_INF_DATA + "/ToDo.xsd");
            activeUser.setUserTodoList(userToDoXmlFile, xmlSchemaFile);
        }

            switch (activeRedirectPath) {

                case "login":
                    LoginRoutine loginRoutine = new LoginRoutine(request, response, todoUserList, contextPath);
                    activeUser = loginRoutine.getActiveTodoUser();
                    userToDoXmlFile = new File(contextPath +
                            DATA_PATH_WEB_INF_USER_DATA +
                            "/" + activeUser.getUserName() + "/ToDo_list_" + activeUser.getUserName()+".xml");
                    xmlSchemaFile = new File(contextPath + DATA_PATH_WEB_INF_DATA + "/ToDo.xsd");
                    activeUser.setUserTodoList(userToDoXmlFile, xmlSchemaFile);
                    activeUser.updateCategoryHashSet(activeUser.getUserTodoList());
                    userSession = loginRoutine.getUserSession();
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
                    System.out.println("category sort routine entered. Value: "+categoryName);
                    activeUser.updateCategoryHashSet(activeUser.getUserTodoList());
                    activeUser.setSortedUserTodoList(categoryName);
                    LinkedList<TodoList.Todo> sortedUserTodoList = activeUser.getSortedUserTodoList();
                    request.setAttribute("todoList", sortedUserTodoList);
                    request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
                    request.getRequestDispatcher("/todolist.jsp").forward(request, response);
                    break;

                default:
                    DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, activeUser);
                    break;

            }
    }
    //------------------------------------------------------------------------------------------------------------------
}

