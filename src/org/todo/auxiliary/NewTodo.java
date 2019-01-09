package org.todo.auxiliary;

import org.todo.*;
//import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class NewTodo {

    //Constructor
    public NewTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){

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
        activeUser.getUserTodoList().add(new TodoEntry(activeUser.getUserTodoList().size() + 1, newTodoString, categoryString, dueDate, highPriority.equals("high")));
        request.setAttribute("loginMessage", "Todo added!");
        request.setAttribute("todoList", activeUser.getUserTodoList());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
