<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/login_stylesheet.css"/>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
</head>
<body>
    <div class="header">
        <div class="title">
            <a href="/jsp/guest/start.jsp"><fmt:message key="application.title"/></a>
        </div>
    </div>
    <div class="q"></div>
    <div class="row">
        <div class="col-1"></div>
        <div class="col-10">
            <div class = "description">
                <form name="loginForm" method="POST" action="/controller" autocomplete="on">
                    <input type="hidden" name="command" value="login"/>
                    <table>
                        <tr class="bordered">
                            <td><fmt:message key="login.emailOrLogin"/></td>
                            <td>
                                <input type="text" name="emailOrLogin" pattern="(^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$)|([a-zA-Z0-9_]{4,50})" autofocus required title="<fmt:message key="login.emailOrLogin.title"/>" maxlength=50>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="login.password"/></td>
                            <td>
                                <input type="password" name="password" pattern="^(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$" title="<fmt:message key="login.password.title"/>"  required>
                            </td>
                        </tr>
                        <tr>
                            <td align=center colspan=2>
                                <input class="button" type="submit" value="<fmt:message key="login.button.submit"/>">
                            </td>
                        </tr>
                        <h3>${errorLoginPassMessage}</h3>
                    </table>
                </form>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.login" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>
</html>
