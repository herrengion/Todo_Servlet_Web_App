<%--
  Created by IntelliJ IDEA.
  User: Gion
  Date: 11/27/2018
  Time: 2:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>Main List - Todo Application</title>
</head>
<body>
<style>
    table {
        font-family: arial, sans-serif;
        border-collapse: collapse;
        width: 100%;
    }

    td, th {
        border: 1px solid #dddddd;
        text-align: center;
        padding: 8px;
    }

    tr:nth-child(even) {
        background-color: #dddddd;
    }
</style>
<h1>Main Site Todo App</h1><br>
<h2>User: ${name} Password: ${pw}</h2><br>
<h2>${loginMessage}</h2>

    <table style="width:100%">
        <tr>
            <th>Todo ID</th>
            <th>Todo Title</th>
            <th>Due Date</th>
            <th>Priority</th>
            <th>Category</th>
            <th>Todo Status</th>
            <th>Action</th>
        </tr>
        <c:forEach items="${todoList}" var="todoInstance" >
        <tr>
            <td>${todoInstance.id}</td>
            <td>${todoInstance.title}</td>
            <td>${todoInstance.dueDate}</td>
            <td>${todoInstance.priority}</td>
            <td>${todoInstance.category}</td>
            <td>${todoInstance.status}<br>
                <form action="todoFSM.do" method="post">
                    <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                    <input type="submit" value="Change Status"/></form></td>
            <td>
                <form action="todoFSM.do" method="post">
                <input type="hidden" name="redirect" value="toUpdateTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <input type="submit" value="Edit Todo"/></form>

                <form action="todoFSM.do" method="post">
                <input type="hidden" name="redirect" value="discardTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <input type="submit" value="Discard Todo"/></form>
            </td>
        </tr>
        </c:forEach>
    </table>
<br/>
<a href="newtodo.jsp">new Todo<a/><br>
<a href="login.jsp">log out</a><br>

</body>
</html>
