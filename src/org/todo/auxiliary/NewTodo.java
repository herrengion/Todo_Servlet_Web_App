package org.todo.auxiliary;

import data.TodoList;
import org.todo.*;
//import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;


public class NewTodo {

    //Constructor
    public NewTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser)
    {
        Long newId = Long.valueOf(1);
        String newTodoString = request.getParameter("newTodo");
        String dueDateString = request.getParameter("dueDate");
        String categoryString = request.getParameter("category");
        String highPriority = request.getParameter("priority");

        DueDate dueDateObj = new DueDate(null);
        try{
            dueDateObj.setDateByString(dueDateString);
        }
        catch (ParseException e)
        {
            System.out.println("Unable to convert string to date -> Check input format and load form with received inputs!");
        }
        TodoList.Todo newToDoObj = new TodoList.Todo();
        int size = activeUser.getUserTodoList().size();
        if(size > 0)
        {
            TodoList.Todo lastToDo = activeUser.getUserTodoList().get(size - 1);
            newId = lastToDo.getId();
        }

        newToDoObj.setId(newId+1);
        newToDoObj.setTitle(newTodoString);
        newToDoObj.setCategory(categoryString);
        newToDoObj.setDueDate(dueDateObj.getXmlGregorianCalendar());
        newToDoObj.setImportant(highPriority.equals("high"));
        activeUser.getUserTodoList().add(newToDoObj);
        activeUser.updateTodo();
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
