<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="messages" />

<html>
<head>
    <title><fmt:message key="msg.title" /></title>
</head>
<body>
<nav>
  <ul>
    <li><a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="msg.nav.home" /></a></li>
    <li><a href="${pageContext.request.contextPath}/users"><fmt:message key="msg.nav.users" /></a></li>
    <li>
        <form action="${pageContext.request.contextPath}/changeLanguage" method="get">
            <select name="lang" onchange="this.form.submit()">
                <option value="en" ${sessionScope.locale.language == 'en' ? 'selected' : ''}>English</option>
                <option value="es" ${sessionScope.locale.language == 'es' ? 'selected' : ''}>Español</option>
            </select>
        </form>
    </li>
  </ul>
</nav>