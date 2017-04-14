<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/profilepage.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
    <script src="${context}/js/profile_scr.js"></script>
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
        <div class="col-7">
            <div class = "description">
                <h3><fmt:message key="profile.h3.myProfile"/></h3>
                <div class="container">
                    <c:choose>
                        <c:when test="${account.avatar eq 'default_avatar.jpg'}">
                            <img src="${context}/image/default_avatar.jpg" alt="Avatar1">
                        </c:when>
                        <c:otherwise>
                            <img src="/imageLoader" alt="Avatar2">
                        </c:otherwise>
                    </c:choose>
                    <form class="newImg" name="avatarForm" enctype="multipart/form-data" method="POST" action="/controller">
                        <input type="hidden" name="command" value="changeAvatar"/>
                        <input  type="file" name="avatar" accept="image/jpeg">
                        <div class="submit">
                            <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                        </div>
                    </form>
                </div>
                <div class="info">
                        <form name="profileInfoForm" onsubmit="return validateForm()"  method="POST" action="/controller" autocomplete="on">
                            <input type="hidden" name="command" value="info"/>
                            <span class="PasswordMessage">${successPasswordChangeMessage}</span>
                            <span class="ErrorPasswordMessage">${errorPasswordChangeMessage}</span>
                            <table>
                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.firstName"/></span>
                                        <span class="successMessage">${successNameChangeMessage}</span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput">
                                        <input type="text" name="firstName" value="${account.firstName}" pattern="[a-zA-Z]+" required maxlength=50>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.lastName"/></span>
                                        <span class="successMessage">${successLastNameChangeMessage}</span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput">
                                        <input type="text" name="lastName" value="${account.lastName}" pattern="[a-zA-Z]+" required maxlength=50>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.email"/></span>
                                        <span class="successMessage">${successEmailChangeMessage}</span>
                                        <span class="errorMessage">${errorEmailChangeMessage}</span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput">
                                        <input type="email" name="email" value="${account.email}" pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$"  title="<fmt:message key="register.email.title"/>" required  maxlength=50>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.login"/></span>
                                        <span class="successMessage">${successLoginChangeMessage}</span>
                                        <span class="errorMessage">${errorLoginChangeMessage}</span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput">
                                        <input type="text" name="login" value="${account.login}" pattern="[a-zA-Z0-9_]{4,50}" title="<fmt:message key="register.userName.title"/>" required  maxlength=50>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span id="passSpan1" class="infTitle" style="display:none;"><fmt:message key="profile.password"/></span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td id="password1" class="fullInput"></td>
                                    <td>
                                        <span class="error" id="err-pwd1"></span>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span id="passSpan2" class="infTitle" style="display:none;"><fmt:message key="profile.password2"/></span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td id="password2" class="fullInput"></td>
                                    <td>
                                        <span class="error" id="err-pwd2"></span>
                                    </td>
                                </tr>

                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.date"/></span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput" >
                                        <input type="date" name="birthDate" value="${account.birthDate}" readonly/>
                                    </td>
                                </tr>
                                <tr class="inf">
                                    <td>
                                        <span class="infTitle"><fmt:message key="profile.gender"/></span>
                                    </td>
                                </tr>
                                <tr class="bordered">
                                    <td class="fullInput">
                                        <input list="gender" name="gender" value="${fn:toLowerCase(account.gender.toString())}" readonly/>
                                        <datalist id="gender">
                                            <option value="<fmt:message key="register.gender.male"/>">
                                            <option value="<fmt:message key="register.gender.female"/>">
                                        </datalist>
                                        </input>
                                    </td>
                                </tr>
                                <tr class ="submit">
                                    <td colspan="2">
                                        <input id="buttonPassword" class="button" type="button" value="<fmt:message key="profile.button.changePass"/>" onclick="showPassword()">
                                        <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                                    </td>
                                </tr>
                            </table>
                        </form>
                </div>
                <h3><fmt:message key="profile.h3.stat"/></h3>
                    <div class="statistics">
                        <table>
                            <tr>
                                <td style="width: 60%;"><fmt:message key="profile.statistics.hands"/> </td>
                                <td>
                                    ${account.handsPlayed}
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 60%;">
                                    <fmt:message key="profile.statistics.hands.won"/>
                                </td>
                                <td>
                                    ${account.handsWon}
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 60%;">
                                    <fmt:message key="profile.statistics.money.won"/>
                                </td>
                                <td>
                                    ${account.moneyWon}
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 60%;">
                                    <fmt:message key="profile.statistics.money.spend"/>
                                </td>
                                <td>
                                    ${account.moneySpend}
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 60%;">
                                    <fmt:message key="profile.statistics.rating"/>
                                </td>
                                <td>
                                    ${account.rating}
                                </td>
                            </tr>
                        </table>
                    </div>
            </div>
        </div>
        <div class="col-3">
            <div class="description">
                <h3><fmt:message key="profile.h3.settings"/></h3>
                <form class="settings" name="profileSetingsForm" method="POST" action="/controller">
                    <input type="hidden" name="command" value="ProfileSettings"/>
                    <input type="radio" name="currency" value="euro" checked> <fmt:message key="profile.settings.euro"/><br>
                    <input type="radio" name="currency" value="dollar"> <fmt:message key="profile.settings.dollar"/><br>
                    <input type="radio" name="currency" value="byr"> <fmt:message key="profile.settings.rub"/><br>
                    <input type="checkbox" name="tips" value="good" checked> <fmt:message key="profile.settings.help"/><br>
                    <div class="submit">
                        <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                    </div>
                </form>
            </div>

            <div class="description bal">
                <a name="notEnoughMoney"></a>
                <h3><fmt:message key="profile.balance"/></h3>
                <form  name="balanceForm"  method="POST" action="/controller">
                    <input type="hidden" name="command" value="AddBalance"/>
                    <table>
                        <tr class="inf">
                            <td>
                                <span class="errorMessage">${balanceMessage}</span>
                                <span class="infTitle"><fmt:message key="profile.balance.available"/></span>
                            </td>
                        </tr>
                        <tr class="balance">
                            <td>
                                <input type="text" name="currency" value="<fmt:formatNumber value="${account.balance}" type="currency" />" readonly>
                            </td>
                        </tr>

                        <tr class="inf">
                            <td id="amountText" style="display:none;">
                                <span><fmt:message key="profile.balance.addAmount"/></span>
                            </td>
                        </tr>
                        <tr class="balance">
                            <td>
                                <input id="amount" type="text" name="amount" pattern="^[1-9]+[0-9]*([.,]{1}[0-9]+)*$" title="<fmt:message key="profile.balance.addAmount.title"/>" style="display:none;" maxlength="10"  required>
                            </td>
                        </tr>
                    </table>
                    <div class="submit">
                        <input id="show" class="button" onclick="showBalanceAdd()" type="button" value="<fmt:message key="profile.balance.button.add"/>"/>
                    </div>
                </form>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.profile" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>

