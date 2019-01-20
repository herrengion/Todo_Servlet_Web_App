package org.todo;
import com.sun.media.sound.InvalidDataException;
import org.xml.sax.SAXException;
import com.sun.xml.internal.ws.server.ServerRtException;
import data.ObjectFactory;
import data.TodoList;


import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import java.io.File;
import java.text.ParseException;
import java.util.*;

public class TodoUser {

    private String userName;
    private String passWord;
    private File userTodoXmlFile;
    private File xmlSchemaTodoFile;
    private HttpSession userSession;
    //Make sure displayed List starts off at '1', not '0'!
    private ObjectFactory todoObjectFactory = new ObjectFactory();

    private TodoList todosObj = todoObjectFactory.createTodoList();
    private LinkedList<TodoList.Todo> userTodoList = new LinkedList();

    //List to sort after attribute (maybe better as ArrayList?)
    private LinkedList<TodoList.Todo> sortedUserTodoList = new LinkedList();

    private Set categorySet = new HashSet();

    private LinkedList<Boolean> overdue = new LinkedList<>();


    //Constructor
    public TodoUser()
    {
    }
    public TodoUser(ServletContext context, HttpServletRequest request)
    {
        this.userName=request.getParameter("name");
        this.passWord=request.getParameter("pw");
        initializeUserSession(request);
    }
    public TodoUser(String userName, String passWord)
    {
        this.userName=userName;
        this.passWord=passWord;
    }

    // Methodes
    public void initializeUserSession(HttpServletRequest request){
        //Lock active user in HttpSession attributes
        userSession = request.getSession();
        userSession.setAttribute("name", userName);
        userSession.setAttribute("pw", passWord);
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

    public HttpSession getUserSession()
    {
        return userSession;
    }

    public LinkedList<TodoList.Todo> getUserTodoList()
    {
        userTodoList.clear();
        overdue.clear();
        for(int i = 0; i<todosObj.getTodo().size(); i++)
        {
            userTodoList.add(todosObj.getTodo().get(i));
            overdue.add(userTodoList.get(i).isOverdue());
        }
        if(!(userTodoList.isEmpty())) {
            this.SelectionSortListAfterDueDate(userTodoList);
        }
        return userTodoList;
    }
    public Long getHighestTodoId()
    {
        Long newId = Long.valueOf(0);

        int size = getUserTodoList().size();
        if(size > 0)
        {
            long maxId = 0;
            for(int i = 0; i<getUserTodoList().size(); i++){
                if(getUserTodoList().get(i).getId()>=maxId){
                    maxId=getUserTodoList().get(i).getId();
                }
            }
            newId=maxId;
        }
        return newId;
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
    public void setUserTodoList(File userToDoFile, File schemaFile)
    {
        if( userToDoFile.isFile() && schemaFile.isFile()) {
            this.userTodoXmlFile = userToDoFile;
            this.xmlSchemaTodoFile = schemaFile;
            convertTodoXmlToLinkedList();
        }
        else
        {
            throw new ServerRtException("Invalid file with the user data of the todos.");
        }
    }
    public TodoList.Todo getTodo(Long todoId) throws InvalidDataException {
        userTodoList  = getUserTodoList();
        Iterator<TodoList.Todo> iterator = userTodoList.iterator();
        while (iterator.hasNext()) {
            TodoList.Todo thisTodo = iterator.next();
            if(thisTodo.getId().equals(todoId))
            {
                return thisTodo;
            }
        }
        //Throw exception with message: Invalid todo index
        throw new InvalidDataException("Invalid todo ID please reload the page");
    }

    public void addTodo(TodoList.Todo newTodo)
    {
        this.todosObj.getTodo().add(newTodo);
    }
    public boolean deleteTodoEntry(Long todoId)
    {
        for (final ListIterator<TodoList.Todo> iterator = this.todosObj.getTodo().listIterator(); iterator.hasNext();) {
            final TodoList.Todo thisTodo = iterator.next();
            System.out.println("Delete todo ID:"+thisTodo.getId());

            if(thisTodo.getId().equals(todoId))
            {
                iterator.remove();
                return true;
            }

        }
        return false;
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
        {
            e.printStackTrace();
            throw new ServerRtException("User todo list is not possible to extract the data: "+e.getMessage());
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
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
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
    public void updateTodoTitle(Long todoId, String newTitle) throws InvalidDataException {
        getTodo(todoId).setTitle(newTitle);
    }
    public void updateTodoCategory(Long todoId, String newCategory) throws InvalidDataException {
        getTodo(todoId).setCategory(newCategory);
    }
    public void updateTodoCompleted(Long todoId, boolean newStatus)
    {
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
    public void updateTodoDueDate(Long todoId, String dueDate) throws ParseException, InvalidDataException {
        DueDate dueDateObj = new DueDate(dueDate);
       getTodo(todoId).setDueDate(dueDateObj.getXmlGregorianCalendar());
    }
    public void updateTodoImportant(Long todoId, boolean newStatus) throws InvalidDataException {
        getTodo(todoId).setImportant(newStatus);
    }

    public void updateCategoryHashSet(LinkedList<TodoList.Todo> activeTodoList){
        categorySet.clear();
        for(int i = 0; i<activeTodoList.size(); i++){
            categorySet.add(activeTodoList.get(i).getCategory());
        }
    }

    public Set getCategorySet(){
        updateCategoryHashSet(getUserTodoList());
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

    public void SelectionSortListAfterDueDate(LinkedList<TodoList.Todo> inputList){

        LinkedList<TodoList.Todo> intermediateList = new LinkedList<>();
        LinkedList<TodoList.Todo> intermediateListCompleted = new LinkedList<>();
        for(int i=0; i<inputList.size(); i++){
            if(inputList.get(i).getDueDate() == null){
                intermediateList.add(inputList.get(i));
                inputList.remove(i);
                i--;
            }
        }
        int minIndex;
        for(int i = 0; i < inputList.size()-1; i++) {
            minIndex=i;
            for (int j = i+1; j < inputList.size(); j++) {
                if(inputList.get(j).getDueDate().toGregorianCalendar().compareTo(inputList.get(minIndex).getDueDate().toGregorianCalendar())<0){
                    minIndex=j;
                }
            }
            inputList.add(i,inputList.get(minIndex));
            inputList.remove(minIndex+1);

        }
        inputList.addAll(intermediateList);
        for(int i = 0; i<inputList.size()-1; i++){
            if(inputList.get(i).isCompleted()){
                intermediateListCompleted.add(inputList.get(i));
                inputList.remove(i);
                i--;
            }
        }
        inputList.addAll(intermediateListCompleted);
    }

    public LinkedList<Boolean> getOverdue(){
        return overdue;
    }



}
