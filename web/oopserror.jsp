<%--
  Created by IntelliJ IDEA.
  User: Nath
  Date: 17.01.2019
  Time: 18:53
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8">
    <title>Error</title>
    <link href="Todoforyou.css" rel="stylesheet" type="text/css" />
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
    <!--________________HEADER_________________________-->
    <div class="w3-row w3-container w3-black w3-padding w3-text-white">
        <h1><b>Todo for you</b></h1>
    </div>
    <div class="w3-row w3-red w3-padding"></div>

    <!--________________BODY_________________________-->
    <div class="w3-row w3-center w3-wide" style="font-size:1000%">:(</div>

    <div class="w3-row w3-center">
        <h2> An Error has been encountered</h2>
    </div>

    <div class="w3-row w3-padding"></div>

    <div class="w3-row">
        <div class="w3-center" >Somewhere, something went horribly wrong!</div>
    </div>

    <div class="w3-row w3-padding"></div>

    <div class="w3row">
        <div class="w3-center"> So sorry to let you down like this!</div>
    </div>

    <div class="w3-row w3-padding"></div>

    <div class="w3-container w3-center w3-padding">
        <a href="login.jsp" class="w3-btn w3-center w3-green">Back to Login</a>
    </div>

    <div class="w3row">
    <div class="w3-container w3-center w3-padding">
        <h6 class="w3-center"> Details:</h6>
        <div class="w3-center">${errorMsg}</div>
    </div>

    <!--________________FOOTER_________________________-->
    <div class="w3-row w3-padding-64"></div>
    <div class="w3-bottom">
        <div class="w3-row w3-red w3-padding"></div>
        <footer class="w3-row w3-black w3-padding">
            <div class="w3-right w3-text-white w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
        </footer>
    </div>

</body>
</html>
