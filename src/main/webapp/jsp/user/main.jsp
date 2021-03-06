<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

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
            <div class="buttonCenter">
                <c:choose>
                    <c:when test="${account.balance < settings.minBet * 6 }">
                        <a class="button" style="width: 22em;" href="/jsp/user/profile.jsp#notEnoughMoney"><fmt:message key="main.button.play.noMoney"/></a>
                    </c:when>
                    <c:when test="${account.balance >= settings.minBet * 6 }">
                        <a class="button" href="/jsp/user/game.jsp"><fmt:message key="main.button.play"/></a>
                    </c:when>
                </c:choose>
            </div>
            <div class = "description">
                <div>
                    <h2><fmt:message key="start.gameDescription"/></h2>
                    <p><fmt:message key="start.gameDescription.content"/></p>
                    <h2><fmt:message key="start.howToPlay"/></h2>
                    <ul>
                        <li>
                            <h3><fmt:message key="start.rules"/></h3>
                            <ul>
                                <li><fmt:message key="start.rules.1"/></li>
                                <li><fmt:message key="start.rules.2"/></li>
                            </ul>
                        </li>
                        <li>
                            <h3><fmt:message key="start.betting"/></h3>
                            <ul>
                                <li><fmt:message key="start.betting.1"/></li>
                                <li><fmt:message key="start.betting.2"/></li>
                                <li><fmt:message key="start.betting.3"/></li>
                                <li class="lastLi"><fmt:message key="start.betting.4"/></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-1">
        </div>
    </div>
    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.main" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>
