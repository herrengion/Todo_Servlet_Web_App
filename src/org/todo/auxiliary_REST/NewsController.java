package org.todo.auxiliary_REST;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Scanner;

@WebServlet("/news")
public class NewsController extends HttpServlet {

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String news = (String) getServletContext().getAttribute("news");
		response.getWriter().println(news == null ? "No news available" : news);
	}

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String news = new Scanner(request.getInputStream()).nextLine();
		getServletContext().setAttribute("news", news);
	}
}
