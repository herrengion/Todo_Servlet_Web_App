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
    <title>List Todo</title>
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
<div class="w3-row w3-padding w3-container w3-orange">
    <h2>
        <b>Todo Overview</b>
        <a href="newtodo.jsp"><i class="w3-btn fa fa-plus w3-xxlarge w3-right w3-margin-right w3-margin-left"></i></a>
    </h2>
</div>

<%--________Medium&Large______________--%>
<div class="w3-row w3-padding-24 w3-container w3-hide-small">

    <div class="w3-responsive">
        <table class="w3-table-all w3-card-4 w3-hoverable w3-centered" id="myTable" style="width:100%;">
            <tr class="w3-blue-gray" style="font-weight:bold">
                <th width="10%" style="text-align: center;" class="w3-large">
                        Status</th>
                <th width="10%" style="text-align: center;" class="w3-large">
                        Priority</th>
                <th width="35%" style="text-align: left;" class="w3-large">
                        Todo
                        <div class="w3-dropdown-click">
                            <button onclick="clickFunction()" class="w3-btn w3-small w3-blue-gray w3-border-blue-gray"><i class="w3-text-white fa fa-caret-right"></i></button>
                            <div id="Search" class="w3-dropdown-content 3-card-4" style="min-width: 800%;">
                                <input class="w3-input w3-border w3-padding" type="text" placeholder="Search for Todos.." id="myInput" onkeyup="searchFunction()">
                            </div>
                        </div>

                    <script>
                        function clickFunction() {
                            var x = document.getElementById("Search");
                            if (x.className.indexOf("w3-show") == -1) {
                                x.className += " w3-show";
                            } else {
                                x.className = x.className.replace(" w3-show", "");
                            }
                        }
                    </script>

                    <script>
                        function searchFunction() {
                            var input, filter, table, tr, td, i;
                            input = document.getElementById("myInput");
                            filter = input.value.toUpperCase();
                            table = document.getElementById("myTable");
                            tr = table.getElementsByTagName("tr");
                            for (i = 0; i < tr.length; i++) {
                                td = tr[i].getElementsByTagName("td")[2];
                                if (td) {
                                    txtValue = td.textContent || td.innerText;
                                    if (txtValue.toUpperCase().indexOf(filter) > -1) {
                                        tr[i].style.display = "";
                                    } else {
                                        tr[i].style.display = "none";
                                    }
                                }
                            }
                        }
                    </script>
                </th>
                <th width="20%" style="text-align: left;" class="w3-large">
                    <div>
                        Category
                        <form action="todoFSM.do" method="post" id="filterCategory" style="display: inline" class="margin-left">
                            <input type="hidden" name="redirect" value="category">
                            <button type="submit" value="filter category" class="w3-btn w3-small fa fa-refresh w3-margin-left"></button>
                        </form>
                    </div>
                    <div style="margin-top: 5pt">
                        <select name="categoryList" id= "categoryList" form="filterCategory" class="w3-medium w3-center s3-select">
                            <option value="" disabled selected>Choose your Category</option>
                            <option value="all">all</option>
                            <c:forEach items="${todoUserCategorySet}" var="todoUserCategorySet" >
                                <option value="${todoUserCategorySet}">${todoUserCategorySet}</option>
                            </c:forEach>
                        </select>
                    </div>
                </th>
                <th width="15%" style="text-align: center;" class="w3-large">
                        Due Date</th>
                <th width="10%"></th></tr>

            <c:forEach items="${todoList}" var="todoInstance" >
                <tr>
                    <c:choose>
                        <c:when test="${todoInstance.completed}">
                            <td style="vertical-align:middle;">
                                <form action="todoFSM.do" method="post">
                                    <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                                    <button type="submit" class="w3-btn"><i class="w3-text-black w3-xlarge fa fa-check-square-o"></i></button>
                                </form>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td style="vertical-align:middle;">
                                <form action="todoFSM.do" method="post">
                                    <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                                    <button type="submit" class="w3-btn"><i class="w3-xlarge w3-text-green fa fa-square-o"></i></button>
                                </form>
                            </td>
                        </c:otherwise>
                    </c:choose>
                <%-- Sobald Servlet erstellt, td unten löschen--%>
               <td style="vertical-align:middle;">
                   <button type="submit" class="w3-btn w3-block"></button>
               </td>
               <%-- Muss noch im Servlet verarbeitet werden!!!
                                   <c:choose>
                                       <c:when test="${todoInstance.important}">
                                           <td style="vertical-align:middle">
                                               <form action="todoFSM.do" method="post">
                                                   <input type="hidden" name="redirect" value="todoImportantToggle"/>
                                                   <input type="hidden" name="todoID" value=${todoInstance.id}>
                                                   <input type="hidden" name="todoPriority" value=${todoInstance.priority}>
                                                   <button type="submit" class="w3-btn"><i class="w3-text-yellow w3-xlarge fa fa-exclamation-triangle"></i></button>
                                               </form>
                                           </td>
                                       </c:when>
                                       <c:otherwise>
                                           <td style="vertical-align:middle">
                                               <form action="todoFSM.do" method="post">
                                                   <input type="hidden" name="redirect" value="todoImportantToggle"/>
                                                   <input type="hidden" name="todoID" value=${todoInstance.id}>
                                                   <input type="hidden" name="todoStatus" value=${todoInstance.important}>
                                                   <button type="submit" class="w3-btn w3-block"></button>
                                               </form>
                                           </td>
                                       </c:otherwise>
                                   </c:choose>--%>
               <td style="vertical-align:middle;text-align: left;">${todoInstance.title}</td>
               <td style="vertical-align:middle;text-align: left;">${todoInstance.category}</td>
               <td style="vertical-align:middle;text-align: center;">${todoInstance.dueDate}</td>
               <td style="vertical-align:middle;text-align: center;">
                   <form action="todoFSM.do" method="post" style="display:inline">
                       <input type="hidden" name="redirect" value="toUpdateTodo"/>
                       <input type="hidden" name="todoID" value=${todoInstance.id}>
                       <button type="submit" class="w3-btn fa fa-pencil w3-center w3-xlarge"></button>
                   </form>
                   <form action="todoFSM.do" method="post" style="display:inline">
                       <input type="hidden" name="redirect" value="discardTodo"/>
                       <input type="hidden" name="todoID" value=${todoInstance.id}>
                       <button type="submit" class="w3-btn fa fa-trash w3-center w3-xlarge"></button>
                   </form></td>
                </tr>
            </c:forEach>
        </table>
    </div>
