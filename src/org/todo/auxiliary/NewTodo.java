package org.todo.auxiliary;

import data.TodoList;
import org.todo.*;
//import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.time.LocalDate;


public class NewTodo {
    private TodoUser todoUser;

    //Constructor
    public NewTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser)
    {
        this.todoUser = activeUser;
        Long newId = Long.valueOf(0);
        String newTodoString = request.getParameter("newTodo");
        String dueDateString = request.getParameter("dueDate");
        String categoryString = request.getParameter("category");
        String highPriority = request.getParameter("priority");

        DueDate dueDateObj = new DueDate(null);
        dueDateObj.setDateByString(dueDateString);
        TodoList.Todo newToDoObj = new TodoList.Todo();
        int size = activeUser.getUserTodoList().size();
        if(size > 0)
        {
            TodoList.Todo lastToDo = todoUser.getUserTodoList().get(size - 1);
            newId = lastToDo.getId();
        }

        newToDoObj.setId(newId+1);
        newToDoObj.setTitle(newTodoString);
        newToDoObj.setCategory(categoryString);
        newToDoObj.setDueDate(dueDateObj.getTransformedDateStringToXMLGregorian(dueDateString));
        newToDoObj.setImportant(highPriority.equals("high"));
        todoUser.addTodo(newToDoObj);
        todoUser.updateTodo();
        request.setAttribute("loginMessage", "Todo added!");
        todoUser.updateCategoryHashSet(todoUser.getUserTodoList());
        request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
        request.setAttribute("todoList", todoUser.getUserTodoList());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
