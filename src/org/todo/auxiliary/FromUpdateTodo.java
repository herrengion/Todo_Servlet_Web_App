package org.todo.auxiliary;

import com.sun.media.sound.InvalidDataException;
import org.todo.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

public class FromUpdateTodo {
    private TodoUser todoUser;

    private String convertStringToUtf8(String inputString)
    {
        byte[] bytes = inputString.getBytes(StandardCharsets.ISO_8859_1);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public FromUpdateTodo(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser) throws ParseException, InvalidDataException {
        this.todoUser = activeUser;
        String updatedTodoId = request.getParameter("todoId");
        Long updatedTodoIdLong = Long.parseLong(updatedTodoId);
        String updatedTodoTitle = request.getParameter("newTodo");
        String updatedDueDateString = request.getParameter("dueDate");
        String updatedCategoryString = request.getParameter("category");
        String updatedHighPriority = request.getParameter("priority");
        //TODO: Fix this as I did for the dueDate
        updatedTodoTitle = convertStringToUtf8(updatedTodoTitle);
        updatedCategoryString = convertStringToUtf8(updatedCategoryString);

        todoUser.updateTodoTitle(updatedTodoIdLong,updatedTodoTitle);
        todoUser.updateTodoDueDate(updatedTodoIdLong,updatedDueDateString);
        todoUser.updateTodoCategory(updatedTodoIdLong,updatedCategoryString);
        if (updatedHighPriority.equals("high")) {
           todoUser.updateTodoImportant(updatedTodoIdLong,true);
        } else {
            todoUser.updateTodoImportant(updatedTodoIdLong,false);
        }
        todoUser.updateTodo();
        request.setAttribute("loginMessage", "Todo : '" + updatedTodoTitle + "' updated!");
        request.setAttribute("name", todoUser.getUserName());
        request.setAttribute("todoList", activeUser.getUserTodoList());
        todoUser.updateCategoryHashSet(todoUser.getUserTodoList());
        request.setAttribute("todoUserCategorySet", activeUser.getCategorySet());
        try {
            request.getRequestDispatcher("/todolist.jsp").forward(request, response);
        }
        catch(Exception e){
            System.err.println("Exception handling FromUpdateTodo()");
        }
    }
}
