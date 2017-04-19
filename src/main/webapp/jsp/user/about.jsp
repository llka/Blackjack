<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/mainpage.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
</head>
<body>
    <c:choose>
        <c:when test="${visitor.role eq 'GUEST'}">
            <jsp:forward page="/jsp/guest/start.jsp"/>
        </c:when>
    </c:choose>

    <%-- Header --%>
    <c:import url="${context}/WEB-INF/jspf/user_header.jsp"/>

    <div class="q"></div>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
            <div class = "description">
                <div>
                    <h2><fmt:message key="about.FAQ"/></h2>
                    <ul>
                        <li>
                            <h3><fmt:message key="about.FAQ.1q"/></h3>
                            <ul>
                                <li><fmt:message key="about.FAQ.1a1"/> <fmt:formatNumber value="${settings.minBet * 6}" type="currency" /> <fmt:message key="about.FAQ.1a2"/></li>
                            </ul>
                        </li>
                        <li>
                            <h3><fmt:message key="about.FAQ.2q"/></h3>
                            <ul>
                                <li><fmt:message key="about.FAQ.2a"/></li>
                            </ul>
                        </li>
                        <li>
                            <h3><fmt:message key="about.FAQ.3q"/></h3>
                            <ul>
                                <li><fmt:message key="about.FAQ.3a"/></li>
                            </ul>
                        </li>
                        <li>
                            <h3><fmt:message key="about.FAQ.4q"/></h3>
                            <ul>
                                <li><fmt:message key="about.FAQ.4a"/></li>
                            </ul>
                        </li>
                    </ul>
                    <h2><fmt:message key="about.developers"/></h2>
                    <p><fmt:message key="about.developers.content"/></p>
                </div>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.about" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>

