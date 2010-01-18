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
  <h1>${it.user.steamId}</h1>
  Created: ${it.user.created}

  <ul>
    <c:forEach var="game" items="${it.user.games}">
      <li><a href="${game.url}">${game.name}</a>
    </c:forEach>
  </ul>
</body>
</html>