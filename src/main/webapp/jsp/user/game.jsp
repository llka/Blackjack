<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>
<c:set var="balance" scope="page" value="${account.balance}"/>
<c:set var="betLimit" scope="page" value="${settings.maxBet}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
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
        <c:when test="${account.balance < settings.minBet * 3 }">
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
                <div id = "cards">
                </div>

                <form id="betForm" class="betForm" name="betForm">
                    <input id="command" type="hidden" name="command" value="DealCards"/>
                    <div class="bets">
                        <c:if test="${balance < settings.maxBet}">
                            <c:set var="betLimit" scope="page" value="${balance}"/>
                        </c:if>
                        <div class="bet">
                            <input type="number" name="bet1" id="bet1input" min="0" max="${betLimit}" step="${settings.minBet}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet2" id="bet2input" min="0" max="${betLimit}" step="${settings.minBet * 2}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet3" id="bet3input" min="0" max="${betLimit}" step="${settings.minBet * 3}" value="0">
                        </div>
                    </div>
                    <div class="error"><span id="betsError"></span></div>
                    <div class="submit">
                        <input class="button" type="button" onclick="return validateBetForm()" id="dealBtn" value="Deal">
                    </div>
                </form>
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
