<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />
<c:set var="commCreate" value="${ForwardConst.CMD_CREATE.getValue()}" />
<c:set var="commDestroy" value="${ForwardConst.CMD_DESTROY.getValue()}" />
<c:import url="../layout/app.jsp">
    <c:param name="content">
        <c:if test="${flush != null}">
            <div id="flush_success">
                <c:out value="${flush}"></c:out>
            </div>
        </c:if>
        <h2>フォロー  一覧</h2>
        <h3> <c:out value='フォロー:${following_count}人'></c:out></h3>
        <table id="follow_list">
            <tbody>
                <tr>
                    <th>名前</th>
                    <th>フォロー</th>
                </tr>
                <c:forEach var="following" items="${followings}" varStatus="status">
                    <tr>
                       <td><c:out value="${following.follower.name}"></c:out></td>
                       <td><a href="<c:url value='?action=${actFol}&command=${commDestroy}&follower=${following.follower.id}&_token=${_token}' />">フォロー解除する</a></td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:param>
</c:import>