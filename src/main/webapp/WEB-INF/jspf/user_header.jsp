<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
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
            <a href="#news"><fmt:message key="header.refreshNews"/></a>
        </bjtag:admin-nav>
        <a href="/jsp/user/profile.jsp"><fmt:message key="header.profile"/></a>
        <a href="/jsp/user/about.jsp"><fmt:message key="header.about"/></a>
        <a href="/controller?command=logout"><fmt:message key="header.logOut"/></a>
        <a href="javascript:void(0);" class="icon" onclick="responsiveNav()">&#9776;</a>
    </div>
</div>
