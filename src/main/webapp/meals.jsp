<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Meals</h2>
<h2>
    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach items="${mealTos}" var="meal">
            <c:if test="${meal.excess}">
                <tr style="background: red">
                    <td>
                        <javatime:format value="${meal.dateTime}" style="MS"/>
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:if>
            <c:if test="${!meal.excess}">
                <tr style="background: green">
                    <td>
                        <javatime:format value="${meal.dateTime}" style="MS"/>
                    </td>
                    <td>${meal.description}</td>
                    <td>${meal.calories}</td>
                </tr>
            </c:if>
        </c:forEach>
    </table>
</h2>
</body>
</html>
