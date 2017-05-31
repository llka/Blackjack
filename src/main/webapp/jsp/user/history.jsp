<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="bjtags" prefix="bjtag" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<jsp:useBean id="game" class="ru.ilka.entity.Game" scope="page"/>
<jsp:useBean id="games" class="java.util.ArrayList" scope="request"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/history.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
    <script src="${context}/js/jquery-1.10.2.js"></script>
    <script src="${context}/js/history.js"></script>
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
                <h2 style="text-align: center"><fmt:message key="profile.button.history"/></h2>
                <div id="main">
                    <ul id="holder">
                        <c:forEach items="${games}" var="game">
                            <li>
                                <div class="item">
                                    <div class="time">${game.time}</div>
                                    <div class="bet"><fmt:formatNumber value="${game.bet}" type="currency" /></div>
                                    <div class="result">
                                        <c:choose>
                                            <c:when test="${game.winCoefficient == -1}">
                                                <fmt:message key="history.lose"/>
                                            </c:when>
                                            <c:when test="${game.winCoefficient == -0.5}">
                                                <fmt:message key="history.surrender"/>
                                            </c:when>
                                            <c:when test="${game.winCoefficient == 0}">
                                                <fmt:message key="history.draw"/>
                                            </c:when>
                                            <c:when test="${game.winCoefficient == 1}">
                                                <fmt:message key="history.win"/>
                                            </c:when>
                                            <c:when test="${game.winCoefficient == 1.5}">
                                                <fmt:message key="history.bj"/>
                                            </c:when>
                                            <c:otherwise>
                                                <fmt:message key="history.draw"/>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>
                            </li>
                        </c:forEach>
                    </ul>
                </div>
            </div>
        </div>
        <div class="col-1"></div>
    </div>
</body>

</html>




