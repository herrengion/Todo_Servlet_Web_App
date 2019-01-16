package org.todo.auxiliary;

import com.sun.media.sound.InvalidDataException;
import org.todo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ToUpdateTodo {
    private TodoUser todoUser;

    public ToUpdateTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser) throws InvalidDataException {
        this.todoUser = activeUser;
        String updateTodoId = request.getParameter("todoID");
        Long updateTodoIdLong = Long.parseLong(updateTodoId);
        request.setAttribute("todoTitle", todoUser.getTodo(updateTodoIdLong).getTitle());
        request.setAttribute("todoDate", todoUser.getTodo(updateTodoIdLong).getDueDate());
        if (todoUser.getTodo(updateTodoIdLong).isImportant()) {
            request.setAttribute("highPriority", "checked");
        } else {
            request.setAttribute("normalPriority", "checked");
        }
        request.setAttribute("category", todoUser.getTodo(updateTodoIdLong).getCategory());
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
