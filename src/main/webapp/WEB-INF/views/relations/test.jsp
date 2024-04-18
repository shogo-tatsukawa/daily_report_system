<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="constants.ForwardConst" %>

<c:set var="actRel" value="${ForwardConst.ACT_REL.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commShow" value="${ForwardConst.CMD_SHOW.getValue()}" />
<c:set var="commNew" value="${ForwardConst.CMD_NEW.getValue()}" />
<c:set var="commIdxFol" value="${ForwardConst.CMD_INDEX_FOLLOWED.getValue()}" />

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>
            <c:out value="${sessionScope.login_employee.name}" />&nbsp;さん&nbsp;のフォロワーリスト
        </h2>
        <table id="relation_list">
            <tbody>
                <tr>
                    <th class="follower_id">従業員ID</th>
                    <th class="followed_name">フォロワー名</th>
                </tr>
                <c:forEach var="relation" items="${relations}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="follower_id"><c:out value="${relation.follower_id}" /></td>
                        <td class="followed_name"><c:out value="${relation.followed.name}" /></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:param>
</c:import>