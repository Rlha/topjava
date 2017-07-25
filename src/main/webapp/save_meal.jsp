<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Meals</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<h2>Save Meal</h2>
<form method="POST" action='meals' name="saveMeal">
    id : <input type="text" readonly="readonly" name="id" value="<c:out value="${meal.id}"/>"/> <br/>
    dateTime : <input type="text" name="dateTime" value="<c:if test="${not empty meal.dateTime}"><c:out value="${formatter.format(meal.dateTime)}"/></c:if>"/> <br/>
    description : <input type="text" name="description" value="<c:out value="${meal.description}" />"/> <br/>
    calories : <input type="text" name="calories" value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>