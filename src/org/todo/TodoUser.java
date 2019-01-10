package org.todo;

import java.time.LocalDate;
import java.util.LinkedList;

public class TodoUser {

    private String userName;
    private String passWord;
    //Make sure displayed List starts off at '1', not '0'!
    private LinkedList<TodoEntry> userTodoList = new LinkedList();

    //Constructor
    public TodoUser(){

    }
    public TodoUser(String userName, String passWord){
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

    public LinkedList<TodoEntry> getUserTodoList()
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
    public void addTodo(TodoEntry newTodo){
        this.userTodoList.add(newTodo);
    }
    public void setUserTodoList(LinkedList<TodoEntry> userTodoList)
    {
        this.userTodoList = userTodoList;
    }

    //Generic Get Method for User Todos
    public TodoEntry getTodo(int todoId){
        return userTodoList.get(todoId);
    }

    public void deleteTodoEntry(int todoId){
        userTodoList.remove(todoId);
        for(int i=0;i<userTodoList.size();i++){
            userTodoList.get(i).setId(i);
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
    public void updateTodoDueDate(int todoId, LocalDate newDueDate)
    {
        userTodoList.get(todoId).setDueDate(newDueDate);
    }
    public void updateTodoImportant(int todoId, boolean newStatus)
    {
        userTodoList.get(todoId).setImportant(newStatus);
    }
}
