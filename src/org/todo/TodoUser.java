package org.todo;

import data.TodoList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
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
    private LinkedList<TodoList.Todo> sortedUserTodoList = new LinkedList();

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
        userTodoList.clear();
        //userTodoList.add(todosObj.getTodo().get(0));
        for(int i = 0; i<todosObj.getTodo().size(); i++) {
            //int comparator = todosObj.getTodo().get(i-1).getDueDate(), todosObj.getTodo().get(i-1).getDueDate());
            userTodoList.add(todosObj.getTodo().get(i));
        }
        //LinkedList<TodoList.Todo> dueDateSortedTodolist = new LinkedList<>();
        //dueDateSortedTodolist.add(userTodoList.get(0));

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
        this.todosObj.getTodo().add(newTodo);
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
        userTodoList  = getUserTodoList();
        Iterator<TodoList.Todo> iterator = userTodoList.iterator();
        while (iterator.hasNext()) {
            TodoList.Todo thisTodo = iterator.next();
            System.out.println("This todo ID:"+thisTodo.getId());
            if(thisTodo.getId() == todoId)
            {
                return thisTodo;
            }
        }
        //Throw exception with message: Invalid todo index
        return null;
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

        System.out.println("update ToDo XML");
        try{
            JAXBContext jc = JAXBContext.newInstance(TodoList.class);
            Marshaller marshaller = jc.createMarshaller();
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = schemaFactory.newSchema(xmlSchemaTodoFile);
            marshaller.setSchema(schema);
            //marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            System.out.println("Store new todo object in file!"+userTodoXmlFile.getAbsolutePath());
            //OutputStream os = new FileOutputStream(userTodoXmlFile);
            marshaller.marshal(todosObj, userTodoXmlFile);
        }
        //catch(JAXBException | SAXException | FileNotFoundException e)
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
    public void updateTodoCompleted(Long todoId, boolean newStatus)
    {
//        this.todosObj.getTodo().iterator()
        for (final ListIterator<TodoList.Todo> iterator = this.todosObj.getTodo().listIterator(); iterator.hasNext();) {
            final TodoList.Todo thisTodo = iterator.next();
            System.out.println("This todo ID:"+thisTodo.getId());

            if(thisTodo.getId().equals(todoId))
            {
                thisTodo.setCompleted(newStatus);
                iterator.remove();
                iterator.add(thisTodo);
                break;
            }

        }
    }
    public void updateTodoDueDate(int todoId, String dueDate) throws ParseException {
        DueDate dueDateObj = new DueDate(null);
        dueDateObj.setDateByString(dueDate);
        userTodoList.get(todoId).setDueDate(dueDateObj.getXmlGregorianCalendar());
    }
    public void updateTodoImportant(int todoId, boolean newStatus)
    {
        userTodoList.get(todoId).setImportant(newStatus);
    }

    public void updateCategoryHashSet(LinkedList<TodoList.Todo> activeTodoList){
        categorySet.clear();
        for(int i = 0; i<activeTodoList.size(); i++){
            categorySet.add(activeTodoList.get(i).getCategory());
        }
    }

    public Set getCategorySet(){
        return categorySet;
    }

    public LinkedList<TodoList.Todo> getSortedUserTodoList() {
        return sortedUserTodoList;
    }

    public void setSortedUserTodoList(String category){
        sortedUserTodoList.clear();
        for(int i = 0; i<userTodoList.size(); i++){
            if(userTodoList.get(i).getCategory().equals(category)){
                sortedUserTodoList.add(userTodoList.get(i));
            }
        }
        if(category.equals("all")){
            sortedUserTodoList.clear();
            for(int i = 0; i<userTodoList.size(); i++){
                    sortedUserTodoList.add(userTodoList.get(i));
            }
        }
    }
}
