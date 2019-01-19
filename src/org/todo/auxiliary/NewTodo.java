package org.todo.auxiliary;

import data.TodoList;
import org.todo.*;
//import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.LocalDate;


public class NewTodo {
    private TodoUser todoUser;

    private String convertStringToUtf8(String inputString)
    {
        byte[] bytes = inputString.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    //Constructor
    public NewTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser)
    {
        this.todoUser = activeUser;
        Long newId = Long.valueOf(0);
        String newTodoString = request.getParameter("newTodo");
        String dueDateString = request.getParameter("dueDate");
        String categoryString = request.getParameter("category");
        String highPriority = request.getParameter("priority");

        /*Convert input values to correct type*/
        newTodoString = convertStringToUtf8(newTodoString);
        categoryString = convertStringToUtf8(categoryString);

        DueDate dueDateObj = new DueDate(null);
        dueDateObj.setDateByString(dueDateString);
        TodoList.Todo newToDoObj = new TodoList.Todo();
        int size = todoUser.getUserTodoList().size();
        if(size > 0)
        {
            TodoList.Todo lastToDo = todoUser.getUserTodoList().get(size - 1);
            newId = lastToDo.getId();
            long maxId = 0;
            for(int i = 0; i<activeUser.getUserTodoList().size(); i++){
                if(activeUser.getUserTodoList().get(i).getId()>=maxId){
                    maxId=activeUser.getUserTodoList().get(i).getId();
                }
            }
            newId=maxId;
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
        request.setAttribute("todoUserCategorySet", todoUser.getCategorySet());
        request.setAttribute("todoList", todoUser.getUserTodoList());
        request.setAttribute("isOverdue", todoUser.getOverdue());
        request.setAttribute("name", todoUser.getUserName());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling newTodoRoutine()");
        }
    }
}
