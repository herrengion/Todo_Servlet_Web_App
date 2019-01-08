<%--
  Created by IntelliJ IDEA.
  User: Nath
  Date: 27.11.2018
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>ToDo</title>
</head>
<body>
    <h2>ToDo for ${name}</h2>
    <p> There the list of todo shall appeare</p>
    <ul>
        <c:forEach items="${todoListArr}" var="todoItems">
            <li>${todoItems[0]}, ${todoItems[1]}, ${todoItems[2]}, ${todoItems[3]}</li>
        </c:forEach>
    </ul>
    <form action="AddToDo.do" method="get">
        <button name="newTodo" type="submit" value="newToDo">Add ToDo</button>
    </form>
</body>
</html>
