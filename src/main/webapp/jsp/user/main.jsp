<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/mainpage.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/mainpage_scr.js"></script>
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
                <a class="button" href="/jsp/user/game.jsp"><fmt:message key="main.button.play"/></a>
                <%--<button class="button"><fmt:message key="main.button.play"/></button>--%>
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
                                <li>The game is played using six decks, which are reshuffled after each hand.</li>
                                <li>Dealer hits on soft 17.  </li>
                                <li>Player Blackjack pays 3 to 2.  </li>
                                <li>Any other winning hand is paid 1 to 1.  </li>
                                <li>Insurance pays 2 to 1.  </li>
                                <li>Split up to three hands. </li>
                                <li>Surrender any first two cards. </li>
                                <li>Split aces receive only one card. </li>
                                <li>No re-splitting of aces. </li>
                                <li>A split ace and a ten-value card is not a Blackjack </li>
                            </ul>
                        </li>
                        <li>
                            <h3><fmt:message key="start.betting"/></h3>
                            <ul>
                                <li>Click on a chip to place a bet.  </li>
                                <li>Additional clicks on the chip interface will add to the wager. </li>
                                <li>To remove a bet, click on the chip in the betting circle. </li>
                                <li class="lastLi">Minimum amount to bet is $1 while the maximum is $500.  </li>
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
