package org.todo;

import data.TodoList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.*;

public class TodoUser {

    private String userName;
    private String passWord;
    private File userTodoXmlFile;
    private File xmlSchemaTodoFile;
    //Make sure displayed List starts off at '1', not '0'!
    private TodoList todosObj = new TodoList();
    private LinkedList<TodoList.Todo> userTodoList = new LinkedList();

    //List to sort after attribute (maybe better as ArrayList?)
    private LinkedList<TodoEntry> sortedUserTodoList = new LinkedList();

    private Set categorySet = new HashSet();


    //Constructor
    public TodoUser()
    {

    }
    public TodoUser(String userName, String passWord)
    {
        this.userName=userName;
        this.passWord=passWord;
    }

    //Get Methods

    public String getUserName()
    {
        return userName;
    }

    public String getPassWord()
    {
        return passWord;
    }

    public LinkedList<TodoList.Todo> getUserTodoList()
    {
        return userTodoList;
    }

    //Set Methods

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }
    public void addTodo(TodoList.Todo newTodo)
    {
        this.userTodoList.add(newTodo);
    }
    public void setUserTodoList(File userToDoFile, File schemaFile)
    {
        this.userTodoXmlFile = userToDoFile;
        this.xmlSchemaTodoFile =  schemaFile;
        convertTodoXmlToLinkedList();
    }

    //Generic Get Method for User Todos
    public TodoList.Todo getTodo(int todoId)
    {
        return userTodoList.get(todoId);
    }

    public void deleteTodoEntry(int todoId)
    {
        userTodoList.remove(todoId);
        for(int i=0;i<userTodoList.size();i++){
            userTodoList.get(i).setId((long) i);
        }
    }
    private void convertTodoXmlToLinkedList()
    {
        try{
            JAXBContext jc = JAXBContext.newInstance(TodoList.class);
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xmlSchemaTodoFile);
            unmarshaller.setSchema(schema);
            todosObj = (TodoList) unmarshaller.unmarshal( userTodoXmlFile );
            for(int i = 0; i<todosObj.getTodo().size(); i++) {
                userTodoList.add(todosObj.getTodo().get(i));
            }
            System.out.println(todosObj.getTodo().get(0).getTitle());
        }
        catch(JAXBException | SAXException e)
        //catch(JAXBException e)
        {
            e.printStackTrace();
            System.err.println(" ToDo list is not possible to read!");
        }

    }

    public void updateTodo()
    {
        JAXBContext jc = null;
        System.out.println("update ToDo XML");
        try{
            Marshaller marshaller = jc.createMarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xmlSchemaTodoFile);
            marshaller.setSchema(schema);
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.marshal(todosObj, userTodoXmlFile);
        }
        catch(JAXBException | SAXException e)
        {
            e.printStackTrace();
            System.err.println(" & ToDo list is not possible to write!");
        }
    }

    //Specific Update Methods for User Todos
    public void updateTodoTitle(int todoId, String newTitle)
    {
        userTodoList.get(todoId).setTitle(newTitle);
    }
    public void updateTodoCategory(int todoId, String newCategory)
    {
        userTodoList.get(todoId).setCategory(newCategory);
    }
    public void updateTodoCompleted(int todoId, boolean newStatus)
    {
        userTodoList.get(todoId).setCompleted(newStatus);
    }
    public void updateTodoDueDate(int todoId, String dueDate)
    {
        DueDate dueDateObj = new DueDate(null);
        try{
            dueDateObj.setDateByString(dueDate);
        }
        catch (ParseException e)
        {
            System.out.println("Unable to convert string to date -> Check input format and load form with received inputs!");
        }
        userTodoList.get(todoId).setDueDate(dueDateObj.getXmlGregorianCalendar());
    }
    public void updateTodoImportant(int todoId, boolean newStatus)
    {
        userTodoList.get(todoId).setImportant(newStatus);
    }

    public void updateCategoryHashSet(LinkedList<TodoList.Todo> activeTodoList){
        categorySet.clear();
        for(int i = 0; i<userTodoList.size(); i++){
            categorySet.add(userTodoList.get(i).getCategory());
        }
    }

    public Set getCategorySet(){
        return categorySet;
    }

    public LinkedList<TodoEntry> getSortedUserTodoList() {
        return sortedUserTodoList;
    }
}
