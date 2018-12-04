package org.todo;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
import javax.servlet.http.*;

@WebServlet("/hello.do")
public class HelloServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        if (name == null || name .isEmpty()) name = "Servlet";
        String greeting = "Hello " + name + "!";
        request.setAttribute("greeting", greeting);
        request.getRequestDispatcher("/hello.jsp").forward(request, response);
        /*response.setContentType("text/html");
        try (PrintWriter out = response.getWriter()) {
            out.println("<html><body><h1>" + greeting + "</h1></body></html>");
            out.println("<br>");
            out.print("<form action=\"/action_page.php\">\n" +
                   "  Enter name:<br>\n" +
                   "  <input type=\"text\" name=\"firstname\" value=\"Mickey\"><br>\n" +
                   "  Enter password:<br>\n" +
                   "  <input type=\"text\" name=\"lastname\" value=\"Mouse\"><br><br>\n" +
                   "  <input type=\"submit\" value=\"Submit\">\n" +
                   "</form>");
        }*/
    }
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException{
        
    }
}