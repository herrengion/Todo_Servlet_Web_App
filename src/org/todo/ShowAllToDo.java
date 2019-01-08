package org.todo;

import data.TodoList;
import org.xml.sax.SAXException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@WebServlet("/ShowAllToDo.do")
public class ShowAllToDo extends HttpServlet {
    public void doGet (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/ShowAllToDo.jsp").forward(request, response);
    }

    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String name = request.getParameter("name");
        String pwd = request.getParameter("pwd");
        String errorMessage = "";
        TodoList todosObj = new TodoList();

        if (name == null || name.isEmpty())
        {
            errorMessage = errorMessage + "Name is missing!";
            name = "";
        }
        if (pwd == null || pwd.isEmpty())
        {
            errorMessage = errorMessage + " & Passwortd is missing!";
            pwd = "";
        }

        if(errorMessage == "") {
            File file = new File("C:\\Users\\Nath\\Documents\\BueroNath\\Ausbildung\\CAS SD\\WebApp\\Uebungen\\ToDo.xml");
            try{
                JAXBContext jc = JAXBContext.newInstance(TodoList.class);
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(new File("C:\\Users\\Nath\\Documents\\BueroNath\\Ausbildung\\CAS SD\\WebApp\\Uebungen\\ToDo.xsd"));
                unmarshaller.setSchema(schema);
                todosObj = (TodoList) unmarshaller.unmarshal( file );
            }
            catch(JAXBException | SAXException e)
            {
                e.printStackTrace();
                errorMessage = errorMessage + " & ToDo list is not possible to read!";
            }

            List<TodoList.Todo> todoList = todosObj.getTodo();
            List<ArrayList<String>> todoListArr = new LinkedList<ArrayList<String>>();
            for (TodoList.Todo todo : todoList) {
                ArrayList<String> arrayListTodo = new ArrayList<String>();
                arrayListTodo.add(Long.toString(todo.getId()));
                XMLGregorianCalendar xmlGregorianCalendar = todo.getDueDate();
                DueDate dueDate = new DueDate(xmlGregorianCalendar);
                //GregorianCalendar gCalendar = todo.getDueDate().toGregorianCalendar();
                System.out.println("Date is: "+ dueDate.toString());
                arrayListTodo.add(dueDate.toString());
                arrayListTodo.add(todo.getTitle());
                arrayListTodo.add(todo.getCategory());
                todoListArr.add(arrayListTodo);
                System.out.println(((LinkedList<ArrayList<String>>) todoListArr).getLast());
            }
            request.setAttribute("todoListArr", todoListArr);
            request.setAttribute("name", name);
            request.setAttribute("errorMessage", errorMessage);
            request.getRequestDispatcher("/ShowAllToDo.jsp").forward(request, response);
        }
        else
        {
            request.getRequestDispatcher("/LoginNath.jsp").forward(request, response);
        }
    }
}
