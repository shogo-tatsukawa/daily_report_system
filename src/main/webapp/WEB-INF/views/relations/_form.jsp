<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<label>フォローした人</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label>フォローされた人</label><br />
<!-- <c:out value="${report.employee.name}" /> -->
<c:out value="${relation.followed.name}" />
<br /><br />
<!-- <input type="hidden" name="${AttributeConst.REL_ID.getValue()}" value="${relation.id}" /> -->
<!-- <input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${report.employee.id}" /> -->
<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${relation.followed.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>