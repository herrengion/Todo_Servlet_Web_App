<%--
  Created by IntelliJ IDEA.
  User: Gion
  Date: 12/3/2018
  Time: 4:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Todo Application - login</title>
</head>
<body>
<form action="todoFSM.do" method="post">
    <input type="hidden" name="redirect" value="login"/>
    Name: <input type="text" name="name">
    <br/><br/>
    Password: <input type="text" name="pw">
    <br/><br/>
    <br/><br/>
    <input type="submit" value="login/sign up">
</form>
</body>
</html>
