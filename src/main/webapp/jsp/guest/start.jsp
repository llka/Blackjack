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
    <link type="text/css" rel="stylesheet" href="${context}/css/startpage.css"/>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
    <script src="${context}/js/startpage_scr.js"></script>
    <script src="${context}/js/register_scr.js"></script>
</head>

<body>
    <div class="header" id="topnavStart">
        <div class="title">
            <a href="/jsp/guest/start.jsp"><fmt:message key="application.title"/></a>
        </div>
        <div>
            <a onclick="loginModal.style.display='block';signUpModal.style.display='none';" href="#"><fmt:message key="start.login.button"/></a>
            <a onclick="signUpModal.style.display='block';loginModal.style.display='none';" href="#"><fmt:message key="start.register.button"/></a>
            <a href="javascript:void(0);" class="icon" onclick="responsiveNav()">&#9776;</a>
        </div>
    </div>
    <div class="q"></div>
    <div class="row">
        <div id="id01" class="mask">
            <form class="modal animate" name="loginForm" method="POST" action="/controller" autocomplete="on">
                <input type="hidden" name="command" value="login"/>
                <span onclick="document.getElementById('id01').style.display='none'" class="close" title="<fmt:message key="login.close.title"/>">&times;</span>
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
        <div id="id02" class="mask">
            <form class="modal animate" name="registerForm" onsubmit="return validateForm()" method="POST" action="/controller" enctype="multipart/form-data" autocomplete="on">
                <input type="hidden" name="command" value="register"/>
                <span onclick="document.getElementById('id02').style.display='none'" class="close" title="<fmt:message key="register.close.title"/>">&times;</span>
                <table>
                    <tr class="bordered">
                        <td><fmt:message key="register.firstName"/></td>
                        <td>
                            <input type="text" name="firstName" pattern="[a-zA-Z]+" autofocus required maxlength=50>
                        </td>
                    </tr>
                    <tr class="bordered">
                        <td><fmt:message key="register.lastName"/></td>
                        <td>
                            <input type="text" name="lastName" pattern="[a-zA-Z]+" required  maxlength=50>
                        </td>
                    </tr>
                    <tr>
                        <td><fmt:message key="register.email"/></td>
                        <td class="bordered">
                            <input type="email" name="email" pattern="^([a-z0-9_-]+\.)*[a-z0-9_-]+@[a-z0-9_-]+(\.[a-z0-9_-]+)*\.[a-z]{2,6}$" required title="<fmt:message key="register.email.title"/>"   maxlength=50>
                        </td>
                    </tr>
                    <tr class="bordered">
                        <td><fmt:message key="register.userName"/></td>
                        <td>
                            <input type="text" name="login" pattern="[a-zA-Z0-9_]{4,50}" title="<fmt:message key="register.userName.title"/>" required  maxlength=50>
                        </td>
                    </tr>
                    <tr class="bordered">
                        <td><fmt:message key="register.password"/></td>
                        <td>
                            <input type="password" name="password" pattern="(?=^.{6,50}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$" title="<fmt:message key="register.password.title"/>"  required maxlength=50>
                        </td>
                        <td>
                            <span class="error" id="err-pwd1"></span>
                        </td>
                    </tr>
                    <tr class="bordered">
                        <td><fmt:message key="register.password2"/></td>
                        <td>
                            <input type="password" name="confPassword" pattern="(?=^.{6,50}$)(?=.*\d)(?=.*[a-z])(?=.*[A-Z])(?!.*\s).*$" title="<fmt:message key="register.password2.title"/>"  required maxlength=50>
                        </td>
                        <td>
                            <span class="error" id="err-pwd2"></span>
                        </td>
                    </tr>
                    <tr class="bordered">
                        <td><fmt:message key="register.date"/></td>
                        <td>
                            <input type="date" name="birthDate" step="1" max="1999-01-01" min="1917-01-01" required>
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
            <script> showRegister()</script>
        </div>

        <div class="col-1"></div>
        <div class="col-10">
            <div class="buttonCenter">
                <button class="button" onclick="loginModal.style.display='block';signUpModal.style.display='none';" ><fmt:message key="start.warning"/></button>
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
        <c:set var="path" value="path.page.start" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
    <script>loginModal = document.getElementById('id01'); signUpModal = document.getElementById('id02');</script>
    <script>
        closeModal();
    </script>
</body>
</html>


