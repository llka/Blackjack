<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>

<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<c:choose>
    <c:when test="${account.balance < settings.maxBet}">
        <c:set var="betLimit1" scope="page" value="${account.balance / 6}"/>
        <c:set var="betLimit2" scope="page" value="${account.balance / 3}"/>
        <c:set var="betLimit3" scope="page" value="${account.balance / 2}"/>
    </c:when>
    <c:when test="${account.balance >= settings.maxBet && account.balance < settings.maxBet * 3}">
        <c:set var="betLimit1" scope="page" value="${settings.maxBet / 6}"/>
        <c:set var="betLimit2" scope="page" value="${settings.maxBet / 3}"/>
        <c:set var="betLimit3" scope="page" value="${settings.maxBet / 2}"/>
    </c:when>
    <c:when test="${account.balance >= settings.maxBet * 3}">
        <c:set var="betLimit1" scope="page" value="${settings.maxBet}"/>
        <c:set var="betLimit2" scope="page" value="${settings.maxBet}"/>
        <c:set var="betLimit3" scope="page" value="${settings.maxBet}"/>
    </c:when>
</c:choose>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/cards.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/game.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>

    <script src="${context}/js/jquery-1.10.2.js"></script>
    <script src="${context}/js/game_scr.js" type="text/javascript"></script>
    <script src="${context}/js/common_scr.js"></script>
    <script src="${context}/js/jquery-1.10.2.js"></script>
    <script>balanceModal = document.getElementById('modalNoMoney');</script>
</head>

<body>
    <c:choose>
        <c:when test="${visitor.role eq 'GUEST'}">
            <jsp:forward page="/jsp/guest/start.jsp"/>
        </c:when>
        <c:when test="${account.balance < settings.minBet * 6 }">
            <div id="modalNoMoney" class="mask" style="display: block">
                <div class="modal animate">
                    <div class="noMoney">
                        <a class="buttonError" href="/jsp/user/profile.jsp#notEnoughMoney"><fmt:message key="main.button.play.noMoney"/></a>
                    </div>
                </div>
            </div>
        </c:when>
    </c:choose>

    <%-- Header --%>
    <c:import url="${context}/WEB-INF/jspf/user_header.jsp"/>

    <div class="q"></div>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
            <div class = "description">
                <div class="table"></div>
                <div id = "cards"></div>

                <form id="betForm" class="betForm" name="betForm">
                    <input id="command" type="hidden" name="command" value="DealCards"/>
                    <input id="balance" type="hidden" name="actualBalance" value="${account.balance}"/>
                    <div class="bets">
                        <div class="bet">
                            <input type="number" name="bet1" id="bet1input" min="0" max="${betLimit1}" step="${settings.minBet}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet2" id="bet2input" min="0" max="${betLimit2}" step="${settings.minBet * 2}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet3" id="bet3input" min="0" max="${betLimit3}" step="${settings.minBet * 3}" value="0">
                        </div>
                    </div>
                    <div class="error"><span id="betsError"></span></div>
                    <div class="submit">
                        <input class="button" type="button" onclick="validateBetForm()" id="dealBtn" value="<fmt:message key="game.deal"/>">
                    </div>
                </form>
                <div class="gameButtons" id="gameButtons">
                </div>
                <div class="balanceRow">
                    <div></div>
                    <div class="balanceVal"> <fmt:message key="game.balance"/> <fmt:formatNumber value="${account.balance}" type="currency" /></div>
                </div>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.game" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>
