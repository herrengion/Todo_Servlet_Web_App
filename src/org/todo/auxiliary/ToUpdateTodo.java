package org.todo.auxiliary;

import org.todo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToUpdateTodo {

    public ToUpdateTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        String updateTodoId = request.getParameter("todoID");
        int updateTodoIdIntZeroed = Integer.parseInt(updateTodoId) - 1;
        request.setAttribute("todoTitle", activeUser.getUserTodoList().get(updateTodoIdIntZeroed).getTitle());
        request.setAttribute("todoDate", activeUser.getUserTodoList().get(updateTodoIdIntZeroed).getDueDate());
        if (activeUser.getUserTodoList().get(updateTodoIdIntZeroed).isImportant()) {
            request.setAttribute("highPriority", "checked");
        } else {
            request.setAttribute("normalPriority", "checked");
        }
        request.setAttribute("category", activeUser.getUserTodoList().get(updateTodoIdIntZeroed).getCategory());
        request.setAttribute("todoId", updateTodoId);
        //request.getRequestDispatcher("/edittodo.jsp").forward(request, response);
        try {
            request.getRequestDispatcher("/edittodo.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
