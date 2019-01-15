package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TodoCompletedToggle {
    private TodoUser todoUser;

    public TodoCompletedToggle(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        this.todoUser = activeUser;

        String todoIdString = request.getParameter("todoID");
        String todoStatusString = request.getParameter("todoStatus");
        Long todoId = Long.parseLong(todoIdString);
        /* Status index is false = 0 and true if it is 1 ()*/
        boolean todoStatusBoolean = todoStatusString.equals("false") ? true : false;


        todoUser.updateTodoCompleted(todoId, todoStatusBoolean);
        todoUser.updateTodo();
        request.setAttribute("redirect", "showTodos");
        request.setAttribute("loginMessage", "Todo Item: '" + todoUser.getTodo(todoId).getTitle() + "' status changed!");
        //request.setAttribute("todoList", activeUser.getUserTodoList());
        request.setAttribute("todoList", todoUser.getUserTodoList());
        todoUser.updateCategoryHashSet(todoUser.getUserTodoList());
        request.setAttribute("todoUserCategorySet", todoUser.getCategorySet());

        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
