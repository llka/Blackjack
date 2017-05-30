<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>403</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
</head>
<body>
    <c:choose>
        <c:when test="${visitor.role eq 'GUEST'}">
            <jsp:forward page="/jsp/guest/start.jsp"/>
        </c:when>
        <c:when test="${visitor.role eq 'USER'}">
            <jsp:forward page="/jsp/user/main.jsp"/>
        </c:when>
    </c:choose>

    <%-- Header --%>
    <c:import url="${context}/WEB-INF/jspf/user_header.jsp"/>

    <div class="q"></div>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
            <div class = "description">
                <h1 style="text-align: center">403 Error - Forbidden</h1>
                Request from ${pageContext.errorData.requestURI} is failed
                <br/>
                Servlet name or type: ${pageContext.errorData.servletName}
                <br/>
                Status code: ${pageContext.errorData.statusCode}
                <br/>
                Exception: ${pageContext.errorData.throwable}
            </div>
        </div>
    </div>
</body>
</html>

