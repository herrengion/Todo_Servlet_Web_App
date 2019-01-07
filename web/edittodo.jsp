<%--
  Created by IntelliJ IDEA.
  User: Gion
  Date: 11/27/2018
  Time: 2:09 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Edit Todo Item</title>
</head>
<body>
<h1>Update Todo '${todoTitle}'</h1>
<%--<h2>hello ${name}</h2>--%>
<form action="todoFSM.do" method="post">
    Todo Title: <input type="text" name="newTodo" value="${todoTitle}"/><br/>
    Due Date: <input type= "date" name="dueDate" value="${todoDate}"/><br/>
    <input type="radio" name="priority" value="normal" ${normalPriority}> Normal Priority<br/>
    <input type="radio" name="priority" value="high" ${highPriority}> High Priority<br/>
    Category: <input type="text"  name="category" value="${category}"/><br/>
    <br/><br/>
    <input type="submit" value="Apply changes"/>
    <input type="hidden" name="todoId" value="${todoId}">
    <input type="hidden" name="redirect" value="fromUpdateTodo"/>
</form>
</body>
</html>
