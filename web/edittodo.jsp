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
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta charset="UTF-8">
    <title>Edit Todo</title>
    <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">

    <style>
        input[type=text], select, textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ccc;
            border-radius: 4px;
            resize: vertical;
        }
    </style>
</head>

<body>
<%--________________HEADER_________________________--%>
<%--medium, large--%>
<div class="w3-row w3-black w3-hide-small">
    <div class="w3-col w3-container m6 w3-black w3-padding w3-text-white w3-hide-small">
        <h1><b>Todo for you</b></h1>
    </div>

    <div class="w3-col w3-container m6 w3-black w3-padding w3-text-white w3-hide-small">
        <div class="w3-dropdown-hover w3-right">
            <button class="w3-button w3-black w3-border-black w3-hover-green w3-hide-small">
                <h3 class="w3-right">${name} <i class="fa fa-user-circle-o w3-xxxlarge w3-margin-left"></i></h3>
            </button>
            <div class="w3-dropdown-content w3-bar-block w3-border w3-hide-small">
                <a href="login.jsp" class="w3-bar-item w3-button w3-black w3-text-white w3-hover-red" style='text-decoration:none;'>
                    <i class="fa fa-sign-out w3-xlarge w3-margin-right"></i><b> log out</b>
                </a>
            </div>
        </div>
    </div>

    <%-- Dropdown Click

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
    --%>
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
<div class="w3-row w3-padding w3-hide-small"></div>
<div class="w3-row">
    <div class="w3-col m2 l3 w3-padding w3-hide-small"></div>
    <div class="w3-col m8 l6 w3-card-4 w3-light-grey">
        <header class="w3-container w3-indigo">
            <h3 class="w3-margin-top">
                <b>Edit Todo number ${todoId}</b>
                <a href="todolist.jsp"><i class="w3-btn fa fa-arrow-left w3-xlarge w3-right w3-margin-bottom w3-margin-left w3-text-white"></i></a>
            </h3>
        </header>

        <div class="w3-container">
            <form action="todoFSM.do" method="post" class="w3-container  w3-text-indigo w3-margin">
                <div>
                    <label><b>Todo:</b></label>
                    <textarea class="w3-hover-grey" name="newTodo" rows="2" cols="20" required>${todoTitle}</textarea>
                </div>
                <div class="w3-padding-16">
                    <label><b>Category:</b></label>
                    <input class="w3-hover-grey" type="text"  name="category" value="${category}" required/>
                </div>
                <div class="w3-padding-16">
                    <label><b>Due Date:</b></label>
                    <input type= "date" name="dueDate" value="${todoDate}"/>
                </div>
                <div class="w3-padding-16">
                    <div>
                        <input class="w3-radio" type="radio" name="priority" value="high" ${highPriority}>
                        <label><b>High Priority</b></label>
                    </div>
                    <div>
                        <input class="w3-radio" type="radio" name="priority" value="normal" ${normalPriority}>
                        <label><b>Normal Priority</b></label>
                    </div>
                </div>
                <div class="w3-center w3-padding-24">
                    <input class="w3-btn w3-center w3-padding-large w3-green" style="width:50%;font-size:110%;font-weight:bold;" type="submit" value="Apply"/>
                </div>
                <input type="hidden" name="redirect" value="newTodo"/>
            </form>
        </div>
    </div>
    <div class="w3-col m2 l3 w3-padding"></div>
    <input type="hidden" name="todoId" value="${todoId}">
    <input type="hidden" name="redirect" value="fromUpdateTodo"/>
</div>




<%--<h1>Update Todo '${todoTitle}'</h1>--%>
<%--<h2>hello ${name}</h2>--%>
<%--<form action="todoFSM.do" method="post">
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
--%>


<%--________________Footer_________________________--%>
<div class="w3-row w3-padding-64"></div>
<div class="w3-bottom w3-black">
    <footer class="w3-col w3-container m12 w3-black w3-padding">
        <div class="w3-right w3-text-white w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
    </footer>
</div>

</body>
</html>