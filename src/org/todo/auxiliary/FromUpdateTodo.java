package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FromUpdateTodo {

    public FromUpdateTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        String updatedTodoId = request.getParameter("todoId");
        int updatedTodoIdIntZeroed = Integer.parseInt(updatedTodoId) - 1;

        String updatedTodoTitle = request.getParameter("newTodo");

        String updatedDueDateString = request.getParameter("dueDate");
        DateTimeFormatter frm = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        XMLGregorianCalendar xmlGregorianCalendar = activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).getDueDate();
        String updatedCategoryString = request.getParameter("category");
        String updatedHighPriority = request.getParameter("priority");
        activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).setTitle(updatedTodoTitle);
        activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).setDueDate(xmlGregorianCalendar);
        activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).setCategory(updatedCategoryString);
        if (updatedHighPriority.equals("high")) {
            activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).setImportant(true);
        } else {
            activeUser.getUserTodoList().get(updatedTodoIdIntZeroed).setImportant(false);
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
