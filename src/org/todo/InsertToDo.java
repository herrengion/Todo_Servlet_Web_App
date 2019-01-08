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
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;


@WebServlet("/InsertToDo.do")
public class InsertToDo extends HttpServlet {
    public void doPost (HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String title = request.getParameter("title");
        String dueDate = request.getParameter("dueDate");
        String important = request.getParameter("important");
        JAXBContext jc = null;
        boolean importantFlag = false;
        TodoList todosObj = null;
        String errorMessage = "";

        if(errorMessage == "") {
            File file = new File("C:\\Users\\Nath\\Documents\\BueroNath\\Ausbildung\\CAS SD\\WebApp\\Uebungen\\ToDo.xml");

            try{
                jc = JAXBContext.newInstance(TodoList.class);
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

            DueDate dueDateObj = new DueDate(null);
            try{
                dueDateObj.setDateByString(dueDate);
            }
            catch (ParseException e)
            {
                System.out.println("Unable to convert string to date -> Check input format and load form with received inputs!");
            }
            TodoList.Todo newToDoObj = new TodoList.Todo();
            int size = todosObj.getTodo().size();
            TodoList.Todo lastToDo = todosObj.getTodo().get(size-1);
            Long newId = lastToDo.getId();
            newToDoObj.setId(newId+1);
            newToDoObj.setTitle(title);
            newToDoObj.setDueDate(dueDateObj.getXmlGregorianCalendar());
            if(important == "on")
            {
                importantFlag=true;
            }
            newToDoObj.setImportant(importantFlag);
            todosObj.getTodo().add(newToDoObj);

            try{
                Marshaller marshaller = jc.createMarshaller();
                SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                Schema schema = schemaFactory.newSchema(new File("C:\\Users\\Nath\\Documents\\BueroNath\\Ausbildung\\CAS SD\\WebApp\\Uebungen\\ToDo.xsd"));
                marshaller.setSchema(schema);
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                marshaller.marshal(todosObj, file);
            }
            catch(JAXBException | SAXException e)
            {
                e.printStackTrace();
                errorMessage = errorMessage + " & ToDo list is not possible to write!";
            }
            request.getRequestDispatcher("ShowAllToDo.do").forward(request, response);
        }
    }
}
