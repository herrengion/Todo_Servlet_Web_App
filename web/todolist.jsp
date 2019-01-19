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
        <table class="w3-table-all w3-card-4 w3-hoverable w3-centered" id="myTable" style="table-layout:fixed;max-width: 100%">
            <tr class="w3-blue-gray" style="font-weight:bold">
                <th width="10%" style="min-width: 7%"></th>
                <th width="56%" style="min-width:45%;vertical-align: middle;text-align: left;">
                            <div>
                                <input class="w3-input w3-border w3-padding" type="text" placeholder="Search Todos.." id="myInput" onkeyup="searchFunction()" style="max-width: 90%;">
                            </div>

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
                <th width="14%" style="min-width: 12%;">
                    <select name="categoryList" id= "categoryList" form="filterCategory" class="s3-select w3-left" style="margin-top:4pt;vertical-align: middle;max-width: 90%">
                        <option value="" disabled selected>Groups</option>
                        <option value="all">all</option>
                        <c:forEach items="${todoUserCategorySet}" var="todoUserCategorySet" >
                            <option value="${todoUserCategorySet}">${todoUserCategorySet}</option>
                        </c:forEach>
                        <form action="todoFSM.do" method="post" id="filterCategory" style="display: inline">
                            <input type="hidden" name="redirect" value="category">
                            <button type="submit" value="filter category" class="w3-btn w3-center fa fa-refresh" style="margin-top:2pt;display: inline"></button>
                        </form>
                    </select>
                </th>
                <th width="10%" style="vertical-align:middle;text-align: center;">
                        till</th>
                <th width="10%"></th></tr>

            <c:forEach items="${todoList}" var="todoInstance" >
                <tr>
                    <td width="10%" style="min-width: 7%;vertical-align:middle;text-align: left;">
                        <c:choose>
                            <c:when test="${todoInstance.completed}">
                                <form action="todoFSM.do" method="post" style="display: inline">
                                    <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                                    <button type="submit" class="w3-btn"><i class="w3-text-black w3-xlarge fa fa-check-square-o"></i></button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form action="todoFSM.do" method="post" style="display: inline">
                                    <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                                    <input type="hidden" name="todoID" value=${todoInstance.id}>
                                    <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                                    <button type="submit" class="w3-btn"><i class="w3-xlarge w3-text-green fa fa-square-o"></i></button>
                                </form>
                            </c:otherwise>
                         </c:choose>
                        <c:choose>
                            <c:when test="${todoInstance.important}">
                                <button style="vertical-align:middle;display: inline;" class="w3-btn w3-text-yellow w3-xlarge fa fa-exclamation-triangle"></button>
                            </c:when>
                            <c:otherwise>
                                <%--leer--%>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td width="56%" style="min-width:45%;word-wrap: break-word;vertical-align:middle;text-align: left;">${todoInstance.title}</td>
                    <td width="14%" style="word-wrap: break-word;vertical-align:middle;text-align: left;">${todoInstance.category}</td>
                    <td width="10%" style="max-width:10%; vertical-align:middle;text-align: center;">${todoInstance.dueDate}</td>
                    <td width="10%" style="max-width:7%;vertical-align:middle;text-align: center;">
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
<c:forEach items="${todoList}" var="todoInstance">
    <div class="w3-row">
        <div class="w3-col m2 w3-padding"></div>

            <%--Importatn Toggle--%>
            <%--
            <c:choose>
                <c:when test="${todoInstance.important}">
                    <div class="w3-col m8 w3-red">
                </c:when>
                <c:otherwise>
                    <div class="w3-col m8 w3-light-blue">
                </c:otherwise>
            </c:choose>
                    --%>
        <div class="w3-col m8 w3-light-blue">
    <c:choose>
        <c:when test="${todoInstance.completed}">
            <form action="todoFSM.do" method="post" style="display: inline">
                <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                <button type="submit" class="w3-btn w3-xlarge fa fa-check-square-o w3-left"></button>
            </form>
        </c:when>
        <c:otherwise>
            <form action="todoFSM.do" method="post" style="display: inline">
                <input type="hidden" name="redirect" value="todoCompletedToggle"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <input type="hidden" name="todoStatus" value=${todoInstance.completed}>
                <button type="submit" class="w3-btn w3-left w3-xlarge w3-text-green fa fa-square-o"></button>
            </form>
        </c:otherwise>
    </c:choose>
            <h6 style="word-wrap: break-word;margin-left: 17%;"><b>${todoInstance.title}</b>
            </h6> <b></b>

                <c:choose>
                    <c:when test="${todoInstance.important}">
                        <form style="display: inline;">
                        <button class="w3-btn w3-text-yellow w3-xlarge w3-left fa fa-exclamation-triangle"></button></form>
                    </c:when>
                    <c:otherwise>

                    </c:otherwise>
                </c:choose>
            <form action="todoFSM.do" method="post" style="display: inline;">
                <input type="hidden" name="redirect" value="discardTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <button type="submit" class="w3-right w3-btn fa fa-trash w3-right w3-xlarge">
                </button>
            </form>
            <form action="todoFSM.do" method="post" style="display: inline">
                <input type="hidden" name="redirect" value="toUpdateTodo"/>
                <input type="hidden" name="todoID" value=${todoInstance.id}>
                <button type="submit" class="w3-right w3-btn fa fa-pencil w3-right w3-xlarge" >
                </button>
            </form>
            <h6 style="margin-left: 17%;margin-top: 15pt;">
                ${todoInstance.dueDate}
                <i style="word-wrap: break-word;" class="w3-right w3-margin-right">${todoInstance.category}</i>
            </h6>
            <%--<c:choose>
                <c:when test="${todoInstance.important}">
                    </div>
                </c:when>
                <c:otherwise>
                    </div>
                </c:otherwise>
                </c:choose>--%>
        <div class="w3-col w3-container m2 w3-padding w3-hide-small"></div>
    </div>
</c:forEach>
</div>

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

