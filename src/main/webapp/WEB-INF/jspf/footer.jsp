<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<fmt:setLocale value="${visitor.locale}" scope="session"/>
<fmt:setBundle basename="properties.content"/>

<!-- Footer jsp fragment -->
<footer>
    <div class = "footer">
        <!--<div class="buttonNavigate">
            <input type="button" onclick="history.back();" value="Назад"/>
            <input type="button" onclick="history.go(1);" value="Вперёд"/>
        </div>-->
        <div class="language">
            <h3><fmt:message key="label.language"/> </h3>
            <ul>
                <li>
                    <a href="/controller?command=changelanguage&locale=en_US"><img src="../../image/lang_en.png"></a>
                </li>
                <li>
                    <a href="/controller?command=changelanguage&locale=ru_RU"><img src="../../image/lang_ru.png"></a>
                </li>
            </ul>
        </div>
    </div>
</footer>

