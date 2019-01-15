package org.todo;

import org.todo.auxiliary.*;
import users.UserList;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Enumeration;
import java.util.LinkedList;

@WebServlet("/restAPI.do")
public class RestServlet extends HttpServlet{



    //------------------------------------------------------------------------------------------------------------------
    //HTTP Methods:
    //------------------------------------------------------------------------------------------------------------------

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------

    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }
    //------------------------------------------------------------------------------------------------------------------
}
