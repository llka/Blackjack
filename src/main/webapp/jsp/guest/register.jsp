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
    <script src="${context}/js/register_scr.js"></script>
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
                <form name="registerForm" onsubmit="return validateForm()" method="POST" action="/controller" enctype="multipart/form-data" autocomplete="on">
                    <input type="hidden" name="command" value="register"/>
                    <table>
                        <tr class="bordered">
                            <td><fmt:message key="register.firstName"/></td>
                            <td>
                                <input type="text" name="firstName" pattern="[a-zA-Z]+" autofocus required maxlength=30>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.lastName"/></td>
                            <td>
                                <input type="text" name="lastName" pattern="[a-zA-Z]+([ ]{1}[a-zA-Z]+)*" required  maxlength=30>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="register.email"/></td>
                            <td class="bordered">
                                <input type="email" name="email" pattern="^([a-zA-Z0-9_-]+\.)*[a-zA-Z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*(\.){1}[a-z]{2,6}$" required title="<fmt:message key="register.email.title"/>"   maxlength=50>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.userName"/></td>
                            <td>
                                <input type="text" name="login" pattern="[a-zA-Z0-9_]{4,50}" title="<fmt:message key="register.userName.title"/>" required  maxlength=45>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.password"/></td>
                            <td>
                                <input type="password" name="password" pattern="(?=^.{6,50}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s)(?!.*\W).*$" title="<fmt:message key="register.password.title"/>"  required maxlength=50>
                            </td>
                            <td>
                                <span class="error" id="err-pwd1"></span>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.password2"/></td>
                            <td>
                                <input type="password" name="confPassword" pattern="(?=^.{6,50}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s)(?!.*\W).*$" title="<fmt:message key="register.password2.title"/>"  required maxlength=50>
                            </td>
                            <td>
                                <span class="error" id="err-pwd2"></span>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.date"/></td>
                            <td>
                                <input type="date" name="birthDate" step="1" max="1999-01-01" min="1917-01-01" title="<fmt:message key="register.date.title"/>" required>
                            </td>
                        </tr>
                        <tr class="bordered">
                            <td><fmt:message key="register.gender"/></td>
                            <td>
                                <input list="gender" name="gender" required>
                                <datalist id="gender">
                                    <option value="<fmt:message key="register.gender.male"/>">
                                    <option value="<fmt:message key="register.gender.female"/>">
                                </datalist>
                                </input>
                            </td>
                        </tr>
                        <tr>
                            <td><fmt:message key="register.avatar"/> </td>
                            <td>
                                <input type="file" name="avatar" accept="image/jpeg">
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <a class="addCode" href="#" onclick="addCode()"><fmt:message key="register.inviteCode"/></a>
                            </td>
                        </tr>
                        <tr id="addCodeId">
                            <td id="addCodeText"></td>
                            <td class="bordered" id="addCodeInput"></td>
                        </tr>
                        <tr class ="submit">
                            <td colspan="2">
                                <input class="button" type="submit" value="<fmt:message key="register.button"/>">
                            </td>
                        </tr>
                        <tr class="contract">
                            <td colspan="2">
                                <fmt:message key="register.contract"/> <a href="/jsp/guest/eula.jsp"> <fmt:message key="register.contract.href"/></a>.
                            </td>
                        </tr>
                        <h3>${errorRegisterMessage}</h3>
                    </table>
                </form>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.register" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>
</html>
