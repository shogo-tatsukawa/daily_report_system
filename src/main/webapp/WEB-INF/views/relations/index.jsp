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
                    <th class="followed_name">フォロワー</th>
                    <th class="relation_action">操作</th>
                </tr>
                <c:forEach var="relation" items="${relations}" varStatus="status">
                    <tr class="row${status.count % 2}">
                        <td class="followed_name"><c:out value="${relation.followed.name}" /></td>
                        <td class="relation_action">
                            <a href="<c:url value='?action=${actRel}&command=${commShow}&id=${relation.followed.id}' />">日報を見る</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <div id="pagination">
            （全 ${relations_count} 件）<br />
            <c:forEach var="i" begin="1" end="${((relations_count - 1) / maxRow) + 1}" step="1">
                <c:choose>
                    <c:when test="${i == page}">
                        <c:out value="${i}" />&nbsp;
                    </c:when>
                    <c:otherwise>
                        <a href="<c:url value='?action=${actRel}&command=${commIdx}&page=${i}' />"><c:out value="${i}" /></a>&nbsp;
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </div>
        <p><a href="<c:url value='?action=${actRel}&command=${commIdxFol}' />">フォロワーが作成した日報一覧をみる</a></p>
    </c:param>
</c:import>