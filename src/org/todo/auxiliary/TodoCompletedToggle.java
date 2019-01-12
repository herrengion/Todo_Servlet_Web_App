package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TodoCompletedToggle {
    public TodoCompletedToggle(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        String toggleTodoId = request.getParameter("todoID");
        int toggleTodoIdIntZeroed = Integer.parseInt(toggleTodoId) - 1;
        if (activeUser.getUserTodoList().get(toggleTodoIdIntZeroed).isCompleted()) {
            activeUser.getUserTodoList().get(toggleTodoIdIntZeroed).setCompleted(false);
        } else {
            activeUser.getUserTodoList().get(toggleTodoIdIntZeroed).setCompleted(true);
        }
        request.setAttribute("loginMessage", "Todo Item: '" + activeUser.getUserTodoList().get(toggleTodoIdIntZeroed).getTitle() + "' status changed!");
        //request.setAttribute("todoList", activeUser.getUserTodoList());
        request.setAttribute("todoList", activeUser.getSortedUserTodoList());
        request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());

        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
