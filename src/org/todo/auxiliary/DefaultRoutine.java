package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultRoutine {
    public DefaultRoutine(HttpServletRequest request, HttpServletResponse response, TodoUser activeUser){
        try {
            request.getRequestDispatcher("/oopserror.html").forward(request, response);
        }
        catch(Exception e){
            System.out.println("Exception handling DefaultRoutine()");
        }
    }
}
