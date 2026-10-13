<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ include file="../../partials/header.jsp" %>

<c:set var="isEdit" value="${user != null && user.id != null}" />
<c:set var="isNew" value="${user == null}" />
<fmt:message key="msg.user-form.create" var="txtCreate" />
<fmt:message key="msg.user-form.update" var="txtUpdate" />

<h1>
    <c:choose>
        <c:when test="${isEdit}"><fmt:message key="msg.user-form.edit" /></c:when>
        <c:otherwise><fmt:message key="msg.user-form.add" /></c:otherwise>
    </c:choose>
</h1>

<c:if test="${not empty errorKey}">
    <div style="color:red;"><fmt:message key="${errorKey}" /></div>
</c:if>

<form action="users" method="post">
    <input type="hidden" name="action" value="${isEdit ? 'update' : 'insert'}" />
    <input type="hidden" name="id" value="${isEdit ? user.id : ''}" />

    <p>
        <label for="username"><fmt:message key="msg.user-form.username" />:</label>
        <input type="text" name="username" id="username" maxlength="40" required
               value="<c:out value='${user.username}' />" />
    </p>

    <p>
        <label for="passwordHash"><fmt:message key="msg.user-form.password" />:</label>
        <input type="text" name="passwordHash" id="passwordHash" maxlength="500" required
               value="<c:out value='${user.passwordHash}' />" />
    </p>

    <p>
        <label for="active"><fmt:message key="msg.user-form.active" />:</label>
        <input type="checkbox" name="active" id="active" value="true"
               <c:if test="${isNew or (user ne null and user.active)}">checked="checked"</c:if> />
    </p>

    <p>
        <label for="accountNonLocked"><fmt:message key="msg.user-form.nonlocked" />:</label>
        <input type="checkbox" name="accountNonLocked" id="accountNonLocked" value="true"
               <c:if test="${isNew or (user ne null and user.accountNonLocked)}">checked="checked"</c:if> />
    </p>

    <p>
        <label for="lastPasswordChange"><fmt:message key="msg.user-form.lastchange" />:</label>
        <input type="datetime-local" name="lastPasswordChange" id="lastPasswordChange" required
               value="${user != null && user.lastPasswordChange != null ? user.lastPasswordChange : ''}" />
    </p>

    <p>
        <label for="failedLoginAttempts"><fmt:message key="msg.user-form.attempts" />:</label>
        <input type="number" name="failedLoginAttempts" id="failedLoginAttempts" min="0"
               value="${user != null && user.failedLoginAttempts != null ? user.failedLoginAttempts : '0'}" />
    </p>

    <p>
        <label for="emailVerified"><fmt:message key="msg.user-form.emailverified" />:</label>
        <input type="checkbox" name="emailVerified" id="emailVerified" value="true"
               <c:if test="${user ne null and user.emailVerified}">checked="checked"</c:if> />
    </p>

    <p>
        <label for="mustChangePassword"><fmt:message key="msg.user-form.mustchange" />:</label>
        <input type="checkbox" name="mustChangePassword" id="mustChangePassword" value="true"
               <c:if test="${user ne null and user.mustChangePassword}">checked="checked"</c:if> />
    </p>

    <input type="submit" value="${isEdit ? txtUpdate : txtCreate}" />
</form>

<a href="users"><fmt:message key="msg.user-form.returnback" /></a>

<%@ include file="../../partials/footer.jsp" %>