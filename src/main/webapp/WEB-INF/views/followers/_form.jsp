<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.AttributeConst" %>

<label>氏名</label><br />
<c:out value="${sessionScope.login_employee.name}" />
<br /><br />

<label>フォロワー</label><br />
<c:out value="${follower.follower.name}" />
<br /><br />
<input type="hidden" name="${AttributeConst.FLW_ID.getValue()}" value="${follower.id}" />
<input type="hidden" name="${AttributeConst.EMP_ID.getValue()}" value="${follower.follower.id}" />
<input type="hidden" name="${AttributeConst.TOKEN.getValue()}" value="${_token}" />
<button type="submit">投稿</button>