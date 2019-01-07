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
    <title>Create New Todo Item</title>
</head>
<body>
<h1>Create New Todo</h1>
<%--<h2>hello ${name}</h2>--%>
<form action="todoFSM.do" method="post">
    New Todo: <input type="text" name="newTodo"/><br/>
    Due Date: <input type= "date" name="dueDate"/><br/>
    <input type="radio" name="priority" value="normal" checked> Normal Priority<br/>
    <input type="radio" name="priority" value="high"> High Priority<br/>
    Category: <input type="text"  name="category"/><br/>
    <br/><br/>
    <input type="submit" value="add Todo"/>
    <input type="hidden" name="redirect" value="newTodo"/>
</form>
</body>
</html>
