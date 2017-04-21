<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="bjtags" prefix="bjtag" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>
<jsp:useBean id="received" class="ru.ilka.entity.Message" scope="page"/>
<jsp:useBean id="sent" class="ru.ilka.entity.Message" scope="page"/>
<c:set var="context" scope="page" value="${pageContext.request.contextPath}"/>

<html>
<head>
    <title><fmt:message key="application.title"/></title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link type="text/css" rel="stylesheet" href="${context}/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${context}/css/messages.css"/>
    <%-- icon --%>
    <link rel="shortcut icon" href="${pageContext.request.contextPath}/image/bj_icon.ico"/>
    <script src="${context}/js/common_scr.js"></script>
    <script src="${context}/js/jquery-1.10.2.js"></script>
    <script src="${context}/js/messages.js"></script>
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
                <div class="error">
                    ${errorReceiverLogin}
                </div>
                <div class="messageNavRow">
                    <a class="button" href="/controller?command=showMessages" id="received"><fmt:message key="messages.button.received"/></a>
                    <button class="button"  onclick="showSent()" id="sent"><fmt:message key="messages.button.sent"/></button>
                    <button class="button" onclick="newMessageModal.style.display='block';" id="new"><fmt:message key="messages.button.new"/></button>
                </div>
                <div id="id01" class="mask">
                    <form class="modal animate" name="newMessageForm" method="POST" action="/controller" autocomplete="on">
                        <input type="hidden" name="command" value="sendMessage"/>
                        <span onclick="document.getElementById('id01').style.display='none'" class="close" title="<fmt:message key="login.close.title"/>">&times;</span>
                        <div class="newMessage">
                            <div class="receiverName">
                                <fmt:message key="messages.label.receiver"/> <input type="text" name="receiver" pattern="[a-zA-Z0-9_]{4,50}" title="" autofocus  required/>
                            </div>
                            <div class="newMessText">
                                <textarea id="inputText" name="text" pattern="[a-zA-Z0-9_.,<>+=-]+" required maxlength=398></textarea>
                            </div>
                            <div class="sendBtn">
                                <input class="button" type="submit" value="<fmt:message key="messages.button.send"/>"/>
                            </div>
                        </div>
                    </form>
                </div>
                <div id="receivedMessages">
                    <c:choose>
                        <c:when test="${requestScope.received.size() > 0}">
                            <h3><fmt:message key="messages.label.received"/></h3>
                            <form name="receivedMessagesForm" method="POST" action="/controller">
                                <input type="hidden" name="command" value="manageMessages"/>
                                <div class="messageRowHeader">
                                    <div class="from"><fmt:message key="messages.column.from"/></div>
                                    <div class="text"><fmt:message key="messages.column.text"/></div>
                                    <div class="read"><fmt:message key="messages.column.mark"/></div>
                                    <div class="delete"><fmt:message key="messages.column.delete"/></div>
                                </div>
                                <bjtag:messages-list messages="${requestScope.received}" received="${true}"></bjtag:messages-list>
                                <div class="sendBtn">
                                    <input class="button" type="submit" value="<fmt:message key="profile.button.save"/>">
                                </div>
                            </form>
                        </c:when>
                        <c:when test="${requestScope.received.size() == 0}">
                            <h3><fmt:message key="messages.label.empty.received"/></h3>
                        </c:when>
                    </c:choose>
                </div>
                <div id="sentMessages">
                    <c:choose>
                        <c:when test="${requestScope.sent.size() > 0}">
                            <h3><fmt:message key="messages.label.sent"/></h3>
                            <div class="messageRowHeader">
                                <div class="from"><fmt:message key="messages.column.to"/></div>
                                <div class="text"><fmt:message key="messages.column.text"/></div>
                                <div class="readSent"><fmt:message key="messages.column.mark"/></div>
                            </div>
                            <bjtag:messages-list messages="${requestScope.sent}" received="${false}"></bjtag:messages-list>
                        </c:when>
                        <c:when test="${requestScope.sent.size() == 0}">
                            <h3><fmt:message key="messages.label.empty.sent"/></h3>
                        </c:when>
                    </c:choose>
                </div>
            </div>
        </div>
        <div class="col-1"></div>
    </div>

    <%-- Footer --%>
    <div class="row">
        <c:set var="path" value="path.page.messages" scope="session"/>
        <c:import url="${context}/WEB-INF/jspf/footer.jsp"/>
    </div>
    <script>newMessageModal = document.getElementById('id01');</script>
    <script>closeModal();</script>
</body>

</html>



