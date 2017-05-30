<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="bjtags" prefix="bjtag" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="accounts" class="ru.ilka.entity.Account" scope="page"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/users.css"/>
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
                <div>
                    <h2><fmt:message key="users.text.show"/></h2>
                    <div class="sort">
                        <a class="button" href="/controller?command=sortByProfit"><fmt:message key="users.button.profit"/></a>
                        <a class="button" href="/controller?command=sortByRating"><fmt:message key="users.button.rating"/></a>
                        <a class="button" href="/controller?command=sortByHandsWon"><fmt:message key="users.button.handsWon"/></a>
                        <a class="button" href="/controller?command=sortByBanStatus"><fmt:message key="users.button.banned"/></a>
                        <a class="button" id="globalBan" href="/controller?command=globalBan" title="<fmt:message key="users.button.globalBan.title"/>"><fmt:message key="users.button.globalBan"/></a>
                    </div>
                </div>
                <c:if test="${requestScope.accounts.size() > 1}">
                    <form name="banUsersForm" method="POST" action="/controller">
                        <input type="hidden" name="command" value="banUsers"/>
                        <table>
                            <thead>
                                <tr>
                                    <th><fmt:message key="users.th.id"/></th>
                                    <th><fmt:message key="users.th.login"/></th>
                                    <th><fmt:message key="users.th.invite"/></th>
                                    <th><fmt:message key="users.th.balance"/></th>
                                    <th><fmt:message key="users.th.played"/></th>
                                    <th><fmt:message key="users.th.handsWon"/></th>
                                    <th><fmt:message key="users.th.profit"/></th>
                                    <th><fmt:message key="users.th.rating"/></th>
                                    <th><fmt:message key="users.th.admin"/></th>
                                    <th><fmt:message key="users.th.ban"/></th>
                                </tr>
                            </thead>
                            <tbody>
                                <bjtag:all-users accounts="${requestScope.accounts}"></bjtag:all-users>
                            </tbody>
                        </table>
                        <div class="submit">
                            <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                        </div>
                    </form>
                </c:if>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.users" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>

