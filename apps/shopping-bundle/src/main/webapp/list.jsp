<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>商品一覧</title>
</head>
<body id="SCR001">

<jsp:include page="/menu.jsp" />

<h3>商品一覧</h3>
<%--
【追加課題】表示商品が0件のときは「指定されたカテゴリの商品は見つかりませんでした。」と表示し、1件以上のときは商品一覧の表を表示する。 
　　　　　　ShowItemTest.ForCategoryWithoutItemTestクラスの@Disabledアノテーションを削除して実行した結果のエラーを修正する。
　　　　　　ヒント：ShowItemTest.DisplayItemListTest.ForCategoryWithoutItemTest.商品が登録されていないカテゴリをクリックすると商品一覧にメッセージが表示されること() メソッドのエラーを解消するようにJSPを修正する
　　　　　　　　　　条件分岐などのロジックについてはJSTLをキーワードとして調べると幸せになれる
--%>
<!-- TODO:【追加課題】 -->
<c:choose>
	<c:when test="${empty requestScope.items}">
		<p class="message">指定されたカテゴリの商品は見つかりませんでした。</p>
	</c:when>
	<c:otherwise>
	<c:forEach items="${requestScope.items}" var="item">
	    <form action="<c:url value="/CartServlet?action=add" />" method="post">
	        <input type="hidden" name="item_code" value="${item.code}">
	        商品番号：<b>${item.code}</b><br>
	        商品名：<b>${item.name}</b><br>
	        価格(税込)：<b>${item.price}円</b><br>
	        個数：
	        <select name="quantity">
	        <option value="1">1
	        <option value="2">2
	        <option value="3">3
	        <option value="4">4
	        <option value="5">5
	        </select>
	        個<br>
	        <input type="submit" value="カートに追加">
	    </form>
	</c:forEach>
	</c:otherwise>
</c:choose>

</body>
</html>