<%@ page isErrorPage="true" contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>Blackjack Error Page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
</head>
<body>
    <c:if test="${visitor.role == 'GUEST'}">
        <jsp:forward page="/jsp/guest/start.jsp"/>
    </c:if>

    <%-- Header --%>
    <c:import url="${context}/WEB-INF/jspf/user_header.jsp"/>
    <div class="q"></div>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
            <div class = "description">
                <h1 style="text-align: center"><fmt:message key="error.greeting"/></h1>
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
    <%-- Footer --%>
    <div class="row" id="bottom">
        <c:set var="path" value="path.page.error" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>
</html>
