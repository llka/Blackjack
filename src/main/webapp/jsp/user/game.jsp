<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/game.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/game_scr.js"></script>
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
                <div class="table"></div>
                <div class="card1">
                    <div class="cardNumber">A</div>
                    <div class="cardSuit"><div class="spades"></div></div>
                    <div class="cardNumberDown">A</div>
                </div>
                <div class="card2">
                    <div class="cardNumber">K</div>
                    <div class="cardSuit"><div class="clubs"></div></div>
                    <div class="cardNumberDown">K</div>
                </div>
                <div class="card3">
                    <div class="cardNumber">8</div>
                    <div class="cardSuit"><div class="diamonds"></div></div>
                    <div class="cardNumberDown">8</div>
                </div>
                <form class="betForm" onsubmit="return validateBetForm()"  name="betForm"  method="POST">
                    <input type="hidden" name="command" value="DealFirstCards"/>
                    <div class="bets">
                        <div class="bet">
                            <input type="number" name="bet1" min="0" max="${settings.maxBet}" step="${settings.minBet}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet2" min="0" max="${settings.maxBet}" step="${settings.minBet * 2}" value="0">
                        </div>
                        <div class="bet">
                            <input type="number" name="bet3" min="0" max="${settings.maxBet}" step="${settings.minBet * 3}" value="0">
                        </div>
                    </div>
                    <div class="error"><span id="errorEmptyBets"></span></div>
                    <div class="submit">
                        <input class="button" type="submit" value="deal">
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
