package org.todo.auxiliary;

import org.todo.TodoUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DefaultRoutine {
    public DefaultRoutine(HttpServletRequest request, HttpServletResponse response, String errorMessage){
        try {
            System.out.println("DefaultRoutine: " + errorMessage);
            request.setAttribute("errorMsg", errorMessage);
            request.getRequestDispatcher("/oopserror.jsp").forward(request, response);
        }
        catch(Exception e){
            System.err.println("Exception handling DefaultRoutine Error messag: " + errorMessage);
        }
    }
}
