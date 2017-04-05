<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>

<html>
    <head>
        <title><fmt:message key="title.index"/></title>
    </head>
    <body>
    <jsp:forward page="/jsp/guest/start.jsp"/>

    </body>
</html>