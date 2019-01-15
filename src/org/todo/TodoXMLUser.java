package org.todo;

public class TodoXMLUser {

    private String username;
    private String password;

    public TodoXMLUser(){}

    public TodoXMLUser(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
