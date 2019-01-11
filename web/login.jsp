<%--
  Created by IntelliJ IDEA.
  User: Gion
  Date: 12/3/2018
  Time: 4:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8">
    <title>Login Todo Application</title>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>

<div>
    <header class="w3-col w3-container m12 w3-black">
        <h2 class="w3-left w3-xxlarge w3-text-white"><b>Todo for you</b></h2>
        <%-- <h2 class="w3-right w3-text-white w3-hide-small"><i class="fa fa-home w3-xxxlarge"></i></h2>--%>
    </header>
</div>

<body>
<div class="w3-row w3-padding-64"></div>
<div class="w3-center w3-padding">
    <h3>Hier kommt noch ein Icon</h3>
</div>
<div class="w3-row w3-padding-32"></div>
<%-- Page Gion
<form action="todoFSM.do" method="post">
    <input type="hidden" name="redirect" value="login"/>
    Name: <input type="text" name="name">
    <br/><br/>
    Password: <input type="password" name="pw">
    <br/><br/>
    <br/><br/>
    <input type="submit" value="login/sign up">
</form>
--%>
<form class="w3-container" action="todoFSM.do" method="post">
    <input type="hidden" name="redirect" value="login"/>
    <div class="w3-row">
        <div class="w3-col w3-container m4" l4></div>
        <div class="w3-col w3-container m4" l3>
            <label><b>Username</b></label>
            <input class="w3-input w3-border w3-margin-bottom form-control" type="text" placeholder="Enter Username" name="name" required>
            <label><b>Password</b></label>
            <input class="w3-input w3-border form-control" type="password" placeholder="Enter Password" name="pw" required>
            <button class="w3-button w3-block w3-green w3-section w3-padding" type="submit">Login</button>
            <input class="w3-check w3-margin-top" type="checkbox" checked="checked"> Remember me
        </div>
        <div class="w3-col w3-container m4 l5"></div>
    </div>
</form>


<div>
    <footer class="w3-col w3-container m12 w3-black w3-padding w3-bottom">
        <div class="w3-left w3-text-white w3-padding-24 w3-hide-small">Forgot password?</div>
        <div class="w3-right w3-text-white w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
    </footer>
</div>

</body>
</html>
