<%--
  Created by IntelliJ IDEA.
  User: Nath
  Date: 27.11.2018
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>ToDo</title>
</head>
<body>
    <h2>Login</h2>
    <form action="ShowAllToDo.do" method="post">
        <div class="container">
            <label for="name"><b>Username</b></label>
            <input type="text" placeholder="Your user name" name="name" required>

            <label for="psw"><b>Password</b></label>
            <input type="password" placeholder="Your password" name="pwd" required>
            <c:if test="${not empty errorMessage}">
                <p>ERROR: ${errorMessage}</p>
            </c:if>
            <button type="submit">Login</button>
            <label>
                <input type="checkbox" checked="checked" name="remember"> Remember me
            </label>
        </div>
    </form>
</body>
</html>