</div>

<%--________Small______________--%>
<div class="w3-hide-medium w3-hide-large">
<%--
    <div class="w3-row w3-padding-32">
        <div class="w3-col w3-container m2 w3-padding">
        </div>
        <div class="w3-col w3-container m8 w3-pale-yellow w3-center w3-padding-32">
            <select name="categoryList" id= "categoryList" form="filterCategory" class="w3-center w3-xlarge s3-select">
                <option value="" disabled selected>Choose your Category</option>
                <option value="all">all</option>
                <c:forEach items="${todoUserCategorySet}" var="todoUserCategorySet" >
                </c:forEach>
            </select>
        </div>
        <div class="w3-col w3-container m2 w3-padding w3-hide-small"></div>
    </div>--%>

    <c:forEach items="${todoList}" var="todoInstance">
    <div class="w3-row w3-padding-16">
        <div class="w3-col w3-container m2 w3-padding"></div>
        <div class="w3-col w3-container m8 w3-light-blue w3-card-4">
            <div class="w3-padding-24 w3-container">
                <c:choose>
                    <c:when test="${todoInstance.completed}">
                        <form action="todoFSM.do" method="post" style="display: inline">
                            <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                            <input type="hidden" name="todoID" value=${todoInstance.id}>
                            <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                            <button type="submit" class="w3-btn w3-left"><i class="w3-text-black w3-xxxlarge fa fa-check-square-o"></i></button>
                        </form>
                    </c:when>
                    <c:otherwise>
                        <form action="todoFSM.do" method="post" style="display: inline">
                            <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                            <input type="hidden" name="todoID" value=${todoInstance.id}>
                            <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                            <button type="submit" class="w3-btn w3-left"><i class="w3-xxxlarge w3-text-green fa fa-square-o"></i></button>
                        </form>
                    </c:otherwise>
                </c:choose>
                    <%-- Sobald Servlet erstellt, td unten löschen--%>

                    <button type="submit" style="display: inline" class="w3-btn w3-right w3-text-yellow w3-xxxlarge fa fa-exclamation-triangle"></button>

                    <%-- Muss noch im Servlet verarbeitet werden!!!
                    <c:choose>
                        <c:when test="${todoInstance.important}">
                                <form action="todoFSM.do" method="post">
                                    <input type="hidden" name="redirect" value="todoImportantToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoPriority" value=${todoInstance.priority}>
                                    <button type="submit" class="w3-btnw3-right"><i class="w3-text-yellow w3-xxxlarge fa fa-exclamation-triangle"></i></button>
                                </form>
                        </c:when>
                        <c:otherwise>
                            <td style="vertical-align:middle">
                                <form action="todoFSM.do" method="post">
                                    <input type="hidden" name="redirect" value="todoImportantToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoStatus" value=${todoInstance.important}>
                                    <button type="submit" class="w3-btn w3-right w3-block"></button>
                                   </form>
                        </c:otherwise>
                    </c:choose>--%>

        <div class="w3-padding w3-container"></div>
        <div class="w3-padding w3-container w3-xxlarge">
            <h3><b>${todoInstance.title}</b></h3>
        </div>
        <div class="w3-padding w3-container w3-xxlarge w3-center">
            <h4><b>Category:</b> <br/>
                    ${todoInstance.category}</h4>
        </div>
        <div class="w3-padding-16 w3-container w3-xxlarge w3-center">
            <h4><b>Due Date:</b> <br/>
                    ${todoInstance.dueDate}</h4>
        </div>
        <div class="w3-padding w3-container"></div>
        <div class="w3-padding w3-container">
            <form action="todoFSM.do" method="post" style="display:inline">
                <input type="hidden" name="redirect" value="toUpdateTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <button type="submit" class="w3-left w3-btn fa fa-pencil w3-center w3-xxxlarge"></button>
            </form>
            <form action="todoFSM.do" method="post" style="display:inline">
                <input type="hidden" name="redirect" value="discardTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <button type="submit" class="w3-right w3-btn fa fa-trash w3-center w3-xxxlarge"></button>
            </form>
        </div>
    </div>


    <div class="w3-col w3-container m2 w3-padding-16"></div>
</div>
</div>
    </c:forEach>
<%--________________Footer_________________________--%>
        <div class="w3-row w3-padding-64"></div>
        <div class="w3-row w3-padding w3-bottom w3-black">
            <footer class="w3-col w3-container m12 w3-black w3-padding">
                <div class="w3-left w3-text-white w3-hide-small">
                    <h6><b>${loginMessage}</b></h6>
                </div>
                <div class="w3-right w3-display-bottomright w3-text-white w3-margin-right w3-padding-24 w3-hide-small">&copy Grunder, Herren, Plüss</div>
            </footer>
        </div>
    </body>
</html>

