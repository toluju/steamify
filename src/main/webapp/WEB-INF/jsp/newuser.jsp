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
  <form action="/" method="post">
    <input type="hidden" name="facebookId" value="${it.facebookId}">
    <label for="steamId">Enter your steam id:</label>
    <input type="text" name="steamId">
    <input type="submit" value="Submit">
  </form>
</body>
</html>