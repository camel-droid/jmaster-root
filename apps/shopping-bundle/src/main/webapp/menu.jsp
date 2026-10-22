<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<%-- XXX:【href属性について】
		　 研修テキストではhref属性のURLはプロジェクト名固定になっているので、本サンプルシステム全体でhrel属性を含めてc:urlに変更してコンテキストパスに依存しない形式を採用する --%>
<%-- XXX:【EL式での暗黙オブジェクトについて】
		　 研修テキストではrequestScopeやsessionScopeなどの暗黙オブジェクトを省略しているが、本サンプルシステム全体で可読性を重視してページスコープ以外は暗黙オブジェクトを明示する --%>

<a href="<c:url value="/ShowItemServlet?action=top" />">ようこそ</a>|
<c:forEach items="${applicationScope.categories}" var="category">
<a href="<c:url value="/ShowItemServlet?action=list&code=${category.code}" />">${category.name}</a>|
</c:forEach>
<a href="<c:url value="/CartServlet?action=show" />">カートを見る</a>