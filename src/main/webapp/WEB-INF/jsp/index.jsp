<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd >
<html>
<head>
  <title>Steamify</title>
</head>
<body>
  <form action="/user" method="post">
    <input type="text" name="name">
    <input type="submit">
  </form>

  <ul>
    <c:forEach var="user" items="${it.users}">
      <li><a href="/user/${user.name}">${user.name}</a>
    </c:forEach>
  </ul>
</body>
</html>