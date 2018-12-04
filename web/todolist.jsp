<%--
  Created by IntelliJ IDEA.
  User: Gion
  Date: 11/27/2018
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main List - Todo Application</title>
</head>
<body>
<h1>Main Site Todo App</h1><br>
<h2>User: ${name} Password: ${pw}</h2><br>
<h2>${loginMessage}</h2>
<h3>Todo ${id} : ${todoContent}</h3>
<a href="newtodo.jsp?name=${name}">new Todo<a/><br>
<a href="edittodo.jsp">edit Todo</a><br>
<a href="login.html">log out</a><br>

</body>
</html>
