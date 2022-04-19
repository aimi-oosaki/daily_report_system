<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="constants.ForwardConst" %>
<%@ page import="constants.AttributeConst" %>

<c:set var="actEmp" value="${ForwardConst.ACT_EMP.getValue()}" />
<c:set var="commIdx" value="${ForwardConst.CMD_INDEX.getValue()}" />
<c:set var="commEdit" value="${ForwardConst.CMD_EDIT.getValue()}" />
<c:set var="actFol" value="${ForwardConst.ACT_FOL.getValue()}" />  <!-- ★追記 -->
<c:set var="commCreate" value="${ForwardConst.CMD_CREATE.getValue()}" /> <!-- ★追記 -->
<c:set var="commDestroy" value="${ForwardConst.CMD_DESTROY.getValue()}" /> <!-- ★追記 -->

<c:import url="/WEB-INF/views/layout/app.jsp">
    <c:param name="content">

        <h2>id : ${employee.id} の従業員情報 詳細ページ</h2>

        <table>
            <tbody>
                <tr>
                    <th>社員番号</th>
                    <td><c:out value="${employee.code}" /></td>
                </tr>
                <tr>
                    <th>氏名</th>
                    <td><c:out value="${employee.name}" /></td>
                </tr>
                <tr>
                    <th>権限</th>
                    <td><c:choose>
                            <c:when test="${employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">管理者</c:when>
                            <c:otherwise>一般</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>役職</th>
                    <td><c:choose>
                            <c:when test="${employee.positionFlag == AttributeConst.ROLE_MANEGER.getIntegerValue()}">管理職</c:when>
                            <c:otherwise>一般職</c:otherwise>
                        </c:choose>
                    </td>
                </tr>
                <tr>
                    <th>登録日時</th>
                    <fmt:parseDate value="${employee.createdAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="createDay" type="date" />
                    <td><fmt:formatDate value="${createDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
                <tr>
                    <th>更新日時</th>
                    <fmt:parseDate value="${employee.updatedAt}" pattern="yyyy-MM-dd'T'HH:mm:ss" var="updateDay" type="date" />
                    <td><fmt:formatDate value="${updateDay}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                </tr>
            </tbody>
        </table>
        <!-- ★追記 -->
        <c:if test="${sessionScope.login_employee.adminFlag == AttributeConst.ROLE_ADMIN.getIntegerValue()}">
        <!-- 管理者の場合のみ従業員情報編集を表示 -->
            <p>
                <a href="<c:url value='?action=${actEmp}&command=${commEdit}&id=${employee.id}' />">この従業員情報を編集する</a>
            </p>
        </c:if>
            <c:choose>
                <c:when test="${sessionScope.login_employee.id == employee.id}">
                <!-- ログインしている本人の場合は何も表示しない -->
                </c:when>
                <c:when test='${follow == AttributeConst.FOLLOWED.getValue()}'>
                <!-- フォロー済みだったらフォロー解除を表示 -->
                    <p>
                        <a href="<c:url value='?action=${actFol}&command=${commDestroy}&follower=${employee.id}&_token=${_token}' />">フォロー解除</a>
                    </p>
                </c:when>
                <c:when test='${follow == AttributeConst.UNFOLLOW.getValue()}'>
                <!--    未フォローだったらフォローするを表示 -->
                      <a href="<c:url value='?action=${actFol}&command=${commCreate}&follower=${employee.id}&_token=${_token}' />">フォローする</a>
                </c:when>
           </c:choose>
        <!-- ★追記ここまで -->
        <p>
            <a href="<c:url value='?action=${actEmp}&command=${commIdx}' />">一覧に戻る</a>
        </p>
    </c:param>
</c:import>