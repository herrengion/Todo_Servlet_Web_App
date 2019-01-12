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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8">
    <title>Main list - Todo</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>
<%--________________HEADER_________________________--%>
<%--medium, large--%>
<div class="w3-row w3-black w3-hide-small">
    <div class="w3-col w3-container m6 w3-black w3-padding w3-text-white w3-hide-small">
        <h1><b>Todo for you</b></h1>
    </div>

    <div class="w3-col w3-container m6 w3-black w3-padding w3-text-white w3-hide-small">
        <div class="w3-dropdown-click w3-right">
            <button onclick="myFunction()" class="w3-button w3-black w3-border-black w3-hover-green w3-hide-small">
                <h3 class="w3-right">${name} <i class="fa fa-user-circle-o w3-xxxlarge w3-margin-left"></i></h3>
            </button>
            <div id="UserMenu" class="w3-dropdown-content w3-bar-block w3-border w3-hide-small">
                <a href="login.jsp" class="w3-bar-item w3-button w3-black w3-text-white w3-hover-red" style='text-decoration:none;'>
                    <i class="fa fa-sign-out w3-xlarge w3-margin-right"></i><b> log out</b>
                </a>
            </div>
        </div>
    </div>

    <script>
        function myFunction() {
            var x = document.getElementById("UserMenu");
            if (x.className.indexOf("w3-show") == -1) {
                x.className += " w3-show";
            } else {
                x.className = x.className.replace(" w3-show", "");
            }
        }
    </script>
</div>

<%--small--%>
<div class="w3-row w3-hide-medium w3-hide-large">
    <div class="w3-col w3-container m6 w3-black w3-padding w3-text-white w3-hide-medium w3-hide-large">
        <h1>
            <b>Todo for you</b>
            <a href="login.jsp"><i class="w3-btn fa fa-sign-out w3-xxxlarge w3-right w3-margin-left w3-hover-red"></i></a>
        </h1>
    </div>
    <div class="w3-col w3-container m6 w3-green w3-padding w3-text-black w3-hide-medium w3-hide-large">
        <h3><i class="fa fa-user-circle-o w3-xxlarge w3-margin-right"></i>${name} </h3>
    </div>
</div>

<%--________________BODY_________________________--%>
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
        <th>Todo ID<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="ID"/>
            <input type="submit" value="sort after todo ID"/></form></th>
        <th>Todo Title<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="title"/>
            <input type="submit" value="sort after title"/></form></th>
        <th>Due Date<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="dueDate"/>
            <input type="submit" value="sort after due Date"/></form></th>
        <th>Priority<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="priority"/>
            <input type="submit" value="sort after Priority"/></form></th>
        <th>Category<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="category"/>
            <input type="submit" value="sort after category"/></form></th>
        <th>Todo Status<form action="todoFSM.do" method="post">
            <input type="hidden" name="redirect" value="sortRoutine"/>
            <input type="hidden" name="sortAfter" value="status"/>
            <input type="submit" value="sort after status"/></form></th>
        <th>Action</th>
    </tr>
    <c:forEach items="${todoList}" var="todoInstance" >
        <tr>
            <td>${todoInstance.id}</td>
            <td>${todoInstance.title}</td>
            <td>${todoInstance.dueDate}</td>
            <td>${todoInstance.important}</td>
            <td>${todoInstance.category}</td>
            <td>${todoInstance.completed}<br>
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
<a href="newtodo.jsp">new Todo</a><br>
<br>
<select>
    <optgroup label="Select category">
        <option value="all">all</option>
        <c:forEach items="${todoUserCategorySet}" var="todoUserCategorySet" >
            <option value="${todoUserCategorySet}">${todoUserCategorySet}</option>
        </c:forEach>
    </optgroup>
</select>

<%--________________Footer_________________________--%>
<div class="w3-row w3-padding-64"></div>
<div class="w3-bottom w3-black">
    <footer class="w3-col w3-container m12 w3-black w3-padding">
        <div class="w3-right w3-text-white w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
    </footer>
</div>

</body>
</html>

