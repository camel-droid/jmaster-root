<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%
String message = "メールアドレスまたはパスワードが一致しませんでした。";
// request.setAttribute("message", message);
%>

<!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="UTF-8">
		<title>ログイン</title>
		<style>
			.error {color: tomato;}
		</style>
	</head>
	<body id="SCR006">
		<h3>ログイン</h3>
		<c:if test="${!empty requestScope.message}">
		<p class="message error">${requestScope.message}</p>
		</c:if>
		<form action="<c:url value="/AuthServlet" />" method="post">
			<table border="1">
				<tr>
					<th>メールアドレス</th>
					<td>
						<input type="text" name="email" />
					</td>
				</tr>
				<tr>
					<th>パスワード</th>
					<td>
						<input type="text" name="password" />
					</td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;">
						<input type="hidden" name="action" value="login" />
						<button name="login">ログイン</button>
					</td>
				</tr>
			</table>
		</form>
	</body>
</html>