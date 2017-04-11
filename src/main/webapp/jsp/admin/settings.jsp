<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="bjtags" prefix="bjtag" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="settings" class="ru.ilka.entity.GameSettings" scope="application"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/login_stylesheet.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
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
                <form name="adminSettingsForm" method="POST" action="/controller" autocomplete="on">
                    <input type="hidden" name="command" value="ChangeSettings"/>
                    <table>
                        <tr class="bordered">
                            <td><fmt:message key="settings.minBet"/></td>
                            <td>
                                <input type="number" name="minBet" pattern="[1-9]+[0-9}?" min="1" value="${settings.minBet}" autofocus required title="<fmt:message key="settings.minBet.title"/>">
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="settings.maxBet"/></td>
                            <td>
                                <input type="number" name="maxBet" pattern="[1-9]+[0-9}?" min="1" value="${settings.maxBet}" required title="<fmt:message key="settings.maxBet.title"/>">
                            </td>
                        </tr>
                        <tr class ="submit">
                            <td colspan="2">
                                <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                            </td>
                        </tr>
                    </table>
                </form>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.settings" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>


