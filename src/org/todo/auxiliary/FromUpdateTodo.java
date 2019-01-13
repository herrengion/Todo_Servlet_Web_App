package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FromUpdateTodo {
    private TodoUser todoUser;

    public FromUpdateTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser) throws ParseException {
        this.todoUser = activeUser;
        String updatedTodoId = request.getParameter("todoId");
        Long updatedTodoIdLong = Long.parseLong(updatedTodoId);
        String updatedTodoTitle = request.getParameter("newTodo");
        String updatedDueDateString = request.getParameter("dueDate");
        String updatedCategoryString = request.getParameter("category");
        String updatedHighPriority = request.getParameter("priority");
        // Fix this as I did for the dueDate

        todoUser.updateTodoTitle(updatedTodoIdLong,updatedTodoTitle);
        todoUser.updateTodoDueDate(updatedTodoIdLong,updatedDueDateString);
        todoUser.updateTodoCategory(updatedTodoIdLong,updatedCategoryString);
        if (updatedHighPriority.equals("high")) {
           todoUser.updateTodoImportant(updatedTodoIdLong,true);
        } else {
            todoUser.updateTodoImportant(updatedTodoIdLong,false);
        }
        request.setAttribute("loginMessage", "Todo : '" + updatedTodoTitle + "' updated!");
        request.setAttribute("todoList", activeUser.getUserTodoList());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.err.println("Exception handling FromUpdateTodo()");
        }
    }
}
