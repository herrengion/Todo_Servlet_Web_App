package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DiscardTodo {
    public DiscardTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        String discardTodoId = request.getParameter("todoID");
        int discardTodoIdIntZeroed = Integer.parseInt(discardTodoId) - 1;
        String discardedTodoTitle = activeUser.getUserTodoList().get(discardTodoIdIntZeroed).getTitle();
        activeUser.getUserTodoList().remove(discardTodoIdIntZeroed);
        for (int i = activeUser.getUserTodoList().size() - 1; i >= 0; i--) {
            activeUser.getUserTodoList().get(i).setId(i + 1);
        }
        request.setAttribute("loginMessage", "Todo Item: '" + discardedTodoTitle + "' discarded!");
        request.setAttribute("todoList", activeUser.getUserTodoList());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
