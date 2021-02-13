<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://sargue.net/jsptags/time" prefix="javatime" %>
<html>
<head>
    <title>Create or Update</title>
</head>
<body>
<form method="POST" action='meals'>
    Id : <input type="text" name="id" readonly="readonly" value="<c:out value="${meal.id}" />"/> <br/>
    Date Time : <input type="datetime-local" name="dateTime"
                       value="<javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd'T'HH:mm"/>"/> <br/>
    Description : <input type="text" name="description"
                         value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input type="number" name="calories"
                      value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
