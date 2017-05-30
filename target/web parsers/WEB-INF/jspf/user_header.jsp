<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<jsp:useBean id="account" class="ru.ilka.entity.Account" scope="session" />
<fmt:setBundle basename="properties.content"/>
<%@ taglib uri="bjtags" prefix="bjtag" %>

<!-- User's header jsp fragment -->
<div class="header" id="topnav">
    <div class="title">
        <a href="/jsp/user/main.jsp"><fmt:message key="application.title"/></a>
    </div>
    <div>
        <a href="/jsp/user/profile.jsp" >${visitor.name}</a>
        <bjtag:admin-nav role="${visitor.role}">
            <a href="/jsp/admin/users.jsp"><fmt:message key="header.manageUsers"/></a>
            <a href="/jsp/admin/settings.jsp"><fmt:message key="header.settings"/></a>
            <a href="/jsp/admin/messages.jsp"><fmt:message key="header.messages"/></a>
        </bjtag:admin-nav>
        <a style="display: flex" href="/jsp/user/profile.jsp">
            <fmt:message key="header.profile"/>
            <bjtag:count-new-messages accountId="${account.accountId}"></bjtag:count-new-messages>
        </a>
        <a href="/jsp/user/about.jsp"><fmt:message key="header.about"/></a>
        <a href="/controller?command=logout"><fmt:message key="header.logOut"/></a>
        <a href="javascript:void(0);" class="icon" onclick="responsiveNav()">&#9776;</a>
    </div>
</div>
