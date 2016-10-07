<%@ page language="java" contentType="text/html; charset=EUC-KR"  pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
<script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<h1>Board List</h1>
	<table class="table">
		<thead>
			<tr>
				<th>board no</th>
				<th>board title</th>
				<th>delete</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var ="list" items="${boardArticleList }">
				<tr>
					<td>${list.boardArticleNo }</td>
					<td><a href ="/boardDetail?boardArticleNo=${list.boardArticleNo}">${list.boardArticleTitle}</a></td>
					<td><a href ="/boardDelete?boardArticleNo=${list.boardArticleNo}" class="btn btn-default" >DELETE</a></td>
				</tr>
			</c:forEach>
			<tr>
				<td colspan="3" align="right"><a href="/boardAdd" class="btn btn-success">사진 등록하기</a></td>
			</tr>
		</tbody>	
	</table>
	
	<!-- 검색 -->
	<form action="/">
	SEARCH TITLE   <input type = "text" name="word">
	<input type ="submit" value="SEARCH" class="btn btn-default">
	</form>
		
	<!-- 페이징 -->
	<div>
		<ul class="pager">
			 <li>
		<c:if test="${page>1 }">
			<a href="/?page=${preView}">이전</a>
		</c:if>
		<c:forEach var="a" begin="${startpage}" end="${endpage}">
			<c:if test="${a==page}">
			${a}
			</c:if>
			<c:if test="${a!=page}">
			<a href="/?page=${a}">${a}</a>&nbsp;
			</c:if>
		</c:forEach>
		<c:if test="${page<lastPage}">
			<a href="/?page=${nextPage}">다음</a>
		</c:if>
		 	</li>
		</ul>
	</div>
</div>
</body>
</html>