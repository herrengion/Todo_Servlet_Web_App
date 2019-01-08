<%--
  Created by IntelliJ IDEA.
  User: Nath
  Date: 24.12.2018
  Time: 13:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ToDo</title>
</head>
<body>
    <h2>Add New ToDo</h2>
    <form action="InsertToDo.do" method="post">
        <div class="container">
            <label for="title"><b>Description</b></label>
            <input type="text" placeholder="ToDo description" name="title" required>

            <label for="dueDate"><b>Due Date</b></label>
            <input type="text" placeholder="YYYY-MM-DD" name="dueDate">
            <input type="checkbox" checked="checked" name="important"> Important
            <c:if test="${not empty errorMessage}">
                <p>ERROR: ${errorMessage}</p>
            </c:if>
            <button type="submit">Add</button>

        </div>
    </form>
</body>
</html>
