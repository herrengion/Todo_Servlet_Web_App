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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8">
    <title>Login Todo</title>
    <link href="Todoforyou.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<body>
<%--________________HEADER_________________________--%>
<div>
    <div class="w3-row w3-black">
        <div class="w3-col w3-container m12 w3-black w3-padding w3-text-white">
            <h1><b>Todo for you</b></h1>
        </div>
    </div>
</div>

<%--________________BODY_________________________--%>

<div class="w3-row w3-container w3-padding"></div>
<div class="w3-row w3-hide-small">
    <div class="w3-col w3-container m4 l4 ">
    </div>
    <div class="w3-col w3-container m4 l3 w3-center">
        <img src="images/adventure.jpg" class="w3-opacity w3-circle w3-image" style='width:100%;max-width:736px;' alt="Checklist">
    </div>
    <div class="w3-col w3-container m4 l5 "></div>
</div>

<div class="w3-row w3-padding"></div>

<form class="w3-container" action="todoFSM.do" method="post">
    <input type="hidden" name="redirect" value="login"/>
    <div class="w3-row">
        <div class="w3-col w3-container m4 l4"></div>
        <div class="w3-col w3-container m4 l3">
            <label><b>Username</b></label>
            <input class="w3-input w3-border w3-margin-bottom" type="text" placeholder="Enter Username" name="name" required>
            <label><b>Password</b></label>
            <input class="w3-input w3-border" type="password" placeholder="Enter Password" name="pw" required>
            <button class="w3-btn w3-block w3-green w3-section w3-padding" type="submit">Login</button>
            <%--<input class="w3-check w3-margin-top w3-margin-right" type="checkbox" checked="checked">Remember me
            <a href="forgotpassword.html" class="w3-margin-top w3-padding w3-hide-medium w3-hide-large w3-right" style='text-decoration: none'>
            Forgot password?</a>--%>
        </div>
        <div class="w3-col w3-container m4 l5"></div>
    </div>
</form>

<%--________________FOOTER_________________________--%>
<div class="w3-row w3-padding-64 w3-hide-small"></div>
<div class="w3-bottom w3-black">
    <footer class="w3-col w3-container m12 w3-black w3-padding">
        <div class="w3-right w3-text-white w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
    </footer>
</div>
</body>
</html>
