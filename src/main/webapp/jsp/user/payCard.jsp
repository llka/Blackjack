<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<c:if test="${visitor.role eq 'GUEST'}">
    <jsp:forward page="/jsp/guest/start.jsp"/>
</c:if>
<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/payCard.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${context}/image/bj_icon.ico"/>
    <script src="${context}/js/mainpage_scr.js"></script>
</head>

<body>
    <%-- Header --%>
    <c:import url="${context}/WEB-INF/jspf/user_header.jsp"/>

    <div class="row">
        <div class="col-1"></div>

        <div class="col-10">
            <div class = "description">
                <div class="errorMessage">${payCardMessage}</div>
                <form name="payCardForm" method="POST" action="/controller">
                    <input type="hidden" name="command" value="CHECKPAYCARD"/>
                    <div class="bordered">
                        <div>
                            <div class="card-left">
                                <label><fmt:message key="paycard.number"/></label>
                                <div class="card-numbers">
                                    <input name="number1" type="text"  maxlength="4" class="bordered" pattern="[0-9]{4}"  placeholder="1234" required autofocus="">
                                    <input name="number2" type="text"  maxlength="4" class="bordered" pattern="[0-9]{4}"  placeholder="2345" required>
                                    <input name="number3" type="text"  maxlength="4" class="bordered" pattern="[0-9]{4}"  placeholder="3456" required>
                                    <input name="number4" type="text"  maxlength="4" class="bordered" pattern="[0-9]{4}"  placeholder="4567" required>
                                </div>

                                <label><fmt:message key="paycard.timeLeft"/></label>
                                <div class="card-formgroup">
                                    <input name="timeLeft" type="text" class="bordered" placeholder="ММ" pattern="([1-9]{1})[0-9]*" maxlength="4" value="" required>
                                </div>

                                <label><fmt:message key="paycard.fio"/></label>
                                <div class="card-name">
                                    <input name="fio" class="bordered" maxlength="90" type="text" pattern="[a-zA-Z]{1,30}([ ]{1}[a-zA-Z]{1,30})*$"  placeholder="IVAN IVANOV" required />
                                </div>
                            </div>
                            <div class="card-right">
                                <div class="black-line"></div>
                                <div class="cvc-cvv">
                                    <label>cvc2/cvv2</label>
                                    <input  name="cvc" class="bordered" maxlength="3" pattern="[0-9]{3}" type="text"  placeholder="123" required/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="submit">
                        <input class="button" type="submit" value="<fmt:message key="paycard.submit"/>"/>
                    </div>
                </form>
            </div>
        </div>

        <div class="col-1"/>
    </div>
    <%-- Footer --%>
    <div class="row" id="bottom">
        <c:set var="path" value="path.page.payCard" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
</body>

</html>

