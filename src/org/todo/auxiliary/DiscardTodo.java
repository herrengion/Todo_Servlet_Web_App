package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DiscardTodo {
    private TodoUser todoUser;

    public DiscardTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        this.todoUser = activeUser;
        String discardTodoId = request.getParameter("todoID");
        Long discardTodoIdInt = Long.parseLong(discardTodoId);
        String discardedTodoTitle = todoUser.getTodo(discardTodoIdInt).getTitle();
        todoUser.deleteTodoEntry(discardTodoIdInt);
        todoUser.updateTodo();
        request.setAttribute("loginMessage", "Todo Item: '" + discardedTodoTitle + "' discarded!");
        request.setAttribute("todoList", todoUser.getUserTodoList());
        todoUser.updateCategoryHashSet(todoUser.getUserTodoList());
        request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
