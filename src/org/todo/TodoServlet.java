package org.todo;
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
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/todoFSM.do")
public class TodoServlet extends HttpServlet {

    //------------------------------------------------------------------------------------------------------------------
    //Fields:
    //------------------------------------------------------------------------------------------------------------------
    public static final String DATA_PATH_WEB_INF = "/WEB-INF";
    //Active Session
    HttpSession userSession;

    //Active User
    private TodoUser activeUser = new TodoUser();

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


        String activeRedirectPath = request.getParameter("redirect");


            switch (activeRedirectPath) {

                case "login":
                    ServletContext context = getServletContext();
                    String contextPath = context.getRealPath("/");
                    LoginRoutine loginRoutine = new LoginRoutine(request, response, todoUserList, contextPath);
                    activeUser = loginRoutine.getActiveTodoUser();
                    userSession = loginRoutine.getUserSession();
                    break;

                case "newTodo":
                    NewTodo newTodo = new NewTodo(request, response, activeUser);
                    break;

                case "toUpdateTodo":
                    ToUpdateTodo toUpdateTodo = new ToUpdateTodo(request, response, activeUser);
                    break;

                case "fromUpdateTodo":
                    FromUpdateTodo fromUpdateTodo = new FromUpdateTodo(request, response, activeUser);
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

                default:
                    DefaultRoutine defaultRoutine = new DefaultRoutine(request, response, activeUser);
                    break;

            }
    }
    //------------------------------------------------------------------------------------------------------------------
}

