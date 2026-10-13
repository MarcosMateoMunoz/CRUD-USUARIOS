<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../../partials/header.jsp" %>

<fmt:message key="msg.user-list.delete" var="txtDelete" />
<fmt:message key="msg.user-list.confirm" var="txtConfirm" />

<h1><fmt:message key="msg.user-list.title" /></h1>

<a href="users?action=new"><fmt:message key="msg.user-list.add" /></a>

<table border="1">
    <thead>
        <tr>
            <th><fmt:message key="msg.user-list.id" /></th>
            <th><fmt:message key="msg.user-list.username" /></th>
            <th><fmt:message key="msg.user-list.active" /></th>
            <th><fmt:message key="msg.user-list.locked" /></th>
            <th><fmt:message key="msg.user-list.lastchange" /></th>
            <th><fmt:message key="msg.user-list.expires" /></th>
            <th><fmt:message key="msg.user-list.attempts" /></th>
            <th><fmt:message key="msg.user-list.emailverified" /></th>
            <th><fmt:message key="msg.user-list.mustchange" /></th>
            <th><fmt:message key="msg.user-list.actions" /></th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="user" items="${listUsers}">
            <tr>
                <td>${user.id}</td>
                <td><c:out value="${user.username}" /></td>
                <td>${user.active}</td>
                <td>${user.accountNonLocked}</td>
                <td>${user.lastPasswordChange}</td>
                <td>${user.passwordExpiresAt}</td>
                <td>${user.failedLoginAttempts}</td>
                <td>${user.emailVerified}</td>
                <td>${user.mustChangePassword}</td>
                <td>
                    <a href="users?action=edit&id=${user.id}"><fmt:message key="msg.user-list.edit" /></a>
                    <form action="users" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete" />
                        <input type="hidden" name="id" value="${user.id}" />
                        <input type="submit" value="${txtDelete}"
                               onclick="return confirm('${txtConfirm}')" />
                    </form>
                </td>
            </tr>
        </c:forEach>
    </tbody>
</table>

<%@ include file="../../partials/footer.jsp" %>